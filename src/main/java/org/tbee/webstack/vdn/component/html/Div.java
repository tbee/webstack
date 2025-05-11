package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.Component;
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
}
