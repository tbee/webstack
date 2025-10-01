package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataKeyMapper;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.Rendering;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbee.webstack.vdn.form.AbstractCrudFormLayout;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CrudComponent<E> extends VerticalLayout {
	private static final Logger LOGGER = LoggerFactory.getLogger(CrudComponent.class);

	private final Grid<E> treeGrid = new Grid<>();
	private final Supplier<E> entitySupplier;
	private final Supplier<AbstractCrudFormLayout<E>> formSupplier;
	private final Consumer<E> saver;
	private final Consumer<E> deleter;
	private final Supplier<List<E>> finder;
	private final String title;

	public CrudComponent(String title, Supplier<E> entitySupplier, Consumer<E> saver, Consumer<E> deleter, Supplier<List<E>> finder, Supplier<AbstractCrudFormLayout<E>> formSupplier, Consumer<Grid<E>> setupTreeGrid) {
		this.title = title;
		this.entitySupplier = entitySupplier;
		this.formSupplier = formSupplier;
		this.saver = saver;
		this.deleter = deleter;
		this.finder = finder;

		// treeGrid
		// not needed: treeGrid.setHeightFull();
		treeGrid.setMultiSort(true); // SHIFT click adds columns
		treeGrid.addItemDoubleClickListener(e -> edit());
		treeGrid.addColumn(new ComponentRenderer<>(e -> new CrudIconButtonbar()
				.onEdit(CrudComponent.this::edit)
				.onDelete(CrudComponent.this::delete)
				.padding(false)
		)).setFlexGrow(0).setWidth("80px");
		setupTreeGrid.accept(treeGrid); // do the default setup first, so it may be overridden here

		// crudButtonbar
		CrudButtonbar crudButtonbar = new CrudButtonbar()
				.onReload(this::reloadGrid)
				.onInsert(this::insert);

		// content
		add(crudButtonbar, treeGrid);
		setSizeFull();
	}

	private void insert() {
		E entity = entitySupplier.get();
		AbstractCrudFormLayout<E> form = formSupplier.get();
		form.populateWith(entity);
        ConfirmationDialog.confirmCancel(title, form)
				.confirmText("Save")
				.onConfirm(dialog -> {
					try {
						form.writeTo(entity);
						saver.accept(entity);
						reloadGrid();
                        return true;
                    }
                    catch (ValidationException | RuntimeException e) {
                        VaadinSession.getCurrent().getErrorHandler().error(new ErrorEvent(e));
                        return false;
                    }
				})
				.open();
	}

	private void edit() {
		E item = getSelectedItem();
		if (item == null) {
			return;
		}

		// Dialog
		AbstractCrudFormLayout<E> form = formSupplier.get().populateWith(item);
        ConfirmationDialog.confirmCancel(title, form)
				.confirmText("Save")
				.onConfirm(dialog -> {
					try {
						form.writeTo(item);
						saver.accept(item);
						reloadGrid();
                        return true;
                    }
                    catch (ValidationException | RuntimeException e) {
                        VaadinSession.getCurrent().getErrorHandler().error(new ErrorEvent(e));
                        return false;
                    }
				})
				.open();
	}

	private void delete() {
		E item = getSelectedItem();
		if (item == null) {
			return;
		}

        ConfirmationDialog.confirmCancel("Remove", new NativeLabel("Are you sure?"))
				.confirmText("Yes")
				.onConfirm(dialog -> {
                    try {
                        deleter.accept(item);
                        reloadGrid();
                        return true;
                    }
                    catch (RuntimeException e) {
                        VaadinSession.getCurrent().getErrorHandler().error(new ErrorEvent(e));
                        return false;
                    }
				})
				.open();
	}

	public void reloadGrid() {
		// Remember selection
		E selectedItem = getSelectedItem();

		// Reload persons
		List<E> items = finder.get();
		treeGrid.setItems(items);

		// Reselect
		// TODO: make sure the select node is one from the treeNodes collection, not the old node, otherwise lazy lock goes wrong
//		if (selectedPerson != null) {
//			timezoneTreeGrid.select(selectedPerson);
//		}
	}

	private E getSelectedItem() {
		Set<E> selectedItems = treeGrid.getSelectedItems();
		if (selectedItems.isEmpty() || selectedItems.size() > 1) {
			return null;
		}
		E item = selectedItems.iterator().next();
		return item;
	}
}