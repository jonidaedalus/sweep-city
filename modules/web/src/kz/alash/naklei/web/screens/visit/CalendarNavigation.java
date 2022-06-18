package kz.alash.naklei.web.screens.visit;

import java.time.LocalDate;

public interface CalendarNavigation {
    void navigate(ECalendarNavigationMode navigationMode, LocalDate referenceDate);
}
