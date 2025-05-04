package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;

public interface ComponentMixin<C extends Component> {
    default C id(String id) {
        ((C)this).setId(id);
        return (C)this;
    }
}
