package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasPlaceholder;

public interface PlaceholderMixin<C extends HasPlaceholder> {
    default C placeholder(String v) {
        ((C)this).setPlaceholder(v);
        return (C)this;
    }
    default String placeholder() {
        return ((C)this).getPlaceholder();
    }
}
