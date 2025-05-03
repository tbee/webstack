package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.treegrid.TreeGridArrayUpdater;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.function.ValueProvider;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TreeGrid<T> extends com.vaadin.flow.component.treegrid.TreeGrid<T> {

    private GridContextMenu<T> gridContextMenu = null;
    public record Cell<T>(T row, Column<T> column) { }
    private final AtomicReference<Cell<T>> contextMenuClickedCell = new AtomicReference<>();

    public TreeGrid() {
    }

    public TreeGrid(int pageSize, DataCommunicatorBuilder<T, TreeGridArrayUpdater> dataCommunicatorBuilder) {
        super(pageSize, dataCommunicatorBuilder);
    }

    public TreeGrid(Class<T> beanType) {
        super(beanType);
    }

    public TreeGrid(Class<T> beanType, boolean autoCreateColumns) {
        super(beanType, autoCreateColumns);
    }

    public TreeGrid(Class<T> beanType, DataCommunicatorBuilder<T, TreeGridArrayUpdater> dataCommunicatorBuilder) {
        super(beanType, dataCommunicatorBuilder);
    }

    public TreeGrid(HierarchicalDataProvider<T, ?> dataProvider) {
        super(dataProvider);
    }

    // =========================================================

    public TreeGrid<T> refresh() {
        getDataProvider().refreshAll();
        return this;
    }
    public TreeGrid<T> refresh(T item) {
        getDataProvider().refreshItem(item);
        return this;
    }
    public TreeGrid<T> refreshTree(T item) {
        getDataProvider().refreshItem(item, true);
        return this;
    }

    public Set<T> selection() {
        return getSelectedItems();
    }

    public TreeGrid<T> selection(Collection<T> v) {
        getSelectedItems().forEach(this::deselect);
        if (v != null) {
            v.forEach(this::select);
        }
        return this;
    }

    @Override
    public Column<T> addColumn(ValueProvider<T, ?> valueProvider) {
        Column<T> column = super.addColumn(valueProvider);
        column.setId(UUID.randomUUID().toString()); // for addContextMenuItem
        return column;
    }

    @Override
    public Column<T> addColumn(Renderer<T> renderer) {
        Column<T> column = super.addColumn(renderer);
        column.setId(UUID.randomUUID().toString()); // for addContextMenuItem
        return column;
    }

    @Override
    public Column<T> addColumn(String propertyName) {
        Column<T> column = super.addColumn(propertyName);
        column.setId(UUID.randomUUID().toString()); // for addContextMenuItem
        return column;
    }

    @Override
    public GridContextMenu<T> addContextMenu() {
        if (gridContextMenu == null) {
            gridContextMenu = super.addContextMenu();

            // Getting the column requires a kludge: https://vaadin.com/forum/t/how-to-get-the-current-grid-column-when-the-gridcontextmenu-is-opened/158278/13
            gridContextMenu.addGridContextMenuOpenedListener(event -> {
                contextMenuClickedCell.set(null);
                String columnId = event.getColumnId().orElse("");
                if (columnId.isBlank()) {
                    return;
                }
                Column<T> column = getColumns().stream().filter(c -> c.getId().equals(event.getColumnId())).findFirst().orElse(null);
                if (column == null) {
                    return;
                }
                T row = event.getItem().orElse(null);
                contextMenuClickedCell.set(new Cell<>(row, column));
            });
        }
        return gridContextMenu;
    }

    ///  Add a context menu that knows what cell was clicked
    public GridMenuItem<T> addContextMenuItem(String text, Consumer<Cell<T>> listener) {
        return addContextMenu().addItem(text, (ComponentEventListener<GridContextMenu.GridContextMenuItemClickEvent<T>>) event -> listener.accept(contextMenuClickedCell.get()));
    }

    ///  Add a context menu that knows what cell was clicked
    public GridMenuItem<T> addContextMenuItem(Component component, Consumer<Cell<T>> listener) {
        return addContextMenu().addItem(component, (ComponentEventListener<GridContextMenu.GridContextMenuItemClickEvent<T>>) event -> listener.accept(contextMenuClickedCell.get()));
    }

    public void onContextMenuOpen(Consumer<Cell<T>> listener) {
        addContextMenu().addGridContextMenuOpenedListener(e -> listener.accept(contextMenuClickedCell.get()));
    }
}
