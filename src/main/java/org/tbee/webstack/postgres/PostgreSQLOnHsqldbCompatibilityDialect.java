package org.tbee.webstack.postgres;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.LongVarcharJdbcType;
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

    @Override
    protected String columnType(int sqlTypeCode) {
        if (sqlTypeCode == SqlTypes.CLOB) {
            return "text";
        }
        if (sqlTypeCode == SqlTypes.BLOB) {
            return "bytea";
        }
        return super.columnType(sqlTypeCode);
    }

    @Override
    protected String castType(int sqlTypeCode) {
        if (SqlTypes.CLOB == sqlTypeCode) {
            return "text";
        }
        if (SqlTypes.BLOB == sqlTypeCode) {
            return "bytea";
        }
        return super.castType(sqlTypeCode);
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);
        JdbcTypeRegistry jdbcTypeRegistry = typeContributions.getTypeConfiguration().getJdbcTypeRegistry();
        jdbcTypeRegistry.addDescriptor(Types.CLOB, LongVarcharJdbcType.INSTANCE);
    }
}