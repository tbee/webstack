package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

public class H1 extends com.vaadin.flow.component.html.H1
implements ComponentMixin<H1>, SizeMixin<H1>, StyleMixin<H1>, TextMixin<H1> {
    public H1() {
    }

    public H1(Component... components) {
        super(components);
    }

    public H1(String text) {
        super(text);
    }
}
