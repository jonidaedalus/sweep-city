package kz.alash.naklei.service.esb;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.dict.EVisitStatus;
import kz.alash.naklei.entity.dict.EVisitType;
import kz.alash.naklei.entity.visit.Visit;
import kz.alash.naklei.entity.visit.VisitCompany;
import kz.alash.naklei.helper.DurationUnit;
import kz.alash.naklei.service.AdvertisementService;
import kz.alash.naklei.service.DriverService;
import kz.alash.naklei.service.esb.dto.visit.*;
import kz.alash.naklei.service.utils.DateUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.*;
import java.util.*;

@Service(EsbVisitService.NAME)
public class EsbVisitServiceBean implements EsbVisitService {

    private static final Logger log = LoggerFactory.getLogger(EsbVisitServiceBean.class);
    @Inject
    private DataManager dataManager;
    @Inject
    private Authentication authentication;
    @Inject
    private AdvertisementService advertisementService;
    @Inject
    private DriverService driverService;

    @Override
    public CreateVisitResponse createVisit(CreateVisitRequest request) {
        long start = System.currentTimeMillis();

        CreateVisitResponse response = new CreateVisitResponse();
        User user = authentication.begin().getUser();

        AdvertisementDriver advertisementDriver;

        try {
            Driver driver = driverService.getDriverByUserId(user.getId())
                    .viewProperties("user",
                            "user.city",
                            "advertisementDrivers",
                            "advertisementDrivers.purpose",
                            "advertisementDrivers.purpose.pastingTime",
                            "advertisementDrivers.endDate")
                    .one();

            advertisementDriver = advertisementService.getCurrentAdvertisementDriver(driver);

            VisitCompany company = dataManager.load(VisitCompany.class)
                    .query("select e from naklei_VisitCompany e where e.city = :city")
                    .parameter("city", driver.getUser().getCity())
                    .viewProperties("employee")
                    .one();

            Visit newVisit = dataManager.create(Visit.class);
            newVisit.setAdvertisementDriver(advertisementDriver);
            newVisit.setAssignedUser(company.getEmployee());
            newVisit.setCompany(company);
            newVisit.setStatus(EVisitStatus.PENDING);

            StringBuilder descr = new StringBuilder();
            descr.append("Запись на оклейку. ");

            LocalDateTime visitStart = request.getVisitStart().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime visitEnd;

            if(advertisementDriver == null)
                throw new Exception("Не найден водитель рекл. кампании");

            Integer pastingTime = advertisementDriver.getPurpose().getPastingTime();
            //Если в рекламной компании не указали кол-во минут на оклейку,тогда по умолчанию ставлю 60 минут(1 час)
            if (pastingTime == null)
                pastingTime = 60;

            visitEnd = visitStart.plusMinutes(pastingTime);
            descr.append("Дата и время начала: ")
                    .append(visitStart);

            newVisit.setType(EVisitType.STICK);
            newVisit.setVisitStart(visitStart);
            newVisit.setVisitEnd(visitEnd);
            newVisit.setDescription(descr.toString());

            dataManager.commit(newVisit);
            response.setCode("0");
            response.setMessage(descr.toString());
        }catch (Exception e){
            response.setCode("-1");
            response.setMessage(e.getMessage());
            response.setStackTrace(Arrays.toString(e.getStackTrace()));
            log.error("Error", e);
        }finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);

