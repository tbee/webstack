package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import org.tbee.webstack.vdn.component.mixin.ComponentMixin;
import org.tbee.webstack.vdn.component.mixin.SizeMixin;
import org.tbee.webstack.vdn.component.mixin.StyleMixin;
import org.tbee.webstack.vdn.tag.table.Table;
import org.tbee.webstack.vdn.tag.table.TableDataCell;
import org.tbee.webstack.vdn.tag.table.TableRow;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

@StyleSheet("context://org/tbee/webstack/vdn/component/year-month-calendar.css")
public class YearMonthCalendar extends Div
implements ComponentMixin<YearMonthCalendar>, SizeMixin<YearMonthCalendar>, StyleMixin<YearMonthCalendar> {
    public static final String DATA_VALUE = "data-value";
    private final String CSSPREFIX = "year-month-calendar";
    private final String CSSHASDATE = CSSPREFIX + "-has-date";
    private final int MAXWEEKS = 6;

    private final Div title = new org.tbee.webstack.vdn.component.html.Div().classNames(CSSPREFIX + "-title");
    private final TableDataCell[] headerCells = new TableDataCell[7];
    private final TableDataCell[][] bodyCells = new TableDataCell[MAXWEEKS][7];

    private List<String> weekdayShortNames;
    private DayOfWeek firstDayOfWeek;
    private List<String> monthNames;

    private BiConsumer<LocalDate, Set<String>> determineClassnamesBiConsumer = (localdate, classnames) -> {};
    private BiConsumer<LocalDate, Map<String, String>> determineStylesBiConsumer = (localdate, styles) -> {};

    private Consumer<LocalDate> onClickConsumer = ld -> {};
    private BiConsumer<LocalDate, Consumer<LocalDate>> onClickBiConsumer = (ld, consumer) -> {};

    private final List<String> DEFAULTCLASSNAMES = List.of(CSSPREFIX + "-cell", CSSPREFIX + "-date-cell");

    public YearMonthCalendar() {
        // Defaults
        locale(Locale.getDefault());

        addClassName(CSSPREFIX);

        Table table = new Table();
        TableRow headRow = table.getHead().addRow();
        for (int weekday = 0; weekday < headerCells.length; weekday++) {
            TableDataCell cell = headerCells[weekday] = headRow.addDataCell();
            cell.add("*");
            cell.addClassName(CSSPREFIX + "-cell");
            cell.addClassName(CSSPREFIX + "-header-cell");
        }

        for (int week = 0; week < MAXWEEKS; week++) {
            TableRow row = table.getBody().addRow();
            for (int weekday = 0; weekday < 7; weekday++) {
                TableDataCell cell = bodyCells[week][weekday] = row.addDataCell();
                cell.add("-");

                cell.getElement().setAttribute("part", CSSPREFIX + "-date-cell");
                DEFAULTCLASSNAMES.forEach(cell::addClassName);
                cell.getElement().addEventListener("click", e -> {
                    String dataValue = cell.getElement().getAttribute(DATA_VALUE);
                    if (dataValue == null) {
                        return;
                    }
                    LocalDate localDate = LocalDate.parse(dataValue);
                    onClickConsumer.accept(localDate);
                    onClickBiConsumer.accept(localDate, this::refresh);
                    refresh(cell, localDate);
                });
            }
        }

        add(title, table);
    }

    /// Usage:
    /// ```java
    ///     .determineClassnames((localDate, classnames) -> {
    ///         classnames.add(localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY ? "year-month-calendar-fadeback" : null);
    ///         classnames.add(selectedDates.contains(localDate) ? "year-month-calendar-selected" : null);
    ///         classnames.add(today.equals(localDate) ? "year-month-calendar-today" : null);
    ///     })
    /// ```
    public YearMonthCalendar determineClassnames(BiConsumer<LocalDate, Set<String>> consumer) {
        this.determineClassnamesBiConsumer = consumer;
        return this;
    }

    public YearMonthCalendar determineStyles(BiConsumer<LocalDate, Map<String, String>> consumer) {
        this.determineStylesBiConsumer = consumer;
        return this;
    }

    public YearMonthCalendar onClick(Consumer<LocalDate> onClick) {
        this.onClickConsumer = onClick;
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
    public YearMonthCalendar onClick(BiConsumer<LocalDate, Consumer<LocalDate>> onClick) {
        this.onClickBiConsumer = onClick;
        return this;
    }

    /// Use this method to setup the weeknames and firstDayOfWeek based on a locale
    public YearMonthCalendar locale(Locale locale) {
        DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale);
        weekdayShortNames = Stream.of(dateFormatSymbols.getShortWeekdays()).skip(1).toList();
        firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
        monthNames = List.of(dateFormatSymbols.getMonths());
        return this;
    }

    public YearMonthCalendar weekdayShortNames(List<String> weekdayShortNames) {
        this.weekdayShortNames = weekdayShortNames;
        return this;
    }

    public YearMonthCalendar firstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
        return this;
    }

    public YearMonthCalendar monthNames(List<String> monthNames) {
        this.monthNames = monthNames;
        return this;
    }

    public YearMonthCalendar display(YearMonth yearMonth) {
        getElement().setAttribute(DATA_VALUE, yearMonth.toString());

        title.setText(monthNames.get(yearMonth.getMonthValue() - 1) + " " + yearMonth.getYear());

        // Set the weekdays
        for (int weekday = 0; weekday < headerCells.length; weekday++) {
            headerCells[weekday].removeAll();
            headerCells[weekday].add(weekdayShortNames.get((firstDayOfWeek.getValue() + weekday) % 7));
        }

        // Set the dates
        LocalDate startDate = determineStartDate(yearMonth);
        for (int week = 0; week < MAXWEEKS; week++) {
            for (int weekday = 0; weekday < 7; weekday++) {
                LocalDate localDate = startDate.plusDays((week * 7) + weekday);

                TableDataCell cell = bodyCells[week][weekday];
                cell.removeAll();
                if (localDate.getYear() == yearMonth.getYear() && localDate.getMonthValue() == yearMonth.getMonthValue()) {
                    cell.getElement().setAttribute(DATA_VALUE, localDate.toString());
                    cell.add("" + localDate.getDayOfMonth());
                    cell.addClassName(CSSHASDATE);
                    refresh(cell, localDate);
                }
                else {
                    cell.removeClassName(CSSHASDATE);
                    cell.getElement().removeAttribute(DATA_VALUE);
                }
            }
        }
        return this;
    }

    private LocalDate determineStartDate(YearMonth yearMonth) {
        LocalDate firstOfMonth = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
        return firstOfMonth.with(TemporalAdjusters.previousOrSame(firstDayOfWeek));
    }

    private void refresh(TableDataCell cell, LocalDate localDate) {
        // Remove all but the default classnames
        HashSet<String> toRemoveClassnames = new HashSet<>(cell.getClassNames());
        toRemoveClassnames.removeAll(DEFAULTCLASSNAMES);
        toRemoveClassnames.remove(CSSHASDATE);
        toRemoveClassnames.forEach(cell::removeClassName);

        // Set the to-be-added classnames
        Set<String> toAddClassnames = new HashSet<>();
        determineClassnamesBiConsumer.accept(localDate, toAddClassnames);
        toAddClassnames.remove(null);
        toAddClassnames.forEach(cell::addClassName);

        // Set styles
        Map<String, String> styles = new HashMap<>();
        determineStylesBiConsumer.accept(localDate, styles);
        cell.getStyle().clear();
        styles.entrySet().stream()
                .filter(e -> e.getKey() != null)
                .forEach(e -> cell.getStyle().set(e.getKey(), e.getValue()));
    }

    public void refresh(LocalDate localDate) {
        // Does the date fall into our month?
        YearMonth yearMonth = YearMonth.parse(getElement().getAttribute(DATA_VALUE));
        if (yearMonth.getYear() != localDate.getYear() || yearMonth.getMonth() != localDate.getMonth()) {
            return;
        }

        // Find the correct cell
        LocalDate startDate = determineStartDate(yearMonth);
        int days = (int)startDate.until(localDate, ChronoUnit.DAYS);
        int week = days / 7;
        int weekday = (days - (week * 7));
        TableDataCell cell = bodyCells[week][weekday];

        // If no date, skip
        String dataValue = cell.getElement().getAttribute(DATA_VALUE);
        if (dataValue == null) {
            return;
        }

        // Refresh
        LocalDate cellLocalDate = LocalDate.parse(dataValue);
        if (cellLocalDate.equals(localDate)) {
            refresh(cell, localDate);
        }
    }
}
