package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.StyleSheet;
import org.tbee.webstack.BuildInfo;
import org.tbee.webstack.vdn.component.html.Div;
import org.tbee.webstack.vdn.component.mixin.LabelMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * A field that selects an integer range by means of one checkbox per step interval between a minimum and a maximum.
 * <p>
 * The value is the selected range: the start is the first selected checkbox's value and the end is the actual end of
 * the last selected checkbox's interval, i.e. the next checkbox's value minus one, capped at the maximum. That way the
 * value and the labels say exactly the same thing.
 * <p>
 * Clicking a selected checkbox in the lower half of the selection deselects it and everything below it; clicking one
 * in the upper half deselects it and everything above it. Clicking a deselected checkbox selects it and everything
 * between it and the selection. At least one checkbox remains selected.
 * <p>
 * Deliberately a {@link CustomField}: the label, the validation state and the error message are drawn by the theme,
 * and a form layout aligns it like any other field.
 */
@StyleSheet("context://org/tbee/webstack/vdn/component/integer-range-checkboxes.css?" + BuildInfo.CACHEBUSTER)
public class IntegerRangeCheckboxes extends CustomField<IntegerRangeCheckboxes.Range>
        implements LabelMixin<IntegerRangeCheckboxes>, SizeMixin<IntegerRangeCheckboxes>,
        StyleMixin<IntegerRangeCheckboxes> {

    /** The value of the field: the start of the first selected interval and the end of the last selected interval. */
    public record Range(int start, int end) {
    }

    /** How the checkboxes are laid out. */
    public enum Orientation {
        VERTICAL, HORIZONTAL
    }

    private static final String CLASS = "integerRangeCheckboxes";
    private static final String CLASS_VERTICAL = "integerRangeCheckboxesVertical";
    private static final String CLASS_HORIZONTAL = "integerRangeCheckboxesHorizontal";

    private final Div container = new Div();
    private final List<Integer> values = new ArrayList<>();
    private final List<Checkbox> checkboxes = new ArrayList<>();

    private int min;
    private int max;
    private int step = 1;
    private Orientation orientation = Orientation.VERTICAL;
    private Function<Integer, String> labelGenerator = this::defaultLabel;
    private int startIndex;
    private int endIndex;
    private boolean updating;

    public IntegerRangeCheckboxes(int min, int max) {
        this(min, max, 1);
    }

    public IntegerRangeCheckboxes(int min, int max, int step) {
        this(null, min, max, step);
    }

    public IntegerRangeCheckboxes(String label, int min, int max) {
        this(label, min, max, 1);
    }

    public IntegerRangeCheckboxes(String label, int min, int max, int step) {
        super(new Range(min, max));
        validate(min, max, step);
        this.min = min;
        this.max = max;
        this.step = step;

        setLabel(label);
        setWidthFull();

        container.classNames(CLASS);
        setOrientationClass();
        add(container);

        buildCheckboxes();
        setPresentationValue(fullRange());
    }

    // --- total range -------------------------------------------------------------------------------------------

    /** Sets the minimum of the total range. */
    public IntegerRangeCheckboxes min(int min) {
        return minMax(min, max);
    }

    /** Returns the minimum of the total range. */
    public int min() {
        return min;
    }

    /** Sets the maximum of the total range. */
    public IntegerRangeCheckboxes max(int max) {
        return minMax(min, max);
    }

    /** Returns the maximum of the total range. */
    public int max() {
        return max;
    }

    /** Sets both ends of the total range. */
    public IntegerRangeCheckboxes minMax(int min, int max) {
        validate(min, max, step);
        Range current = getValue();
        this.min = min;
        this.max = max;
        buildCheckboxes();
        setPresentationValue(current == null ? fullRange() : normalize(current));
        updateValue();
        return this;
    }

    /** Sets the size of the step intervals. */
    public IntegerRangeCheckboxes step(int step) {
        validate(min, max, step);
        Range current = getValue();
        this.step = step;
        buildCheckboxes();
        setPresentationValue(current == null ? fullRange() : normalize(current));
        updateValue();
        return this;
    }

    /** Returns the size of the step intervals. */
    public int step() {
        return step;
    }

    // --- selected range ----------------------------------------------------------------------------------------

    /** Selects every checkbox whose interval overlaps the given range. */
    public IntegerRangeCheckboxes startEnd(int start, int end) {
        setValue(new Range(start, end));
        return this;
    }

    /** Returns the start of the selected range. */
    public int start() {
        return getValue().start();
    }

    /** Returns the end of the selected range. */
    public int end() {
        return getValue().end();
    }

    // --- presentation ------------------------------------------------------------------------------------------

    /** Sets the orientation; null means vertical. */
    public IntegerRangeCheckboxes orientation(Orientation orientation) {
        this.orientation = orientation == null ? Orientation.VERTICAL : orientation;
        setOrientationClass();
        return this;
    }

    /** Returns the orientation. */
    public Orientation orientation() {
        return orientation;
    }

    /** Sets the function that turns a checkbox's value into its label; null restores the default. */
    public IntegerRangeCheckboxes labelGenerator(Function<Integer, String> labelGenerator) {
        this.labelGenerator = labelGenerator == null ? this::defaultLabel : labelGenerator;
        updateLabels();
        return this;
    }

    /** Returns the function that turns a checkbox's value into its label. */
    public Function<Integer, String> labelGenerator() {
        return labelGenerator;
    }

    // --- fluent conveniences -----------------------------------------------------------------------------------

    /** Sets the identifier. */
    public IntegerRangeCheckboxes id(String id) {
        setId(id);
        return this;
    }

    /** Returns the identifier. */
    public String id() {
        return getId().orElse(null);
    }

    /** Sets the enabled state. */
    public IntegerRangeCheckboxes enabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    /** Returns the enabled state. */
    public boolean enabled() {
        return isEnabled();
    }

    /** Sets the visible state. */
    public IntegerRangeCheckboxes visible(boolean visible) {
        setVisible(visible);
        return this;
    }

    /** Returns the visible state. */
    public boolean visible() {
        return isVisible();
    }

    /** Sets the error message. */
    public IntegerRangeCheckboxes errorMessage(String errorMessage) {
        setErrorMessage(errorMessage);
        return this;
    }

    /** Sets the invalid state. */
    public IntegerRangeCheckboxes invalid(boolean invalid) {
        setInvalid(invalid);
        return this;
    }

    // --- CustomField -------------------------------------------------------------------------------------------

    @Override
    protected Range generateModelValue() {
        return new Range(values.get(startIndex), endValue(values.get(endIndex)));
    }

    @Override
    protected void setPresentationValue(Range value) {
        Range range = value == null ? fullRange() : clamp(value);
        startIndex = indexOf(range.start());
        endIndex = indexOf(range.end());
        applySelection();
    }

    @Override
    public void setValue(Range value) {
        super.setValue(value == null ? fullRange() : normalize(value));
    }

    @Override
    public void clear() {
        setValue(fullRange());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if (checkboxes != null) {
            checkboxes.forEach(checkbox -> checkbox.setReadOnly(readOnly));
        }
    }

    @Override
    public void onEnabledStateChanged(boolean enabled) {
        super.onEnabledStateChanged(enabled);
        if (container != null) {
            container.setEnabled(enabled);
        }
        if (checkboxes != null) {
            checkboxes.forEach(checkbox -> checkbox.setEnabled(enabled));
        }
    }

    // --- internals ---------------------------------------------------------------------------------------------

    private void buildCheckboxes() {
        long count = ((long) max - min) / step + 1;
        if (count > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Too many checkboxes: " + count);
        }
        container.removeAll();
        values.clear();
        checkboxes.clear();
        for (int i = 0; i < count; i++) {
            int value = (int) ((long) min + (long) i * step);
            int index = i;
            Checkbox checkbox = new Checkbox(labelGenerator.apply(value));
            checkbox.addValueChangeListener(event -> click(index));
            container.add(checkbox);
            values.add(value);
            checkboxes.add(checkbox);
        }
    }

    private void updateLabels() {
        for (int i = 0; i < checkboxes.size(); i++) {
            checkboxes.get(i).setLabel(labelGenerator.apply(values.get(i)));
        }
    }

    private void setOrientationClass() {
        container.removeClassNames(CLASS_VERTICAL, CLASS_HORIZONTAL);
        container.addClassName(orientation == Orientation.HORIZONTAL ? CLASS_HORIZONTAL : CLASS_VERTICAL);
    }

    /**
     * Handles a click on the checkbox at the given index. The server-side selection before the click decides what the
     * click means, not the checkbox's toggled value.
     */
    void click(int index) {
        if (updating || index < 0 || index >= checkboxes.size()) {
            return;
        }
        boolean wasSelected = index >= startIndex && index <= endIndex;
        if (wasSelected) {
            int middle = startIndex + (endIndex - startIndex) / 2;
            if (index <= middle) {
                int newStartIndex = index + 1;
                if (newStartIndex > endIndex) {
                    applySelection();
                    return;
                }
                startIndex = newStartIndex;
            } else {
                int newEndIndex = index - 1;
                if (newEndIndex < startIndex) {
                    applySelection();
                    return;
                }
                endIndex = newEndIndex;
            }
        } else if (index < startIndex) {
            startIndex = index;
        } else {
            endIndex = index;
        }
        applySelection();
        updateValue();
    }

    private void applySelection() {
        updating = true;
        try {
            for (int i = 0; i < checkboxes.size(); i++) {
                checkboxes.get(i).setValue(i >= startIndex && i <= endIndex);
            }
        } finally {
            updating = false;
        }
    }

    private Range fullRange() {
        return new Range(min, endValue(values.get(values.size() - 1)));
    }

    private Range normalize(Range range) {
        Range clamped = clamp(range);
        return new Range(values.get(indexOf(clamped.start())), endValue(values.get(indexOf(clamped.end()))));
    }

    private Range clamp(Range range) {
        int start = Math.max(min, Math.min(max, range.start()));
        int end = Math.max(min, Math.min(max, range.end()));
        if (start > end) {
            int swap = start;
            start = end;
            end = swap;
        }
        return new Range(start, end);
    }

    private int indexOf(int value) {
        return (int) (((long) value - min) / step);
    }

    private int endValue(int value) {
        return (int) Math.min((long) value + step - 1, max);
    }

    private String defaultLabel(int value) {
        int to = endValue(value);
        return value == to ? String.valueOf(value) : value + " - " + to;
    }

    private static void validate(int min, int max, int step) {
        if (min > max) {
            throw new IllegalArgumentException("min must not be greater than max: " + min + " > " + max);
        }
        if (step <= 0) {
            throw new IllegalArgumentException("step must be greater than 0: " + step);
        }
    }

    // --- for tests ---------------------------------------------------------------------------------------------

    List<Checkbox> checkboxes() {
        return Collections.unmodifiableList(checkboxes);
    }

    List<Integer> values() {
        return Collections.unmodifiableList(values);
    }

    Div container() {
        return container;
    }
}
