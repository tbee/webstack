package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

public class H3 extends com.vaadin.flow.component.html.H3
implements ComponentMixin<H3>, SizeMixin<H3>, StyleMixin<H3>, TextMixin<H1> {
    public H3() {
    }

    public H3(Component... components) {
        super(components);
    }

    public H3(String text) {
        super(text);
    }
}
