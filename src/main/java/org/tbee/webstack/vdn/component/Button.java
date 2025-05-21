package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.ButtonVariant;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.TextMixin;

import java.util.function.Consumer;

public class Button extends com.vaadin.flow.component.button.Button
implements ComponentMixin<Button>, TextMixin<Button> {

    public Button() {
    }

    public Button(String text) {
        super(text);
    }

    public Button(Component icon) {
        super(icon);
    }

    public Button(String text, Component icon) {
        super(text, icon);
    }

    public Button(String text, ComponentEventListener<ClickEvent<com.vaadin.flow.component.button.Button>> clickListener) {
        super(text, clickListener);
    }

    public Button(Component icon, ComponentEventListener<ClickEvent<com.vaadin.flow.component.button.Button>> clickListener) {
        super(icon, clickListener);
    }

    public Button(String text, Component icon, ComponentEventListener<ClickEvent<com.vaadin.flow.component.button.Button>> clickListener) {
        super(text, icon, clickListener);
    }

    public Button onClick(Consumer<ClickEvent<com.vaadin.flow.component.button.Button>> listener) {
        addClickListener(listener::accept);
        return this;
    }

    public Button isPrimary(boolean isPrimary) {
        if (isPrimary) {
            addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        return this;
    }
}
