package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.MultiSelectionListener;
import com.vaadin.flow.shared.Registration;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class MultiSelectListBox<T> extends com.vaadin.flow.component.listbox.MultiSelectListBox<T> {
    int ignoreSelectionEvents = 0;

    public MultiSelectListBox<T> renderer(ComponentRenderer<? extends Component, T> renderer) {
        setRenderer(renderer);
        return this;
    }

    public MultiSelectListBox<T> items(Collection<T> items) {
        setItems(items);
        return this;
    }

    public MultiSelectListBox<T> refresh() {
        try {
            ignoreSelectionEvents++;

            Set<T> selection = selection();
            getDataProvider().refreshAll(); // this clears the selection
            selection(selection);
            return this;
        }
        finally {
            ignoreSelectionEvents--;
        }
    }

    public Set<T> selection() {
        return getSelectedItems();
    }
    public MultiSelectListBox<T> selection(Collection<T> v) {
        try {
            ignoreSelectionEvents++;

            deselect(getSelectedItems());
            select(v);
            // ignore and then manually call public void updateSelection(Set<T> addedItems, Set<T> removedItems)?
            return this;
        }
        finally {
            ignoreSelectionEvents--;
        }
    }

    @Override
    public Registration addSelectionListener(MultiSelectionListener<com.vaadin.flow.component.listbox.MultiSelectListBox<T>, T> listener) {
        return super.addSelectionListener((MultiSelectionListener<com.vaadin.flow.component.listbox.MultiSelectListBox<T>, T>) multiSelectionEvent -> {
            if (ignoreSelectionEvents > 0) {
                return;
            }
            listener.selectionChange(multiSelectionEvent);
        });
    }

    public MultiSelectListBox<T> onSelection(Consumer<Set<T>> listener) {
        addSelectionListener(e -> listener.accept(selection()));
        return this;
    }

    /// Last clicked on item
    public MultiSelectListBox<T> onClick(Consumer<T> listener) {
        addSelectionListener(e -> {
            Set<T> addedSelection = e.getAddedSelection();
            Set<T> removedSelection = e.getRemovedSelection();
            if (!addedSelection.isEmpty()) {
                listener.accept(addedSelection.iterator().next());
            }
            else if (!removedSelection.isEmpty()) {
                listener.accept(removedSelection.iterator().next());
            }
        });
        return this;
    }

    public MultiSelectListBox<T> sizeFull() {
        setSizeFull();
        return this;
    }
}
