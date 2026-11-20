package org.tbee.webstack.vdn.component.textfield;

import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.PlaceholderMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

public class TextArea extends com.vaadin.flow.component.textfield.TextArea
implements ComponentMixin<TextArea>, LabelMixin<TextArea>, SizeMixin<TextArea>, StyleMixin<TextArea>, PlaceholderMixin<TextArea> {
    public TextArea() {
    }

    public TextArea(String label) {
        super(label);
    }

    public TextArea(String label, String placeholder) {
        super(label, placeholder);
    }

    public TextArea(String label, String initialValue, String placeholder) {
        super(label, initialValue, placeholder);
    }

    public TextArea(ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextArea, String>> listener) {
        super(listener);
    }

    public TextArea(String label, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextArea, String>> listener) {
        super(label, listener);
    }

    public TextArea(String label, String initialValue, ValueChangeListener<? super ComponentValueChangeEvent<com.vaadin.flow.component.textfield.TextArea, String>> listener) {
        super(label, initialValue, listener);
    }

    public TextArea value(String v) {
        setValue(v);
        return this;
    }
}
