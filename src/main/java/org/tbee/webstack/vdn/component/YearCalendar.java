package org.tbee.webstack.vdn.component;

import org.tbee.webstack.vdn.component.html.Div;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class YearCalendar extends Div {

    private final YearMonthRangeCalendar yearMonthRangeCalendar = new YearMonthRangeCalendar(12);

    public YearCalendar() {
        add(yearMonthRangeCalendar);
    }


    /// Use this method to setup the weeknames, firstDayOfWeek, ... based on a locale
    public YearCalendar locale(Locale locale) {
        yearMonthRangeCalendar.locale(locale);
        return this;
    }

    public YearCalendar monthNames(List<String> monthNames) {
        yearMonthRangeCalendar.monthNames(monthNames);
        return this;
    }

    public YearCalendar weekdayShortNames(List<String> list) {
        yearMonthRangeCalendar.weekdayShortNames(list);
        return this;
    }

    public YearCalendar firstDayOfWeek(DayOfWeek dayOfWeek) {
        yearMonthRangeCalendar.firstDayOfWeek(dayOfWeek);
        return this;
    }

    public YearCalendar determineClassnames(BiConsumer<LocalDate, Set<String>> consumer) {
        yearMonthRangeCalendar.determineClassnames(consumer);
        return this;
    }

    public YearCalendar determineStyles(BiConsumer<LocalDate, Map<String, String>> consumer) {
        yearMonthRangeCalendar.determineStyles(consumer);
        return this;
    }

    public YearCalendar onClick(Consumer<LocalDate> onClick) {
        yearMonthRangeCalendar.onClick(onClick);
        return this;
    }

    /// Onclick automatically refreshes the date that was clicked.
    /// If another date also needs refreshing, this onclick can be used.
    ///
    /// Use as:
    /// ```java
    ///     .onClick((localDate, refresh) -> {
    ///         refresh.accept(someOtherLocalDate);
    ///     }
    /// ```
    public YearCalendar onClick(BiConsumer<LocalDate, Consumer<LocalDate>> onClick) {
        yearMonthRangeCalendar.onClick(onClick);
        return this;
    }

    public YearCalendar display(int year) {
        yearMonthRangeCalendar.display(YearMonth.of(year, Month.JANUARY));
        return this;
    }

    public void refresh(LocalDate localDate) {
        yearMonthRangeCalendar.refresh(localDate);
    }
}
