package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.shared.SelectionPreservationMode;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class MultiSelectListBox<T> extends com.vaadin.flow.component.listbox.MultiSelectListBox<T>
implements SizeMixin<MultiSelectListBox<T>> {

    public MultiSelectListBox() {
        setSelectionPreservationMode(SelectionPreservationMode.PRESERVE_EXISTING); // otherwise will refresh clear the selection
    }

    public MultiSelectListBox<T> renderer(ComponentRenderer<? extends Component, T> renderer) {
        setRenderer(renderer);
        return this;
    }

    public MultiSelectListBox<T> items(Collection<T> items) {
        setItems(items);
        return this;
    }

    public MultiSelectListBox<T> refresh() {
        getDataProvider().refreshAll();
        return this;
    }

    public Set<T> selection() {
        return getSelectedItems();
    }
    public MultiSelectListBox<T> selection(Collection<T> v) {
        deselect(getSelectedItems());
        select(v);
        // ignore and then manually call public void updateSelection(Set<T> addedItems, Set<T> removedItems)?
        return this;
    }

    public MultiSelectListBox<T> onSelection(Consumer<Set<T>> listener) {
        addSelectionListener(e -> listener.accept(selection()));
        return this;
    }
}
