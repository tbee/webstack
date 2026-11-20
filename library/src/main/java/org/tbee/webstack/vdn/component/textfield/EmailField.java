package org.tbee.webstack.vdn.component.textfield;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.PlaceholderMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

public class EmailField extends com.vaadin.flow.component.textfield.EmailField
implements ComponentMixin<EmailField>, LabelMixin<EmailField>, SizeMixin<EmailField>, PlaceholderMixin<EmailField> {
    public EmailField() {
    }

    public EmailField(String label) {
        super(label);
    }

    public EmailField(String label, String placeholder) {
        super(label, placeholder);
    }

    public EmailField(ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.EmailField, String>> listener) {
        super(listener);
    }

    public EmailField(String label, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.EmailField, String>> listener) {
        super(label, listener);
    }

    public EmailField(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.EmailField, String>> listener) {
        super(label, initialValue, listener);
    }

    public EmailField value(String v) {
        setValue(v);
        return this;
    }
}
