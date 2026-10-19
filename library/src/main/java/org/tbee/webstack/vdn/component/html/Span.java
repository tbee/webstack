package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import org.jspecify.annotations.NonNull;
import org.tbee.webstack.vdn.VaadinUtil;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class Span extends com.vaadin.flow.component.html.Span
implements ComponentMixin<Span>, SizeMixin<Span>, StyleMixin<Span> {
    public Span() {
    }

    public Span(Component... components) {
        super(components);
    }

    public Span(String text) {
        super(text);
    }
}
