package org.tbee.webstack.vdn;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Looks up translations, with the option to fall back to a default when a key is not defined.
 * <p>
 * Vaadin's {@code getTranslation} cannot express a default: when the key is missing it returns
 * a marker like {@code "!en:some.key"} instead of the key itself, and when the key is defined with
 * an empty value it returns an empty string. Existence is therefore checked with
 * {@link I18NProvider#getAllTranslations(Locale)}, which contains exactly the keys of the bundle.
 * Note that a key that is present with an empty value counts as existing, so it does <em>not</em>
 * fall back to the default; only a missing key does.
 * <p>
 */
@Component
public class Translations {

    private final I18NProvider i18nProvider;

    public Translations(I18NProvider i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    /** @return whether the key is defined in the bundle of the given locale */
    public boolean has(String key, Locale locale) {
        return i18nProvider.getAllTranslations(locale).containsKey(key);
    }

    /** @return the translation of the key in the current UI locale, or {@code fallback} when the key is not defined */
    public String translateOr(String key, String fallback, Object... params) {
        return translateOr(key, currentLocale(), fallback, params);
    }

    /** @return the translation of the key in the given locale, or {@code fallback} when the key is not defined */
    public String translateOr(String key, Locale locale, String fallback, Object... params) {
        if (!has(key, locale)) {
            return fallback;
        }
        return i18nProvider.getTranslation(key, locale, params);
    }

    private Locale currentLocale() {
        return UI.getCurrent() == null ? Locale.getDefault() : UI.getCurrent().getLocale();
    }
}
