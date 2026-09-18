package org.tbee.webstack.vdn.component;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.tbee.webstack.vdn.component.html.Div;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@StyleSheet("context://org/tbee/webstack/vdn/component/year-month-list.css")
public class YearMonthList extends Div {
    private final String CSSPREFIX = "year-month-list";
    private final int size = 1000000;

    class Pool<Component> {

        record Entry<T>(YearMonth yearMonth, T component) { }
        private List<Entry<Component>> pool = new ArrayList<>();

        int size() {
            return pool.size();
        }

        void forEach(Consumer<Component> consumer) {
            pool.stream().map(entry -> entry.component).forEach(consumer);
        }

        Component remove(YearMonth yearMonth) {
            for (int i = 0; i < pool.size(); i++) {
                if (pool.get(i).yearMonth().equals(yearMonth)) {
                    return pool.remove(i).component();
                }
            }
            return null;
        }

        Component removeOldest() {
            return pool.removeFirst().component();
        }

        void add(YearMonth yearMonth, Component component) {
            pool.add(new Entry<>(yearMonth, component));
        }
        void add(Component component) {
            add(YearMonth.of(size - this.size(), 1), component);
        }
    }

    private final Pool<YearMonthCalendar> calendarPool = new Pool<>();
    private final VirtualList<YearMonth> yearMonthVirtualList = new VirtualList<>();

    private BiConsumer<LocalDate, Set<String>> determineClassnamesBiConsumer = (localdate, classnames) -> {};
    private BiConsumer<LocalDate, Map<String, String>> determineStylesBiConsumer = (localdate, styles) -> {};

    private Consumer<LocalDate> onClickConsumer = ld -> {};
    private BiConsumer<LocalDate, Consumer<LocalDate>> onClickBiConsumer = (ld, consumer) -> {};

    private List<String> weekdayShortNames;
    private DayOfWeek firstDayOfWeek;
    private List<String> monthNames;

    private YearMonth visibleYearMonth;
    private Consumer<YearMonth> scrollListener = (yearMonth) -> {};

    public YearMonthList() {
        locale(Locale.getDefault());

        addClassName(CSSPREFIX);

        // DataProvider
        int middleIndex = size / 2;
        YearMonth middleYearMonth = visibleYearMonth = YearMonth.now();
        DataProvider<YearMonth, Void> dataProvider = DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();

                    // Populate pools
                    while (calendarPool.size() < (limit * 2)) {
                        YearMonthCalendar yearMonthCalendar = new YearMonthCalendar()
                                .weekdayShortNames(weekdayShortNames)
                                .monthNames(monthNames)
                                .firstDayOfWeek(firstDayOfWeek)
                                .determineClassnames(determineClassnamesBiConsumer)
                                .determineStyles(determineStylesBiConsumer)
                                .onClick(onClickConsumer)
                                .onClick(onClickBiConsumer);
                        yearMonthCalendar.classNames(CSSPREFIX + "-calendar");
                        calendarPool.add(yearMonthCalendar);
                    }

                    // Return all YearMonths for this range
                    return IntStream.range(offset, offset + limit).mapToObj(idx -> middleYearMonth.plusMonths(idx - middleIndex));
                },
                // Second callback fetches the total number of items currently in the Grid.
                query -> size);

        // YearMonth
        String calendarId = UUID.randomUUID().toString();
        yearMonthVirtualList.setRenderer(new ComponentRenderer<Component, YearMonth>(ym -> {
            YearMonthCalendar yearMonthCalendar = calendarPool.remove(ym);
            if (yearMonthCalendar == null) {
                yearMonthCalendar = calendarPool.removeOldest();
                yearMonthCalendar.display(ym);
            }
            calendarPool.add(ym, yearMonthCalendar);
            yearMonthCalendar.removeClassNames(CSSPREFIX + "-even", CSSPREFIX + "-odd");
            yearMonthCalendar.addClassName(CSSPREFIX + (ym.getMonthValue() % 2 == 0 ? "-even" : "-odd"));
            return yearMonthCalendar;
        }));
        yearMonthVirtualList.setDataProvider(dataProvider);
        yearMonthVirtualList.setId(calendarId);
        yearMonthVirtualList.scrollToIndex(middleIndex);
        yearMonthVirtualList.setSizeFull();
        yearMonthVirtualList.addScrollListener(event -> {
            int delta = event.visibleIndex() - middleIndex + 1;
            visibleYearMonth = middleYearMonth.plusMonths(delta);
            scrollListener.accept(visibleYearMonth);
        });

        yearMonthVirtualList.getStyle().set("overflow", "hidden");
        add(yearMonthVirtualList);
    }

    /// Usage:
    /// ```java
    ///     .determineClassnames((localDate, classnames) -> {
    ///         classnames.add(localDate.getDayOfWeek() == DayOfWeek.SATURDAY || localDate.getDayOfWeek() == DayOfWeek.SUNDAY ? "year-month-calendar-fadeback" : null);
    ///         classnames.add(selectedDates.contains(localDate) ? "year-month-calendar-selected" : null);
    ///         classnames.add(today.equals(localDate) ? "year-month-calendar-today" : null);
    ///     })
    /// ```
    public YearMonthList determineClassnames(BiConsumer<LocalDate, Set<String>> consumer) {
        this.determineClassnamesBiConsumer = consumer;
        calendarPool.forEach(ymc -> ymc.determineClassnames(determineClassnamesBiConsumer));
        return this;
    }

    public YearMonthList determineStyles(BiConsumer<LocalDate, Map<String, String>> consumer) {
        this.determineStylesBiConsumer = consumer;
        calendarPool.forEach(ymc -> ymc.determineStyles(determineStylesBiConsumer));
        return this;
    }

    public YearMonthList onClick(Consumer<LocalDate> onClick) {
        this.onClickConsumer = onClick;
        calendarPool.forEach(ymc -> ymc.onClick(onClick));
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
    public YearMonthList onClick(BiConsumer<LocalDate, Consumer<LocalDate>> onClick) {
        this.onClickBiConsumer = onClick;
        calendarPool.forEach(ymc -> ymc.onClick(onClick));
        return this;
    }

    /// Use this method to setup the weeknames and firstDayOfWeek based on a locale
    public YearMonthList locale(Locale locale) {
        DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale);
        weekdayShortNames = Stream.of(dateFormatSymbols.getShortWeekdays()).skip(1).toList();
        firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
        monthNames = List.of(dateFormatSymbols.getMonths());
        return this;
    }

    public YearMonthList weekdayShortNames(List<String> weekdayShortNames) {
        this.weekdayShortNames = weekdayShortNames;
        calendarPool.forEach(ymc -> ymc.weekdayShortNames(weekdayShortNames));
        return this;
    }

    public YearMonthList firstDayOfWeek(DayOfWeek firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
        calendarPool.forEach(ymc -> ymc.firstDayOfWeek(firstDayOfWeek));
        return this;
    }

    public YearMonthList monthNames(List<String> monthNames) {
        this.monthNames = monthNames;
        calendarPool.forEach(ymc -> ymc.monthNames(monthNames));
        return this;
    }

    public YearMonthList heightInMonths(double heightInMonths) {
        style("height", (heightInMonths * 21.5) + "em");
        return this;
    }

    public YearMonth visibleYearMonth() {
        return visibleYearMonth;
    }

    public YearMonthList addScrollListener(Consumer<YearMonth> consumer) {
        this.scrollListener = consumer;
        return this;
    }
}
