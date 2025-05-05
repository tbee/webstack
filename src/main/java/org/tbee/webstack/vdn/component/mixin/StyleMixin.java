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

    default C fontSize(String v) {
        style("font-size", v);
        return (C)this;
    }

    default C opacity(double v) {
        style("opacity", ""  + v);
        return (C)this;
    }
}
