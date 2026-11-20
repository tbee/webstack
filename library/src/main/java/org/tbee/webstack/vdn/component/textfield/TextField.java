package org.tbee.webstack.vdn.component.textfield;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.PlaceholderMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

public class TextField extends com.vaadin.flow.component.textfield.TextField
implements ComponentMixin<TextField>, LabelMixin<TextField>, SizeMixin<TextField>, PlaceholderMixin<TextField> {

    public TextField() {
    }

    public TextField(String label) {
        super(label);
    }

    public TextField(String label, String placeholder) {
        super(label, placeholder);
    }

    public TextField(String label, String initialValue, String placeholder) {
        super(label, initialValue, placeholder);
    }

    public TextField(ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextField, String>> listener) {
        super(listener);
    }

    public TextField(String label, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextField, String>> listener) {
        super(label, listener);
    }

    public TextField(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextField, String>> listener) {
        super(label, initialValue, listener);
    }

    // -----------

    public TextField requiredIndicatorVisible(boolean v) {
        setRequiredIndicatorVisible(v);
        return this;
    }

    public TextField value(String v) {
        setValue(v);
        return this;
    }
}
