package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.orderedlayout.ThemableLayout;

public interface ThemableLayoutMixin<C extends ThemableLayout> {

    default C padding(Boolean v) {
        ((C)this).setPadding(v);
        return (C)this;
    }
    default Boolean padding() {
        return ((C)this).isPadding();
    }

    default C margin(Boolean v) {
        ((C)this).setMargin(v);
        return (C)this;
    }
    default Boolean margin() {
        return ((C)this).isMargin();
    }

    default C spacing(Boolean v) {
        ((C)this).setSpacing(v);
        return (C)this;
    }
    default Boolean spacing() {
        return ((C)this).isSpacing();
    }

    default C themeList(String v) {
        ((C)this).getThemeList().add(v);
        return (C)this;
    }
}
