package kz.alash.naklei.web.screens.visit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import com.vaadin.v7.shared.ui.calendar.CalendarState;
import kz.alash.naklei.entity.ExtUser;
import kz.alash.naklei.entity.dict.EVisitType;
import kz.alash.naklei.entity.visit.Visit;
import kz.alash.naklei.entity.visit.VisitCompany;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static kz.alash.naklei.web.screens.visit.ECalendarNavigationMode.*;
import static kz.alash.naklei.web.screens.visit.RelativeDates.startOfWeek;

@UiController("naklei_Visit.browse")
@UiDescriptor("visit-browse.xml")
@LookupComponent("visitsTable")
@LoadDataBeforeShow
@Route("visits")
public class VisitBrowse extends StandardLookup<Visit> {
    private ExtUser assignedUser;
    @Inject
    protected Calendar<LocalDateTime> calendar;
    @Inject
    protected CollectionLoader<Visit> visitsCalendarDl;
    @Inject
    protected ScreenBuilders screenBuilders;
    @Inject
    protected CollectionContainer<Visit> visitsCalendarDc;
    @Inject
    protected CollectionLoader<Visit> visitsDl;
    @Inject
    protected DataContext dataContext;
    @Inject
    protected CheckBoxGroup<EVisitType> typeMultiFilter;
    @Inject
    protected RadioButtonGroup<ECalendarMode> calendarMode;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected Label<String> calendarTitle;
    @Inject
    protected CalendarNavigators calendarNavigators;
    @Inject
    protected DatePicker<LocalDate> calendarNavigator;
    @Inject
    protected UserSessionSource userSessionSource;
    @Inject
    protected DatatypeFormatter datatypeFormatter;
    @Inject
    protected Notifications notifications;
    @Inject
    private DataManager dataManager;
//    @Inject
//    protected MessageBundle messageBundle;
//    @Inject
//    protected Messages messages;

    @Subscribe
    protected void onInit(InitEvent event) {
        initTypeFilter();
        initSortCalendarEventsInMonthlyView();
        assignedUser = (ExtUser) userSessionSource.getUserSession().getUser();
        initUser();
        initCompanyData();
    }

    private void initCompanyData() {
//        VisitCompany company = dataManager.load(VisitCompany.class)
//                .query("select e from naklei_VisitCompany e where e.employee = :user")
//                .parameter("user", assignedUser)
//                .view("visitCompany-view")
//                .one();
        calendar.setFirstVisibleHourOfDay(7);
        calendar.setLastVisibleHourOfDay(21);
    }

    private void initUser() {
        visitsDl.setParameter("user", assignedUser);
    }

    private void initTypeFilter() {
        typeMultiFilter.setOptionsEnum(EVisitType.class);
        typeMultiFilter.setValue(EnumSet.allOf(EVisitType.class));
        typeMultiFilter.setOptionIconProvider(o -> EVisitTypeIcon.valueOf(o.getIcon()).source());
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        current(ECalendarMode.WEEK);
    }

    @SuppressWarnings("deprecation")
    private void initSortCalendarEventsInMonthlyView() {
        calendar.unwrap(com.vaadin.v7.ui.Calendar.class)
                .setEventSortOrder(CalendarState.EventSortOrder.START_DATE_DESC);
    }


    @Subscribe("calendar")
    protected void onCalendarCalendarDayClick(Calendar.CalendarDateClickEvent<LocalDateTime> event) {
        atDate(ECalendarMode.DAY,
                event.getDate().toLocalDate()
        );
    }

    @Subscribe("calendar")
    protected void onCalendarCalendarWeekClick(Calendar.CalendarWeekClickEvent<LocalDateTime> event) {
        atDate(ECalendarMode.WEEK,
                startOfWeek(
                        event.getYear(),
                        event.getWeek(),
                        userSessionSource.getLocale()
                )
        );
    }

    @Subscribe("navigatorPrevious")
    protected void onNavigatorPreviousClick(Button.ClickEvent event) {
        previous(calendarMode.getValue());
    }

    @Subscribe("navigatorNext")
    protected void onNavigatorNextClick(Button.ClickEvent event) {
        next(calendarMode.getValue());
    }

    @Subscribe("navigatorCurrent")
    protected void onNavigatorCurrentClick(Button.ClickEvent event) {
        current(calendarMode.getValue());
    }

