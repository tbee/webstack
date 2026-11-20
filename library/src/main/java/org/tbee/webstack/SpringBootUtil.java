package org.tbee.webstack;

public class SpringBootUtil {

    /// To generate the build-info.properties, use in pom:
    /// 					<execution>
    /// 						<id>build-info</id>
    /// 						<goals>
    /// 							<goal>build-info</goal>
    /// 						</goals>
    /// 					</execution>
    public static String readSpringBootBuildVersion(Class<?> clazz) {
        String version = clazz.getPackage().getImplementationVersion();
        if (version != null) {
            return version;
        }
        try (var in = clazz.getClassLoader().getResourceAsStream("META-INF/build-info.properties")) {
            if (in != null) {
                var props = new java.util.Properties();
                props.load(in);
                version = props.getProperty("build.version");
                if (version != null) {
                    return version;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "dev";
    }
}
