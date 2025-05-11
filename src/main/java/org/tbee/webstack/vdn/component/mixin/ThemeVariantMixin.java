package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.shared.HasThemeVariant;

public interface ThemeVariantMixin<T extends com.vaadin.flow.component.shared.ThemeVariant, C extends HasThemeVariant<T>> {

    default C themeVariants(T... variants) {
        ((C)this).addThemeVariants(variants);
        return (C)this;
    }
}
