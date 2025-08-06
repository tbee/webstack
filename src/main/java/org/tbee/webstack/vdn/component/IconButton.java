package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class IconButton extends HorizontalLayout
implements ComponentMixin<IconButton>, StyleMixin<IconButton> {

    public IconButton(Icon icon, ComponentEventListener<ClickEvent<Icon>> clickListener) {
        icon.addClickListener(clickListener);
        icon.getElement().setAttribute("style", "cursor: pointer;");
        add(icon);
    }

    public IconButton(ExternalIcon<?> icon, ComponentEventListener<ClickEvent<Span>> clickListener) {
        icon.addClickListener(clickListener);
        icon.getElement().setAttribute("style", "cursor: pointer;");
        add(icon);
    }
}
