package org.tbee.webstack.vdn.component.textfield;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.PlaceholderMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

public class PasswordField extends com.vaadin.flow.component.textfield.PasswordField
implements ComponentMixin<PasswordField>, LabelMixin<PasswordField>, SizeMixin<PasswordField>, PlaceholderMixin<PasswordField> {
    public PasswordField() {
    }

    public PasswordField(String label) {
        super(label);
    }

    public PasswordField(String label, String placeholder) {
        super(label, placeholder);
    }

    public PasswordField(ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.PasswordField, String>> listener) {
        super(listener);
    }

    public PasswordField(String label, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.PasswordField, String>> listener) {
        super(label, listener);
    }

    public PasswordField(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.PasswordField, String>> listener) {
        super(label, initialValue, listener);
    }


    public PasswordField value(String v) {
        setValue(v);
        return this;
    }
}
