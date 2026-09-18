package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@StyleSheet("context://org/tbee/webstack/vdn/component/year-month-range-calendar.css")
public class YearMonthRangeCalendar extends FormLayout
implements ComponentMixin<YearMonthRangeCalendar>, SizeMixin<YearMonthRangeCalendar>, StyleMixin<YearMonthRangeCalendar> {
    private final String CSSPREFIX = "year-month-range-calendar";

    private final YearMonthCalendar[] yearMonthCalendars;
    private final int length;

    public YearMonthRangeCalendar(int length) {
        addClassName(CSSPREFIX);

        this.length = length;
        this.yearMonthCalendars = new YearMonthCalendar[length];

        setResponsiveSteps(IntStream.range(0, length).mapToObj(i -> new FormLayout.ResponsiveStep((i * 20) + "em", i + 1)).toList());
        for (int i = 0; i < length; i++) {
            yearMonthCalendars[i] = new YearMonthCalendar();
            add(yearMonthCalendars[i]);
        }

        // Defaults
        locale(Locale.getDefault());
    }

    /// Use this method to setup the monthNames based on a locale
    public YearMonthRangeCalendar locale(Locale locale) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.locale(locale));
        return this;
    }

    public YearMonthRangeCalendar monthNames(List<String> monthNames) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.monthNames(monthNames));
        return this;
    }

    public YearMonthRangeCalendar weekdayShortNames(List<String> list) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.weekdayShortNames(list));
        return this;
    }

    public YearMonthRangeCalendar firstDayOfWeek(DayOfWeek dayOfWeek) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.firstDayOfWeek(dayOfWeek));
        return this;
    }

    /// Usage:
    /// ```java
    ///     .determineClassnames((localDate, classnames) -> {
    ///         classnames.add(localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY ? "year-month-calendar-fadeback" : null);
    ///         classnames.add(selectedDates.contains(localDate) ? "year-month-calendar-selected" : null);
    ///         classnames.add(today.equals(localDate) ? "year-month-calendar-today" : null);
    ///     })
    /// ```
    public YearMonthRangeCalendar determineClassnames(BiConsumer<LocalDate, Set<String>> consumer) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.determineClassnames(consumer));
        return this;
    }

    public YearMonthRangeCalendar determineStyles(BiConsumer<LocalDate, Map<String, String>> consumer) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.determineStyles(consumer));
        return this;
    }

    public YearMonthRangeCalendar onClick(Consumer<LocalDate> onClick) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.onClick(onClick));
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
    public YearMonthRangeCalendar onClick(BiConsumer<LocalDate, Consumer<LocalDate>> onClick) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.onClick(ld -> onClick.accept(ld, this::refresh)));
        return this;
    }

    public void refresh(LocalDate localDate) {
        Arrays.stream(yearMonthCalendars).forEach(ymc -> ymc.refresh(localDate));
    }

    public YearMonthRangeCalendar display(YearMonth yearMonth) {
        LocalDate start = yearMonth.atDay(1);
        for (int i = 0; i < length; i++) {
            LocalDate localDate = start.plusMonths(i);
            yearMonthCalendars[i].display(YearMonth.of(localDate.getYear(), localDate.getMonthValue()));
        }
        return this;
    }
}
