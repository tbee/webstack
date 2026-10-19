package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.HasStyle;

public interface StyleMixin<C extends HasStyle> {
    default C style(String key, String value) {
        ((C)this).getStyle().set(key, value);
        return (C)this;
    }

    default C classNames(String... names) {
        ((C)this).addClassNames(names);
        return (C)this;
    }

    // COMMON USAGES

    default C fontSize(String v) {
        return style("font-size", v);
    }

    default C opacity(double v) {
        return style("opacity", ""  + v);
    }

    default C backgroundColor(String v) {
        return style("background-color", v);
    }

    default C cursor(String b) {
        return style("cursor", b);
    }
    default C cursorPointer() {
        return cursor("pointer");
    }

    default C margin(String v) {
        return style("margin", v);
    }
    default C marginLeft(String v) {
        return style("margin-left", v);
    }
    default C marginTop(String v) {
        return style("margin-top", v);
    }
    default C marginRight(String v) {
        return style("margin-right", v);
    }
    default C marginBottom(String v) {
        return style("margin-bottom", v);
    }
}
