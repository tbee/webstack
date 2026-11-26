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
 * A slider for a range of two values, with the start and the end shown next to the track, so both are visible at all
 * times (Vaadin's own value bubble only appears while a thumb is dragged). By default both are drawn above the track;
 * {@link #splitLabels(boolean)} moves the end below it, which gives each label the full width of the slider.
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

    private static final String CLASS_START_END = "labeledIntegerRangeSliderStartEnd";
    private static final String CLASS_SPLIT_LABELS = "labeledIntegerRangeSliderSplitLabels";
    private static final String CLASS_START_LABEL = "labeledIntegerRangeSliderStartLabel";
    private static final String CLASS_END_LABEL = "labeledIntegerRangeSliderEndLabel";

    private final IntegerRangeSlider slider = new IntegerRangeSlider(Integer.MIN_VALUE, Integer.MAX_VALUE);
    private final Span startLabel = new Span("Start").classNames("sliderValueBubble", CLASS_START_LABEL).cursorPointer();
    private final Span endLabel = new Span("End").classNames("sliderValueBubble", CLASS_END_LABEL).cursorPointer();
    // start and end on one row above the track; only used when splitLabels is false
    private final HorizontalLayout startEndLabels = new HorizontalLayout(FlexComponent.JustifyContentMode.BETWEEN)
            .widthFull()
            .lean()
            .wrap()
            .classNames(CLASS_START_END);
    private final VerticalLayout contentLayout = new VerticalLayout()
            .widthFull()
            .lean()
            .spacing(false)
            .classNames("labeledIntegerRangeSliderField");
    private boolean splitLabels = false;
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

        layoutLabels();
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

    /**
     * Sets whether the start label is drawn above the track and the end label below it, instead of both above it.
     * False, the default, puts them on one row above the track; splitting gives each label the full width of the
     * slider, so long labels no longer have to fit next to each other.
     */
    public LabeledIntegerRangeSlider splitLabels(boolean v) {
        if (this.splitLabels != v) {
            this.splitLabels = v;
            layoutLabels();
        }
        return this;
    }

    /** Returns whether the start label is drawn above the track and the end label below it. */
    public boolean splitLabels() {
        return splitLabels;
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

    /**
     * Places the labels: both on one row above the track, or the start above and the end below it. The labels are
     * the same instances in both arrangements, so their click listeners keep working.
     */
    private void layoutLabels() {
        contentLayout.removeAll();
        contentLayout.removeClassName(CLASS_SPLIT_LABELS);
        if (splitLabels) {
            // adding a label here moves it out of the row it was in
            contentLayout.add(startLabel, slider, endLabel);
            contentLayout.addClassName(CLASS_SPLIT_LABELS);
        } else {
            startEndLabels.add(startLabel, endLabel);
            contentLayout.add(startEndLabels, slider);
        }
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
