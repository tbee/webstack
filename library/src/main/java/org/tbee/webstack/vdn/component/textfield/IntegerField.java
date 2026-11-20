package org.tbee.webstack.vdn.component.textfield;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.PlaceholderMixin;

public class IntegerField extends com.vaadin.flow.component.textfield.IntegerField
implements ComponentMixin<IntegerField>, LabelMixin<IntegerField>, PlaceholderMixin<IntegerField> {
    public IntegerField() {
    }

    public IntegerField(String label) {
        super(label);
    }

    public IntegerField(String label, String placeholder) {
        super(label, placeholder);
    }

    public IntegerField(ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.IntegerField, Integer>> listener) {
        super(listener);
    }

    public IntegerField(String label, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.IntegerField, Integer>> listener) {
        super(label, listener);
    }

    public IntegerField(String label, Integer initialValue, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.IntegerField, Integer>> listener) {
        super(label, initialValue, listener);
    }

    // --------------------

    public IntegerField value(int v) {
        setValue(v);
        return this;
    }

    public IntegerField min(int v) {
        setMin(v);
        return this;
    }

    public IntegerField max(int v) {
        setMax(v);
        return this;
    }
}
