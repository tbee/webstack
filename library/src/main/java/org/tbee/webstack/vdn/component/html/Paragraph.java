package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import org.tbee.webstack.vdn.VaadinUtil;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class Paragraph extends com.vaadin.flow.component.html.Paragraph
implements ComponentMixin<Paragraph>, SizeMixin<Paragraph>, StyleMixin<Paragraph> {
    public Paragraph() {
    }

    public Paragraph(Component... components) {
        super(components);
    }

    public Paragraph(String text) {
        super(text);
    }

    public Paragraph onClick(ComponentEventListener<ClickEvent<Paragraph>> listener) {
        super.addClickListener((ComponentEventListener<ClickEvent<com.vaadin.flow.component.html.Paragraph>>) event -> listener.onComponentEvent(VaadinUtil.clone(event)));
        return this;
    }
}
