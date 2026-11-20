package org.tbee.webstack;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.tbee.webstack.vdn.Translations;

/**
 * Brings the beans of this library into the context of the application using it.
 * <p>
 * {@code org.tbee.webstack} is outside the component scan of the application's own base package, so without this
 * the beans here (for example {@link Translations}) would not be found. Spring Boot picks the class up from
 * {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports}, so an application
 * needs no {@code @Import} of its own.
 */
@AutoConfiguration
@Import(Translations.class)
public class Webstack {
}
