package org.tbee.webstack.vdn.component.combobox;

import com.vaadin.flow.component.ItemLabelGenerator;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.PlaceholderMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

import java.util.Collection;

public class ComboBox<T> extends com.vaadin.flow.component.combobox.ComboBox<T>
implements ComponentMixin<ComboBox<T>>, LabelMixin<ComboBox<T>>, PlaceholderMixin<ComboBox<T>>, SizeMixin<ComboBox<T>> {

    public ComboBox(int pageSize) {
        super(pageSize);
    }

    public ComboBox() {
    }

    public ComboBox(String label) {
        super(label);
    }

    public ComboBox(String label, Collection<T> items) {
        super(label, items);
    }

    public ComboBox(String label, T... items) {
        super(label, items);
    }

    public ComboBox(ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.combobox.ComboBox<T>, T>> listener) {
        super(listener);
    }

    public ComboBox(String label, ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.combobox.ComboBox<T>, T>> listener) {
        super(label, listener);
    }

    public ComboBox(String label, ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.combobox.ComboBox<T>, T>> listener, T... items) {
        super(label, listener, items);
    }

    public ComboBox<T> onValueChange(ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.combobox.ComboBox<T>, T>> listener) {
        addValueChangeListener(listener);
        return this;
    }

    public ComboBox<T> itemLabelGenerator(ItemLabelGenerator<T> v) {
        super.setItemLabelGenerator(v);
        return this;
    }

    public ComboBox<T> items(Collection<T> v) {
        super.setItems(v);
        return this;
    }

    public ComboBox<T> value(T v) {
        super.setValue(v);
        return this;
    }
}
