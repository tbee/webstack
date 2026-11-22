package org.tbee.webstack.vdn;

import com.vaadin.flow.i18n.I18NProvider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TranslationsTest {

    /** Like Vaadin, renders "key[arg1,arg2]@locale"; the keys present are what getAllTranslations reports. */
    private final I18NProvider i18nProvider = new I18NProvider() {
        @Override
        public List<Locale> getProvidedLocales() {
            return List.of(Locale.ENGLISH);
        }

        @Override
        public String getTranslation(String key, Locale locale, Object... params) {
            return key + Arrays.toString(params) + "@" + locale.getLanguage();
        }

        @Override
        public Map<String, String> getAllTranslations(Locale locale) {
            return Map.of(
                    "present", "Present",
                    "blank", ""
            );
        }
    };

    private final Translations translations = new Translations(Optional.of(i18nProvider));

    @Test
    void existingKeyReturnsItsTranslation() {
        assertEquals("present[]@en", translations.translateOr("present", Locale.ENGLISH, "fallback"));
    }

    @Test
    void existingKeyIsTranslatedWithParameters() {
        assertEquals("present[a, 1]@en", translations.translateOr("present", Locale.ENGLISH, "fallback", "a", 1));
    }

    @Test
    void missingKeyReturnsTheFallback() {
        assertEquals("fallback", translations.translateOr("missing", Locale.ENGLISH, "fallback"));
    }

    @Test
    void keyWithEmptyValueCountsAsDefinedAndDoesNotFallBack() {
        assertEquals("blank[]@en", translations.translateOr("blank", Locale.ENGLISH, "fallback"));
    }

    @Test
    void hasReportsWhetherTheKeyIsDefined() {
        assertTrue(translations.has("present", Locale.ENGLISH));
        assertTrue(translations.has("blank", Locale.ENGLISH));
        assertFalse(translations.has("missing", Locale.ENGLISH));
    }

    @Test
    void withoutProviderEveryKeyIsMissing() {
        Translations withoutProvider = new Translations(Optional.empty());

        assertFalse(withoutProvider.has("present", Locale.ENGLISH));
        assertEquals("fallback", withoutProvider.translateOr("present", Locale.ENGLISH, "fallback"));
    }
}
