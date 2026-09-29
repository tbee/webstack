package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import org.tbee.webstack.vdn.VaadinUtil;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

public class Div extends com.vaadin.flow.component.html.Div
implements ComponentMixin<Div>, SizeMixin<Div>, StyleMixin<Div>, TextMixin<Div> {
    public Div() {
    }

    public Div(Component... components) {
        super(components);
    }

    public Div(String text) {
        super(text);
    }

    public Div onClick(ComponentEventListener<ClickEvent<Div>> listener) {
        super.addClickListener((ComponentEventListener<ClickEvent<com.vaadin.flow.component.html.Div>>) event -> listener.onComponentEvent(VaadinUtil.clone(event)));
        return this;
    }
}
