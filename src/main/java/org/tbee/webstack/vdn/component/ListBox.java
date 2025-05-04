package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class ListBox<T> extends com.vaadin.flow.component.listbox.ListBox<T> {

    public ListBox<T> renderer(ComponentRenderer<? extends Component, T> renderer) {
        setRenderer(renderer);
        return this;
    }

    public ListBox<T> items(Collection<T> items) {
        setItems(items);
        return this;
    }

    public Set<T> selection() {
        return getValue() == null ? Set.of() : Set.of(getValue());
    }
    public ListBox<T> selection(Collection<T> v) {
        if (v.size() > 1) {
            throw new IllegalArgumentException("ListBox can only have a selection of size 1");
        }
        setValue(v.isEmpty() ? null : v.iterator().next());
        return this;
    }

    public ListBox<T> onSelection(Consumer<Set<T>> listener) {
        addValueChangeListener(e -> listener.accept(selection()));
        return this;
    }

    public ListBox<T> sizeFull() {
        setSizeFull();
        return this;
    }

    public void refresh() {
        getDataProvider().refreshAll();
    }
}
