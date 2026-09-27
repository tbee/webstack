package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.combobox.ComboBox;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.PlaceholderMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

import java.util.List;
import java.util.function.Consumer;

public class Select<T> extends com.vaadin.flow.component.select.Select<T>
        implements ComponentMixin<Select<T>>, LabelMixin<Select<T>>, PlaceholderMixin<Select<T>>, SizeMixin<Select<T>> {

    public Select() {
    }

    public Select(String label) {
        setLabel(label);
    }

    public Select(ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.select.Select<T>, T>> listener) {
        super(listener);
    }

    public Select(String label, ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.select.Select<T>, T>> listener) {
        super(label, listener);
    }

    public Select(String label, ValueChangeListener<ComponentValueChangeEvent<com.vaadin.flow.component.select.Select<T>, T>> listener, T... items) {
        super(label, listener, items);
    }

    public Select<T> items(List<T> items) {
        setItems(items);
        return this;
    }

    public T selected() {
        return getValue();
    }
    public Select<T> selected(T v) {
        setValue(v);
        return this;
    }

    public Select<T> onValueChange(Consumer<T> listener) {
        addValueChangeListener(e -> {
            listener.accept(e.getValue());
        });
        return this;
    }

    public Select<T> refresh() {
        getDataProvider().refreshAll();
        return this;
    }
}
