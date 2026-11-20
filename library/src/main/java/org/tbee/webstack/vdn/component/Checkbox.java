package org.tbee.webstack.vdn.component;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

public class Checkbox extends com.vaadin.flow.component.checkbox.Checkbox
implements ComponentMixin<Checkbox>, LabelMixin<Checkbox>, SizeMixin<Checkbox> {
    public Checkbox() {
    }

    public Checkbox(String labelText) {
        super(labelText);
    }

    public Checkbox(boolean initialValue) {
        super(initialValue);
    }

    public Checkbox(String labelText, boolean initialValue) {
        super(labelText, initialValue);
    }

    public Checkbox(String label, ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.checkbox.Checkbox, Boolean>> listener) {
        super(label, listener);
    }

    public Checkbox(boolean initialValue, ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.checkbox.Checkbox, Boolean>> listener) {
        super(initialValue, listener);
    }

    public Checkbox(String labelText, boolean initialValue, ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.checkbox.Checkbox, Boolean>> listener) {
        super(labelText, initialValue, listener);
    }
}
