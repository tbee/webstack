package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/// Usage in a Grid:
///
/// ```java
///     private final FilterStringTextField<Person> firstNameFilterTextField = new FilterStringTextField<>(this::applyFilters, Person::firstName);
///     private final FilterStringTextField<Person> lastNameFilterTextField = new FilterStringTextField<>(this::applyFilters, Person::lastName).startsWithIgnoreCase();
///     private final GridListDataView<T> gridListDataView = grid.getListDataView();
///
///     public Constructor() {
///         HeaderRow headerRow = grid.appendHeaderRow();
///         headerRow.getCell(codeColumn).setComponent(firstNameFilterTextField);
///         headerRow.getCell(nameColumn).setComponent(lastNameFilterTextField);
///     }
///
///     private void applyFilters() {
///         gridListDataView.setFilter(firstNameFilterTextField.and(lastNameFilterTextField));
///     }
/// ```
public class FilterStringTextField<T> extends TextField implements SerializablePredicate<T> {
    private final Function<T, String> valueProvider;
    private Predicate<T> predicate = data -> true;

    /// @param applyFilterCallback this is the method that effectuates this filter, probably in combination with others, onto the data.
    /// @param valueProvider the filter usually is applied onto a property of the data, like the name of a person, this provider provides that data
    public FilterStringTextField(Runnable applyFilterCallback, Function<T, String> valueProvider) {
        this.valueProvider = valueProvider;
        containsIgnoreCase();

        setClearButtonVisible(true);
        addThemeVariants(TextFieldVariant.LUMO_SMALL);
        getStyle().set("padding-left", "3px"); // for the focus border
        getStyle().set("padding-right", "3px"); // for the focus border

        setValueChangeMode(ValueChangeMode.EAGER);
        addValueChangeListener(e -> {
            applyFilterCallback.run();
        });
    }

    @Override
    public boolean test(T value) {
        return predicate.test(value);
    }

    public FilterStringTextField<T> filter(Predicate<T> v) {
        Objects.requireNonNull(v);
        this.predicate = v;
        return this;
    }

    /// This is the default filter being used
    public FilterStringTextField<T> containsIgnoreCase() {
        return filter(data -> {
            String filterContent = getValue();
            return filterContent == null || filterContent.isEmpty() || valueProvider.apply(data).toLowerCase().contains(filterContent.toLowerCase());
        });
    }

    public FilterStringTextField<T> contains() {
        return filter(data -> {
            String filterContent = getValue();
            return filterContent == null || filterContent.isEmpty() || valueProvider.apply(data).contains(filterContent);
        });
    }

    public FilterStringTextField<T> startsWithIgnoreCase() {
        return filter(data -> {
            String filterContent = getValue();
            return filterContent == null || filterContent.isEmpty() || valueProvider.apply(data).toLowerCase().startsWith(filterContent.toLowerCase());
        });
    }

    public FilterStringTextField<T> startsWith() {
        return filter(data -> {
            String filterContent = getValue();
            return filterContent == null || filterContent.isEmpty() || valueProvider.apply(data).startsWith(filterContent);
        });
    }
}
