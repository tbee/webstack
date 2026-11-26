package org.tbee.webstack.vdn.component;

import org.junit.jupiter.api.Test;
import org.tbee.webstack.vdn.component.IntegerRangeCheckboxes.Orientation;
import org.tbee.webstack.vdn.component.IntegerRangeCheckboxes.Range;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegerRangeCheckboxesTest {

    // --- labels ------------------------------------------------------------------------------------------------

    @Test
    void labelsForStep1() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 1);
        assertEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"), labels(field));
    }

    @Test
    void labelsForStep2() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 2);
        assertEquals(List.of("0 - 1", "2 - 3", "4 - 5", "6 - 7", "8 - 9", "10"), labels(field));
    }

    @Test
    void labelsForStep3() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        assertEquals(List.of("0 - 2", "3 - 5", "6 - 8", "9 - 10"), labels(field));
    }

    @Test
    void labelsForStep5() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 3, 13, 5);
        assertEquals(List.of("3 - 7", "8 - 12", "13"), labels(field));
    }

    @Test
    void labelsForStep5NotClamped() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 3, 17, 5);
        assertEquals(List.of("3 - 7", "8 - 12", "13 - 17"), labels(field));
    }

    // --- initial value -----------------------------------------------------------------------------------------

    @Test
    void initialSelectionIsFull() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        assertEquals(new Range(0, 10), field.getValue());
        assertEquals(0, field.start());
        assertEquals(10, field.end());
        field.checkboxes().forEach(checkbox -> assertTrue(checkbox.getValue()));
    }

    // --- clicking ----------------------------------------------------------------------------------------------

    @Test
    void clickLowerHalfDeselectsLower() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.click(1);
        assertEquals(new Range(6, 10), field.getValue());
        assertEquals(List.of(false, false, true, true), selection(field));
    }

    @Test
    void clickUpperHalfDeselectsUpper() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.click(2);
        assertEquals(new Range(0, 5), field.getValue());
        assertEquals(List.of(true, true, false, false), selection(field));
    }

    @Test
    void clickMiddleOfOddSelectionGoesLower() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 12, 3);
        field.click(2);
        assertEquals(new Range(9, 12), field.getValue());
        assertEquals(List.of(false, false, false, true, true), selection(field));
    }

    @Test
    void clickLowerHalfOfSubSelectionDeselectsLower() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(3, 8);
        assertEquals(new Range(3, 8), field.getValue());
        field.click(1);
        assertEquals(new Range(6, 8), field.getValue());
        assertEquals(List.of(false, false, true, false), selection(field));
    }

    @Test
    void clickUpperHalfOfSubSelectionDeselectsUpper() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(3, 8);
        assertEquals(new Range(3, 8), field.getValue());
        field.click(2);
        assertEquals(new Range(3, 5), field.getValue());
        assertEquals(List.of(false, true, false, false), selection(field));
    }

    @Test
    void clickDeselectedBelowExpands() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(6, 10);
        assertEquals(new Range(6, 10), field.getValue());
        field.click(0);
        assertEquals(new Range(0, 10), field.getValue());
        assertEquals(List.of(true, true, true, true), selection(field));
    }

    @Test
    void clickDeselectedAboveExpands() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(0, 5);
        assertEquals(new Range(0, 5), field.getValue());
        field.click(3);
        assertEquals(new Range(0, 10), field.getValue());
        assertEquals(List.of(true, true, true, true), selection(field));
    }

    @Test
    void minimumOneSelected() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(9, 10);
        assertEquals(new Range(9, 10), field.getValue());
        field.click(3);
        assertEquals(new Range(9, 10), field.getValue());
        assertEquals(List.of(false, false, false, true), selection(field));
    }

    @Test
    void minimumOneSelectedDoesNotFireEvent() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(9, 10);
        List<Range> events = new ArrayList<>();
        field.addValueChangeListener(event -> events.add(event.getValue()));
        field.click(3);
        assertTrue(events.isEmpty());
    }

    // --- setting the value -------------------------------------------------------------------------------------

    @Test
    void startEndNormalizesToIntervals() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(4, 7);
        assertEquals(new Range(3, 8), field.getValue());
        assertEquals(List.of(false, true, true, false), selection(field));
    }

    @Test
    void startEndOrdersReversedRange() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(7, 3);
        assertEquals(new Range(3, 8), field.getValue());
    }

    @Test
    void setValueClampsOutOfRange() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.setValue(new Range(20, 30));
        assertEquals(new Range(9, 10), field.getValue());
        assertEquals(List.of(false, false, false, true), selection(field));
    }

    @Test
    void setValueNullSelectsAll() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(0, 5);
        field.setValue(null);
        assertEquals(new Range(0, 10), field.getValue());
        field.checkboxes().forEach(checkbox -> assertTrue(checkbox.getValue()));
    }

    @Test
    void clearSelectsAll() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(0, 5);
        field.clear();
        assertEquals(new Range(0, 10), field.getValue());
        field.checkboxes().forEach(checkbox -> assertTrue(checkbox.getValue()));
    }

    // --- changing the total range ------------------------------------------------------------------------------

    @Test
    void minMaxKeepsRangeClamped() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.minMax(0, 20);
        assertEquals(new Range(0, 11), field.getValue());
        assertEquals(List.of(true, true, true, true, false, false, false), selection(field));
        assertEquals(List.of("0 - 2", "3 - 5", "6 - 8", "9 - 11", "12 - 14", "15 - 17", "18 - 20"), labels(field));
    }

    @Test
    void stepKeepsRange() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.startEnd(0, 5);
        field.step(2);
        assertEquals(new Range(0, 5), field.getValue());
        assertEquals(List.of(true, true, true, false, false, false), selection(field));
        assertEquals(List.of("0 - 1", "2 - 3", "4 - 5", "6 - 7", "8 - 9", "10"), labels(field));
    }

    @Test
    void minAndMaxSettersDelegate() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.min(2).max(20);
        assertEquals(2, field.min());
        assertEquals(20, field.max());
        assertEquals(new Range(2, 10), field.getValue());
    }

    @Test
    void invalidRangeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new IntegerRangeCheckboxes("Range", 10, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new IntegerRangeCheckboxes("Range", 0, 10, 0));
    }

    // --- label generator ---------------------------------------------------------------------------------------

    @Test
    void customLabelGenerator() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.labelGenerator(value -> "v" + value);
        assertEquals(List.of("v0", "v3", "v6", "v9"), labels(field));
        assertEquals("v0", field.labelGenerator().apply(0));
    }

    @Test
    void nullLabelGeneratorRestoresDefault() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.labelGenerator(value -> "v" + value);
        field.labelGenerator(null);
        assertEquals(List.of("0 - 2", "3 - 5", "6 - 8", "9 - 10"), labels(field));
    }

    // --- orientation -------------------------------------------------------------------------------------------

    @Test
    void orientationDefaultsToVertical() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        assertEquals(Orientation.VERTICAL, field.orientation());
        assertTrue(field.container().getClassNames().contains("integerRangeCheckboxesVertical"));
        assertFalse(field.container().getClassNames().contains("integerRangeCheckboxesHorizontal"));
    }

    @Test
    void orientationHorizontal() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.orientation(Orientation.HORIZONTAL);
        assertEquals(Orientation.HORIZONTAL, field.orientation());
        assertTrue(field.container().getClassNames().contains("integerRangeCheckboxesHorizontal"));
        assertFalse(field.container().getClassNames().contains("integerRangeCheckboxesVertical"));
    }

    @Test
    void nullOrientationMeansVertical() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.orientation(Orientation.HORIZONTAL);
        field.orientation(null);
        assertEquals(Orientation.VERTICAL, field.orientation());
        assertTrue(field.container().getClassNames().contains("integerRangeCheckboxesVertical"));
    }

    // --- state propagation -------------------------------------------------------------------------------------

    @Test
    void readOnlyPropagatesToCheckboxes() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.setReadOnly(true);
        field.checkboxes().forEach(checkbox -> assertTrue(checkbox.isReadOnly()));
        field.setReadOnly(false);
        field.checkboxes().forEach(checkbox -> assertFalse(checkbox.isReadOnly()));
    }

    @Test
    void enabledPropagatesToCheckboxes() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        field.setEnabled(false);
        assertFalse(field.enabled());
        field.checkboxes().forEach(checkbox -> assertFalse(checkbox.isEnabled()));
        field.setEnabled(true);
        assertTrue(field.enabled());
        field.checkboxes().forEach(checkbox -> assertTrue(checkbox.isEnabled()));
    }

    @Test
    void valueChangeEventFiresOnClick() {
        IntegerRangeCheckboxes field = new IntegerRangeCheckboxes("Range", 0, 10, 3);
        List<Range> events = new ArrayList<>();
        field.addValueChangeListener(event -> events.add(event.getValue()));
        field.click(1);
        assertEquals(List.of(new Range(6, 10)), events);
    }

    // --- helpers -----------------------------------------------------------------------------------------------

    private static List<String> labels(IntegerRangeCheckboxes field) {
        return field.checkboxes().stream().map(Checkbox::getLabel).toList();
    }

    private static List<Boolean> selection(IntegerRangeCheckboxes field) {
        return field.checkboxes().stream().map(Checkbox::getValue).toList();
    }
}