        return response;
    }

    @Override
    public FreeTimeSlotResponse getVisitFreeTime(String visitType, @JsonFormat(pattern = "yyyy-MM-dd") Date day, String cityCode, int visitLength) {

        User user = authentication.begin().getUser();

        LocalDate localDate = DateUtility.convertToLocalDateViaSqlDate(day);
        LocalDate currentLocalDate = DateUtility.convertToLocalDateViaSqlDate(new Date());

        Driver driver = driverService.getDriverByUserId(user.getId())
                .viewProperties(
                        "advertisementDrivers",
                        "advertisementDrivers.purpose",
                        "advertisementDrivers.purpose.pastingTime",
                        "advertisementDrivers.endDate"
                )
                .one();

        AdvertisementDriver advertisementDriver = advertisementService.getCurrentAdvertisementDriver(driver);
        if (advertisementDriver != null)
            visitLength = advertisementDriver.getPurpose().getPastingTime();

        //В зависимости от типа визита находим информации о компании которая это делает. По городу и типу
        if (visitType.equals(EVisitType.STICK.getId())){
            //Получить время работы оклейв/компании в зависимости от дня недели, учитывая праздники
            VisitCompany company = dataManager.load(VisitCompany.class)
                .query("select e from naklei_VisitCompany e where e.city.code = :cityCode")
                .parameter("cityCode", cityCode)
                .view("visitCompany-view")
                .one();

            DayOfWeek dayOfWeek = localDate.getDayOfWeek();

            LocalTime workFrom = getWorkFromByDayOfWeek(company, dayOfWeek);
            if (workFrom == null) {
                return new FreeTimeSlotResponse();
            }

            LocalTime currentTime = LocalTime.now();
            //check if workFrom and currentTime are the same day
            if (localDate.equals(currentLocalDate) && currentTime.isAfter(workFrom))
                workFrom = DateUtility.getNearestHourThird(currentTime);

            LocalTime workTo = getWorkToByDayOfWeek(company, dayOfWeek);

            //создаем boolean массив где все изначально false, что значит по этим тайм слотам никто не записывался
            int size = calculateTimeIntervals(workFrom, workTo, visitLength);
            boolean[] visited = new boolean[size];

            //находим визиты которые существуют
            List<Visit> existingVisits = dataManager.load(Visit.class)
                    .query("select e from naklei_Visit e " +
                            "where e.assignedUser = :user " +
                            "and e.visitStart >= :visitStart " +
                            "and e.visitEnd <= :visitEnd")
                    .parameter("user", company.getEmployee())
                    .parameter("visitStart", localDate.atTime(workTo))
                    .parameter("visitEnd", localDate.atTime(workFrom))
                    .view("visit-view")
                    .list();

            //тайм слоты существующих визитов отмечаем true
            int finalVisitLength = visitLength;
            existingVisits.forEach(visit -> {
                LocalDateTime now = LocalDateTime.now();

                LocalDateTime start = now.isAfter(visit.getVisitStart())
                        ? now.truncatedTo(DurationUnit.ofMinutes(10))
                        : visit.getVisitStart();
                LocalDateTime end = visit.getVisitEnd();


                if (now.isAfter(visit.getVisitStart())) {

                }
                // 9 20 -> 9 50
                //[9:00->9:20, 9:20->9:40, 9:40->10:00]
                // [false      true        true]
                int timeSlotTo  = (localDateTimeToMinutes(start)
                        - localDateTimeToMinutes(visit.getVisitStart())) / finalVisitLength;
                int timeSlotFrom  = (localDateTimeToMinutes(LocalDateTime.from(end))
                        - localDateTimeToMinutes(visit.getVisitStart())) / finalVisitLength;
                visited[timeSlotTo] = true;
                visited[timeSlotFrom] = true;
            });

            FreeTimeSlotResponse response = new FreeTimeSlotResponse();
            List<TimeSlotVisit> visitList = new ArrayList<>();

            //переводим все обработанные данные в json
            for (int i = 0; i < size; i++) {
                TimeSlotVisit responseVisit = new TimeSlotVisit();
                int startMinutes = i * visitLength + workFrom.getHour() * 60 + workFrom.getMinute();
                int endMinutes = startMinutes + visitLength;
                responseVisit.setStartTime(minutesToTime(startMinutes));
                responseVisit.setEndTime(minutesToTime(endMinutes));
                responseVisit.setFree(!visited[i]);
                visitList.add(responseVisit);
            }

            response.setVisits(visitList);
            return response;
        }
        return null;
    }

    @Override
    public VisitCompaniesResponse visitCompanies(String cityCode, String visitType) {
        VisitCompaniesResponse response = new VisitCompaniesResponse();
        List<VisitCompanyDto> companies = new ArrayList<>();
        response.setResult(companies);

        try {
            List<VisitCompany> visitCompanies = dataManager.load(VisitCompany.class)
                    .query("select v from naklei_VisitCompany v where v.city.code = :cityCode and v.visitType = :visitType")
                    .parameter("cityCode", cityCode)
                    .parameter("visitType", visitType)
                    .viewProperties(
                            "name",
                            "addressText",
                            "mapLink",
                            "workFrom",
                            "workTo",
                            "satWorkFrom",
                            "satWorkTo",
                            "sunWorkFrom",
                            "sunWorkTo"
                    )
                    .list();

            visitCompanies.forEach(company -> {
                VisitCompanyDto visitCompanyDto = new VisitCompanyDto();
                visitCompanyDto.setName(company.getName());
                visitCompanyDto.setAddress(company.getAddressText());
                visitCompanyDto.setMapUrlLink(company.getMapLink());

                visitCompanyDto.setHoursFrom(String.valueOf(company.getWorkFrom()));
                visitCompanyDto.setHoursTo(String.valueOf(company.getWorkTo()));

                visitCompanyDto.setSatHoursFrom(String.valueOf(company.getSatWorkFrom()));
                visitCompanyDto.setSatHoursTo(String.valueOf(company.getSatWorkTo()));

                visitCompanyDto.setSunHoursFrom(String.valueOf(company.getSunWorkFrom()));
                visitCompanyDto.setSunHoursTo(String.valueOf(company.getSunWorkTo()));
                companies.add(visitCompanyDto);
            });

        } catch (Exception e) {
            log.error("EsbVisitServiecBean/visitCompanies : " + e);
        }
        return response;
    }

    private LocalTime getWorkToByDayOfWeek(VisitCompany company, DayOfWeek dayOfWeek) {
        if (dayOfWeek.equals(DayOfWeek.SATURDAY))
            return company.getSatWorkTo();
        else if (dayOfWeek.equals(DayOfWeek.SUNDAY))
            return company.getSunWorkTo();
        else
            return company.getWorkTo();
    }

    private LocalTime getWorkFromByDayOfWeek(VisitCompany company, DayOfWeek dayOfWeek) {
        if (dayOfWeek.equals(DayOfWeek.SATURDAY))
            return company.getSatWorkFrom();
        else if (dayOfWeek.equals(DayOfWeek.SUNDAY))
            return company.getSunWorkFrom();
        else
            return company.getWorkFrom();
    }


    private int calculateTimeIntervals(LocalTime workFrom, LocalTime workTo, int visitLength) {
        /*
            09:15 -> 17:33
            09:00 -> 18:00

            09:15 -> 11:00
            09:00 -> 11:38

            9 * 60  + 15 = 540 + 15 = 555
            11 * 60 = 660
            (660 - 555) / 20 = 115 / 20 = 5;
         */
        int to = workTo.getHour() * 60 + workTo.getMinute();
        int from = workFrom.getHour() * 60 + workFrom.getMinute();
        return (to - from) / visitLength;
    }

    private int localDateTimeToMinutes(LocalDateTime localDateTime) {
        return localDateTime.getHour() * 60 + localDateTime.getMinute();
    }

    private String minutesToTime(int minutes) {
        return LocalTime.MIN.
                plus(Duration.ofMinutes(minutes))
                .toString();
    }


}