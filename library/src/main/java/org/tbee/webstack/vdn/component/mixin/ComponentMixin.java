package org.tbee.webstack.vdn.component.mixin;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.shared.Tooltip;
import org.tbee.webstack.vdn.VaadinUtil;
import org.tbee.webstack.vdn.component.html.Div;
import org.tbee.webstack.vdn.component.orderedlayout.HorizontalLayout;

import java.util.function.Consumer;

public interface ComponentMixin<C extends Component> {
    default C id(String v) {
        ((C)this).setId(v);
        return (C)this;
    }
    default String id() {
        return ((C)this).getId().orElse(null);
    }

    default C visible(boolean v) {
        ((C)this).setVisible(v);
        return (C)this;
    }
    default boolean visible() {
        return ((C)this).isVisible();
    }

    /// Set the attribute on the associated element
    default C attribute(String attribute, String value) {
        ((C)this).getElement().setAttribute(attribute, value);
        return (C)this;
    }

    default C ariaLabel(String value) {
        return (C)this.attribute("aria-label", value);
    }

    default C tooltip(String value) {
        Tooltip.forComponent((C)this).withText(value);
        return ((C)this);
    }

    default C onClick(Consumer<ClickEvent<C>> listener) {
        ((ClickNotifier<C>)this).addClickListener(listener::accept);
        return ((C)this);
    }

    default C debug() {
        return debug("red");
    }

    default C debug(String color) {
        C c = (C)this;
        c.getStyle().set("border", color + " 1px dashed");
        return c;
    }
}
