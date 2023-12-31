package kz.alash.naklei.web.screens.visit;

import com.haulmont.cuba.core.global.DatatypeFormatter;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("naklei_CalendarNavigators")
public class CalendarNavigators {

    @Inject
    protected UserSessionSource userSessionSource;

    public CalendarNavigation forMode(
            CalendarScreenAdjustment screenAdjustment,
            DatatypeFormatter datatypeFormatter,
            ECalendarMode mode
    ) {
        switch (mode) {
            case DAY: return new DayCalendarNavigation(screenAdjustment, datatypeFormatter);
            case WEEK: return new WeekCalendarNavigation(screenAdjustment, userSessionSource.getLocale());
            case MONTH: return new MonthCalendarNavigation(screenAdjustment, userSessionSource.getLocale());
        }

        throw new IllegalStateException("Calendar Mode has to be set");
    }
}
