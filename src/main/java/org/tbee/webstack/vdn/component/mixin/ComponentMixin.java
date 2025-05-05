package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.Component;

public interface ComponentMixin<C extends Component> {
    default C id(String id) {
        ((C)this).setId(id);
        return (C)this;
    }
}
