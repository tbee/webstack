package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.Component;

public interface ComponentMixin<C extends Component> {
    default C id(String v) {
        ((C)this).setId(v);
        return (C)this;
    }
    default String id() {
        return ((C)this).getId().orElse(null);
    }

    default C visible(boolean v) {
        ((C)this).setVisible(v);
        return (C)this;
    }
    default boolean visible() {
        return ((C)this).isVisible();
    }
}
