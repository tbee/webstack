package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.html.Span;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

abstract public class ExternalIcon<T extends ExternalIcon<?>> extends Span
implements ComponentMixin<T>, StyleMixin<T> {

    public T color(String v) {
        getStyle().set("color", v);
        return (T)this;
    }

    public T size(String v) {
        getStyle().set("font-size", v);
        return (T)this;
    }

    public T opacity(double v) {
        getStyle().set("opacity", "" + v);
        return (T)this;
    }
}
