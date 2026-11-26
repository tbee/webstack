package org.tbee.webstack.vdn.component.slider;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.slider.IntegerRangeSlider;
import org.junit.jupiter.api.Test;
import org.tbee.webstack.vdn.component.html.Span;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LabeledIntegerRangeSliderTest {

    // --- layout of the labels ----------------------------------------------------------------------------------

    @Test
    void splitLabelsDefaultsToFalse() {
        LabeledIntegerRangeSlider field = new LabeledIntegerRangeSlider("Range");
        assertFalse(field.splitLabels());
        assertFalse(content(field).getClassNames().contains("labeledIntegerRangeSliderSplitLabels"));
        assertEquals(List.of("labeledIntegerRangeSliderStartEnd", "labeledIntegerRangeSliderSlider"), classMarkers(content(field)));
    }

    @Test
    void byDefaultBothLabelsShareTheRowAboveTheSlider() {
        HorizontalLayout labelsRow = assertInstanceOf(HorizontalLayout.class, firstChild(content(new LabeledIntegerRangeSlider("Range"))));
        assertEquals(List.of("labeledIntegerRangeSliderStartLabel", "labeledIntegerRangeSliderEndLabel"), classMarkers(labelsRow));
    }

    @Test
    void splitLabelsPutsStartAboveAndEndBelowTheSlider() {
        LabeledIntegerRangeSlider field = new LabeledIntegerRangeSlider("Range");
        field.splitLabels(true);
        assertTrue(field.splitLabels());
        assertTrue(content(field).getClassNames().contains("labeledIntegerRangeSliderSplitLabels"));
        assertEquals(List.of("labeledIntegerRangeSliderStartLabel", "labeledIntegerRangeSliderSlider", "labeledIntegerRangeSliderEndLabel"), classMarkers(content(field)));
    }

    @Test
    void splitLabelsFalseRestoresTheRow() {
        LabeledIntegerRangeSlider field = new LabeledIntegerRangeSlider("Range");
        field.splitLabels(true);
        field.splitLabels(false);
        assertFalse(content(field).getClassNames().contains("labeledIntegerRangeSliderSplitLabels"));
        assertEquals(List.of("labeledIntegerRangeSliderStartEnd", "labeledIntegerRangeSliderSlider"), classMarkers(content(field)));
        HorizontalLayout labelsRow = assertInstanceOf(HorizontalLayout.class, firstChild(content(field)));
        assertEquals(List.of("labeledIntegerRangeSliderStartLabel", "labeledIntegerRangeSliderEndLabel"), classMarkers(labelsRow));
    }

    @Test
    void splitLabelsIsFluent() {
        LabeledIntegerRangeSlider field = new LabeledIntegerRangeSlider("Range");
        assertEquals(field, field.splitLabels(true));
    }

    // --- the field keeps working ---------------------------------------------------------------------------------

    @Test
    void splittingKeepsTheValue() {
        LabeledIntegerRangeSlider field = new LabeledIntegerRangeSlider("Range");
        field.minMax(0, 100).startEnd(10, 20);
        field.splitLabels(true);
        assertEquals(10, field.start());
        assertEquals(20, field.end());
        assertEquals(new LabeledIntegerRangeSlider.Range(10, 20), field.getValue());
    }

    @Test
    void splittingKeepsTheLabelTextsAndTheClicks() {
        LabeledIntegerRangeSlider field = new LabeledIntegerRangeSlider("Range", i -> "low " + i, i -> "high " + i);
        field.minMax(0, 100).startEnd(10, 20);
        field.splitLabels(true);
        assertEquals(List.of("low 10", "high 20"), labelTexts(field));

        field.decrease().increase(2);
        assertEquals(9, field.start());
        assertEquals(22, field.end());
        assertEquals(List.of("low 9", "high 22"), labelTexts(field));
    }

    // --- helpers -------------------------------------------------------------------------------------------------

    private static VerticalLayout content(LabeledIntegerRangeSlider field) {
        return assertInstanceOf(VerticalLayout.class, firstChild(field));
    }

    private static Component firstChild(Component parent) {
        return parent.getChildren().findFirst().orElseThrow();
    }

    /** The component class names this component contributes to the layout, in order. */
    private static List<String> classMarkers(Component parent) {
        return parent.getChildren()
                .map(component -> component.getClassNames().stream()
                        .filter(name -> name.startsWith("labeledIntegerRangeSlider"))
                        .findFirst()
                        .orElse(component instanceof IntegerRangeSlider ? "<slider>" : "<none>"))
                .toList();
    }

    private static List<String> labelTexts(LabeledIntegerRangeSlider field) {
        return content(field).getChildren()
                .filter(Span.class::isInstance)
                .map(component -> ((Span) component).getText())
                .toList();
    }
}
