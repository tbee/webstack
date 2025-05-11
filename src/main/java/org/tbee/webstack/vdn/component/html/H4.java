package org.tbee.webstack.vdn.component.html;

import com.vaadin.flow.component.Component;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

public class H4 extends com.vaadin.flow.component.html.H4
implements ComponentMixin<H4>, SizeMixin<H4>, StyleMixin<H4>, TextMixin<H1> {
    public H4() {
    }

    public H4(Component... components) {
        super(components);
    }

    public H4(String text) {
        super(text);
    }
}
