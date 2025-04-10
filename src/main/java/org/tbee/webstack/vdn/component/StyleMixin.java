package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.HasStyle;

public interface StyleMixin<C extends HasStyle> {
    default C style(String key, String value) {
        ((C)this).getStyle().set(key, value);
        return (C)this;
    }
}
