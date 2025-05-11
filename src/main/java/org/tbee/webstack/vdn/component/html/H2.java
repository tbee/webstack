package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

public class H2 extends com.vaadin.flow.component.html.H2
implements ComponentMixin<H2>, SizeMixin<H2>, StyleMixin<H2>, TextMixin<H1> {
    public H2() {
    }

    public H2(Component... components) {
        super(components);
    }

    public H2(String text) {
        super(text);
    }
}
