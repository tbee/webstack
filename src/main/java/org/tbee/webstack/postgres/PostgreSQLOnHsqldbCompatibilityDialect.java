package org.tbee.webstack.postgres;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.LongVarbinaryJdbcType;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;
import org.hibernate.type.descriptor.jdbc.spi.JdbcTypeRegistry;

import java.sql.Types;

/// For Compatibility with HSQLDB
/// - Map CLOB to TEXT so the @Lob annotation works correctly
///
/// Usage:
/// - In application.yaml:
///     `spring.jpa.properties.hibernate.dialect=org.tbee.webstack.postgresql.PostgreSQLOnHsqldbCompatibilityDialect`
/// - Via system property:
///     `System.setProperty("spring.jpa.database-platform", PostgreSQLOnHsqldbCompatibilityDialect.class.getName());`
/// - In compose.yaml:
///     `SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: org.tbee.webstack.postgres.PostgreSQLOnHsqldbCompatibilityDialect`
public class PostgreSQLOnHsqldbCompatibilityDialect extends PostgreSQLDialect {

    public PostgreSQLOnHsqldbCompatibilityDialect() {
        super();
    }

    public PostgreSQLOnHsqldbCompatibilityDialect(org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo info) {
        super(info);
    }

    @Override
    protected String columnType(int sqlTypeCode) {
        if (sqlTypeCode == SqlTypes.CLOB) {
            return "text";
        }
        if (sqlTypeCode == SqlTypes.BLOB
                || sqlTypeCode == SqlTypes.BINARY
                || sqlTypeCode == SqlTypes.VARBINARY
                || sqlTypeCode == SqlTypes.LONGVARBINARY) {
            return "bytea";
        }
        return super.columnType(sqlTypeCode);
    }

    @Override
    protected String castType(int sqlTypeCode) {
        if (SqlTypes.CLOB == sqlTypeCode) {
            return "text";
        }
        if (SqlTypes.BLOB == sqlTypeCode
                || SqlTypes.BINARY == sqlTypeCode
                || SqlTypes.VARBINARY == sqlTypeCode
                || SqlTypes.LONGVARBINARY == sqlTypeCode) {
            return "bytea";
        }
        return super.castType(sqlTypeCode);
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);
        JdbcTypeRegistry jdbcTypeRegistry = typeContributions.getTypeConfiguration().getJdbcTypeRegistry();

        // CLOB -> behave like text
        jdbcTypeRegistry.addDescriptor(Types.CLOB, LongVarcharJdbcType.INSTANCE);

        // BLOB -> behave like a binary type that maps to bytea.
        // Crucially, this descriptor reports DDL type code BINARY so that
        // schema validation expects bytea (Types#BINARY), matching the
        // actual column, while @Lob byte[] still resolves through BLOB.
        jdbcTypeRegistry.addDescriptor(Types.BLOB, new BlobAsBinaryJdbcType());
    }

    /**
     * A JdbcType registered under Types.BLOB that delegates binding/extraction
     * to the standard VARBINARY handling (bytea on PostgreSQL) and reports its
     * DDL type code as BINARY so schema validation matches a bytea column.
     */
    private static class BlobAsBinaryJdbcType extends VarbinaryJdbcType {
        @Override
        public int getJdbcTypeCode() {
            // Keep BLOB as the registered/JDBC code so @Lob attributes resolve here.
            return Types.BLOB;
        }

        @Override
        public int getDdlTypeCode() {
            // Report BINARY for DDL generation & schema validation (bytea).
            return Types.BINARY;
        }

        @Override
        public int getDefaultSqlTypeCode() {
            return Types.BINARY;
        }
    }
}