package org.tbee.webstack.vdn;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

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
 * The {@link I18NProvider} is optional: an application that does not define one simply has no
 * translations, so every key is considered missing and the fallback is returned.
 * <p>
 */
@Component
public class Translations {

    private final Optional<I18NProvider> i18nProvider;

    @Autowired
    public Translations(Optional<I18NProvider> i18nProvider) {
        this.i18nProvider = i18nProvider;
    }

    /**
     * The same for code that holds an {@link I18NProvider} itself, so a caller does not have to wrap it; a
     * {@code null} provider means the same as an application that defines none.
     */
    public Translations(@Nullable I18NProvider i18nProvider) {
        this(Optional.ofNullable(i18nProvider));
    }

    /** @return whether the key is defined in the bundle of the given locale */
    public boolean has(String key, Locale locale) {
        return i18nProvider
                .map(provider -> provider.getAllTranslations(locale).containsKey(key))
                .orElse(false);
    }

    /** @return the translation of the key in the current UI locale, or {@code fallback} when the key is not defined */
    public String translateOr(String key, String fallback, Object... params) {
        return translateOr(key, currentLocale(), fallback, params);
    }

    /** @return the translation of the key in the given locale, or {@code fallback} when the key is not defined */
    public String translateOr(String key, Locale locale, String fallback, Object... params) {
        return i18nProvider
                .filter(provider -> provider.getAllTranslations(locale).containsKey(key))
                .map(provider -> provider.getTranslation(key, locale, params))
                .orElse(fallback);
    }

    private Locale currentLocale() {
        return UI.getCurrent() == null ? Locale.getDefault() : UI.getCurrent().getLocale();
    }
}
