# webstack

Generic Spring Boot / Vaadin classes.

- `library` - `org.tbee.webstack`.
- `testsupport` - `org.tbee.webstack.test`, the Playwright helpers (`ComboBox`, `Select`, `PlaywrightVdnUtil`);
  add the artifact in test scope, Playwright itself stays the application's dependency.

## Using the library

- The beans are registered by `org.tbee.webstack.Webstack`, an auto-configuration listed in
  `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`; an application needs no
  `@Import` of its own.
- A component's stylesheet lives in this jar in `META-INF/resources/org/tbee/webstack/...` and is loaded as
  `context://org/tbee/webstack/...?` + `BuildInfo.CACHEBUSTER` (generated at build time, so every build busts the
  browser cache).
- The application must permit that path in its security configuration (`/org/tbee/webstack/**`): the container
  serves those files from `META-INF/resources`, which Spring Security does not permit by default, and the link is
  then answered with a redirect to the login page instead of the stylesheet.
