package org.tbee.webstack.vdn.component.slider;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.slider.IntegerRangeSlider;
import com.vaadin.flow.component.slider.IntegerRangeSliderValue;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.tbee.webstack.BuildInfo;
import org.tbee.webstack.vdn.component.html.Span;
import org.tbee.webstack.vdn.component.orderedlayout.HorizontalLayout;
import org.tbee.webstack.vdn.component.orderedlayout.VerticalLayout;

import java.util.function.Function;

/**
 * A slider for a range of two values, with the start and the end shown above the track, so both are visible at all
 * times (Vaadin's own value bubble only appears while a thumb is dragged).
 * <p>
 * Deliberately a {@link CustomField}: that is what a Vaadin component with its own content uses to be a field. The
 * label, the validation state and the error message are drawn by the theme, exactly as they are for a text field,
 * and a form layout aligns it the same way. Nothing about its label or its spacing is a private arrangement with
 * this application, so it can move to another project as it is.
 */
@StyleSheet("context://org/tbee/webstack/vdn/component/labeled-integer-range-slider.css?" + BuildInfo.CACHEBUSTER)
public class LabeledIntegerRangeSlider extends CustomField<LabeledIntegerRangeSlider.Range> {

    /** The value of the field: the start and the end of the selected range. */
    public record Range(int start, int end) {
    }

    private final IntegerRangeSlider slider = new IntegerRangeSlider(Integer.MIN_VALUE, Integer.MAX_VALUE);
    private final Span startLabel = new Span("Start").classNames("sliderValueBubble").cursorPointer();
    private final Span endLabel = new Span("End").classNames("sliderValueBubble").cursorPointer();
    private Function<Integer, String> startLabelFunction;
    private Function<Integer, String> endLabelFunction;

    public LabeledIntegerRangeSlider(String label) {
        this(label, (i) -> ""  + i, (i) -> ""  + i);
    }

    public LabeledIntegerRangeSlider(String label, Function<Integer, String> startLabelFunction, Function<Integer, String> endLabelFunction) {
        // the initial min/max of the slider, until minMax(...) is called by the subclass
        super(new Range(Integer.MIN_VALUE, Integer.MAX_VALUE));
        this.startLabelFunction = startLabelFunction;
        this.endLabelFunction = endLabelFunction;

        setLabel(label);
        setWidthFull();

        slider.setValueAlwaysVisible(false);
        slider.setMinMaxVisible(false);
        slider.setWidthFull();
        slider.addClassNames("hideValueBubble", "labeledIntegerRangeSliderSlider");

        HorizontalLayout startEndLabels = new HorizontalLayout(FlexComponent.JustifyContentMode.BETWEEN, startLabel, endLabel)
                .widthFull()
                .lean()
                .wrap()
                .classNames("labeledIntegerRangeSliderStartEnd");
        VerticalLayout contentLayout = new VerticalLayout(startEndLabels, slider)
                .widthFull()
                .lean()
                .spacing(false)
                .classNames("labeledIntegerRangeSliderField");
        add(contentLayout);

        addClassName("labeledIntegerRangeSlider");
        slider.setValueChangeMode(ValueChangeMode.EAGER);
        slider.addValueChangeListener(event -> {
            setLabelTexts();
            updateValue(); // keep the field's value in step with the thumbs
        });

        startLabel.onClick(e -> decrease());
        endLabel.onClick(e -> increase());
    }

    @Override
    protected Range generateModelValue() {
        IntegerRangeSliderValue value = slider.getValue();
        return new Range(value.start(), value.end());
    }

    @Override
    protected void setPresentationValue(Range value) {
        if (value != null) {
            slider.setValue(new IntegerRangeSliderValue(value.start(), value.end()));
        }
    }

    public LabeledIntegerRangeSlider decrease() {
        return decrease(1);
    }
    public LabeledIntegerRangeSlider decrease(int step) {
        IntegerRangeSliderValue value = slider.getValue();
        if (value.start() - step >= slider.getMin()) {
            slider.setValue(new IntegerRangeSliderValue(value.start() - step, value.end()));
        }
        return this;
    }

    public LabeledIntegerRangeSlider increase() {
        return increase(1);
    }
    public LabeledIntegerRangeSlider increase(int step) {
        IntegerRangeSliderValue value = slider.getValue();
        if (value.end() + step <= slider.getMax()) {
            slider.setValue(new IntegerRangeSliderValue(value.start(), value.end() + step));
        }
        return this;
    }

    public LabeledIntegerRangeSlider startLabelFunction(Function<Integer, String> v) {
        this.startLabelFunction = v;
        return this;
    }
    public LabeledIntegerRangeSlider endLabelFunction(Function<Integer, String> v) {
        this.endLabelFunction = v;
        return this;
    }

    private void setLabelTexts() {
        startLabel.setText(this.startLabelFunction.apply(slider.getValue().start()));
        endLabel.setText(this.endLabelFunction.apply(slider.getValue().end()));
    }

    public LabeledIntegerRangeSlider minMax(int min, int max) {
        slider.setMin(min);
        slider.setMax(max);
        setLabelTexts();
        return this;
    }

    public LabeledIntegerRangeSlider startEnd(int start, int end) {
        slider.setValue(new IntegerRangeSliderValue(start, end));
        setLabelTexts();
        return this;
    }

    public int start() {
        return slider.getValue().start();
    }
    public int end() {
        return slider.getValue().end();
    }

    public LabeledIntegerRangeSlider errorMessage(String v) {
        setErrorMessage(v);
        return this;
    }

    public LabeledIntegerRangeSlider invalid(boolean v) {
        setInvalid(v);
        return this;
    }

    public LabeledIntegerRangeSlider id(String v) {
        setId(v);
        return this;
    }
}