    @Subscribe("calendarNavigator")
    protected void onCalendarRangePickerValueChange(HasValue.ValueChangeEvent<LocalDate> event) {
        if (event.isUserOriginated()) {
            atDate(calendarMode.getValue(), event.getValue());
        }
    }

    private void current(ECalendarMode calendarMode) {
        change(calendarMode, AT_DATE, timeSource.now().toLocalDate());
    }

    private void atDate(ECalendarMode calendarMode, LocalDate date) {
        change(calendarMode, AT_DATE, date);
    }

    private void next(ECalendarMode calendarMode) {
        change(calendarMode, NEXT, calendarNavigator.getValue());
    }

    private void previous(ECalendarMode calendarMode) {
        change(calendarMode, PREVIOUS, calendarNavigator.getValue());
    }

    private void change(ECalendarMode calendarMode, ECalendarNavigationMode navigationMode, LocalDate referenceDate) {
        this.calendarMode.setValue(calendarMode);

        calendarNavigators
                .forMode(
                        CalendarScreenAdjustment.of(calendar, calendarNavigator, calendarTitle),
                        datatypeFormatter,
                        calendarMode)
                .navigate(navigationMode, referenceDate);

        loadEvents();
    }


    @Subscribe("calendarMode")
    protected void onCalendarRangeValueChange(HasValue.ValueChangeEvent event) {
        if (event.isUserOriginated()) {
            atDate((ECalendarMode) event.getValue(), calendarNavigator.getValue());
        }
    }

    private void loadEvents() {
        visitsCalendarDl.setParameter("visitStart", calendar.getStartDate());
        visitsCalendarDl.setParameter("visitEnd", calendar.getEndDate());
        visitsCalendarDl.setParameter("user", assignedUser);
        visitsCalendarDl.load();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Calendar Visit Event Click
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @Subscribe("calendar")
//    protected void onCalendarCalendarDayClick(Calendar.CalendarDayClickEvent<LocalDateTime> event) {
//        Screen visitEditor = screenBuilders.editor(Visit.class, this)
//                .newEntity()
//                .withInitializer(visit -> {
//                    visit.setVisitStart(event.getDate());
//                    visit.setVisitEnd(event.getDate().plusHours(1));
//                })
//                .withOpenMode(OpenMode.DIALOG)
//                .build();
//
//        visitEditor.addAfterCloseListener(afterCloseEvent -> {
//            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
//                getScreenData().loadAll();
//            }
//        });
//
//        visitEditor.show();
//    }

    @Subscribe("calendar")
    protected void onCalendarCalendarEventClick(Calendar.CalendarEventClickEvent<LocalDateTime> event) {

        Screen visitEditor = screenBuilders.editor(Visit.class, this)
                .editEntity((Visit) event.getEntity())
                .withOpenMode(OpenMode.DIALOG)
                .build();

        visitEditor.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                getScreenData().loadAll();
            }
        });

        visitEditor.show();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Filter for Visit Types
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Subscribe("typeMultiFilter")
    protected void onTypeMultiFilterValueChange(HasValue.ValueChangeEvent event) {

        if (event.getValue() == null) {
            visitsCalendarDl.removeParameter("type");
        } else if (CollectionUtils.isEmpty((Set<EVisitType>) event.getValue())) {
            visitsCalendarDl.setParameter("type", Collections.singleton(""));
        } else {
            visitsCalendarDl.setParameter("type", event.getValue());
        }
        loadEvents();
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Visit Changes through Calendar Event Adjustments
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @Subscribe("calendar")
//    protected void onCalendarCalendarEventResize(Calendar.CalendarEventResizeEvent<LocalDateTime> event) {
//        updateVisit(event.getEntity(), event.getNewStart(), event.getNewEnd());
//    }

//    @Subscribe("calendar")
//    protected void onCalendarCalendarEventMove(Calendar.CalendarEventMoveEvent<LocalDateTime> event) {
//        updateVisit(event.getEntity(), event.getNewStart(), event.getNewEnd());
//    }

//    private void updateVisit(Entity entity, LocalDateTime newStart, LocalDateTime newEnd) {
//        Visit visit = (Visit) entity;
//        visit.setVisitStart(newStart);
//        visit.setVisitEnd(newEnd);
//        dataContext.commit();
//        notifications.create(Notifications.NotificationType.TRAY)
//                .withCaption(
//                        messageBundle.formatMessage(
//                                "visitUpdated",
//                                messages.getMessage(visit.getType()),
//                                visit.getDriverName()
//                        )
//                )
//                .show();
//    }
}