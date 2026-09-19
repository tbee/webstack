package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.HasLabel;

public interface LabelMixin<C extends HasLabel> {
    default C label(String v) {
        ((C)this).setLabel(v);
        return (C)this;
    }
    default String label() {
        return ((C)this).getLabel();
    }
}
