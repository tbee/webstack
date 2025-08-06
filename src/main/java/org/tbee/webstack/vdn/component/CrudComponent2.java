package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CrudComponent2<E, F extends Component> extends VerticalLayout {
	private static final Logger LOGGER = LoggerFactory.getLogger(CrudComponent.class);

	private final Grid<E> treeGrid = new Grid<>();
	private final String title;

	private Supplier<E> entitySupplier;
	private Function<E,F> formSupplier;
	private BiConsumer<E,F> saver;
	private Consumer<E> deleter;
	private Supplier<List<E>> finder;

	public CrudComponent2(String title) {
		this.title = title;

		// treeGrid
		// not needed: treeGrid.setHeightFull();
		treeGrid.setMultiSort(true); // SHIFT click adds columns
		treeGrid.addItemDoubleClickListener(e -> edit());
		treeGrid.addColumn(new ComponentRenderer<>(e -> new CrudIconButtonbar()
				.onEdit(CrudComponent2.this::edit)
				.onDelete(CrudComponent2.this::deletedPopup)
				.padding(false)
		)).setFlexGrow(0).setWidth("80px");

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
		F form = formSupplier.apply(entity);
		ConfirmationDialog.confirmCancel(title, form)
				.confirmText("Save")
				.onConfirm(() -> {
					saver.accept(entity, form);
					reloadGrid();
				})
				.open();
	}

	private void edit() {
		E item = getSelectedItem();
		if (item == null) {
			return;
		}

		// Dialog
		F form = formSupplier.apply(item);
		ConfirmationDialog.confirmCancel(title, form)
				.confirmText("Save")
				.onConfirm(() -> {
					saver.accept(item, form);
					reloadGrid();
				})
				.open();
	}

	private void deletedPopup() {
		E item = getSelectedItem();
		if (item == null) {
			return;
		}

		ConfirmationDialog.confirmCancel("Remove", new NativeLabel("Are you sure?"))
				.confirmText("Yes")
				.onConfirm(() -> {
					deleter.accept(item);
					reloadGrid();
				})
				.open();
	}

	public void reloadGrid() {
		List<E> items = finder.get();
		treeGrid.setItems(items);
	}

	private E getSelectedItem() {
		Set<E> selectedItems = treeGrid.getSelectedItems();
		if (selectedItems.size() != 1) {
			return null;
		}
        return selectedItems.iterator().next();
	}

	// =====================================

	public CrudComponent2<E,F> entity(Supplier<E> entitySupplier) {
		this.entitySupplier = entitySupplier;
		return this;
	}

	public CrudComponent2<E,F> form(Function<E,F> formSupplier) {
		this.formSupplier = formSupplier;
		return this;
	}

	public CrudComponent2<E,F> save(BiConsumer<E, F> saver) {
		this.saver = saver;
		return this;
	}
	public CrudComponent2<E,F> saveVE(BiConsumerValidateException<E, F> saver) {
		this.saver = (e, f) -> {
			try {
				saver.accept(e, f);
			}
			catch (ValidationException ex) {
				throw new RuntimeException(ex);
			}
		};
		return this;
	}

	public CrudComponent2<E,F> delete(Consumer<E> deleter) {
		this.deleter = deleter;
		return this;
	}

	public CrudComponent2<E,F> find(Supplier<List<E>> finder) {
		this.finder = finder;
		return this;
	}

	public CrudComponent2<E,F> treeGrid(Consumer<Grid<E>> setupTreeGrid) {
		setupTreeGrid.accept(treeGrid);
		return this;
	}

	public interface BiConsumerValidateException<T, U> {
		void accept(T t, U u) throws ValidationException;
	}
}