package kz.alash.naklei.service.esb;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.*;
import kz.alash.naklei.entity.dict.DPointCost;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.moderation.Moderation;
import kz.alash.naklei.entity.visit.Visit;
import kz.alash.naklei.service.DriverService;
import kz.alash.naklei.service.esb.dto.UpdateFcmTokenResponse;
import kz.alash.naklei.service.esb.dto.advertisement.GetHistoryAdvertisementResponse;
import kz.alash.naklei.service.esb.dto.moderation.ModerationDto;
import kz.alash.naklei.service.esb.dto.profile.ProfileDataResponse;
import kz.alash.naklei.service.utils.NumberUtility;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service(EsbUserService.NAME)
public class EsbUserServiceBean implements EsbUserService {

    private static final Logger log = LoggerFactory.getLogger(EsbUserServiceBean.class);

    public static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    public static final DateFormat dateFormatWithTime = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    @Inject
    private Authentication authentication;
    @Inject
    private DataManager dataManager;
    @Inject
    private FileStorageAPI fileStorageAPI;
    @Inject
    private DriverService driverService;

    @Override
    public UpdateFcmTokenResponse updateUserToken(String token) {
        long start = System.currentTimeMillis();
        UpdateFcmTokenResponse response = new UpdateFcmTokenResponse();

        User user = authentication.begin().getUser();
        try {
            UpdateFcmTokenResponse.Result result = new UpdateFcmTokenResponse.Result();

            ExtUser extUser = dataManager.load(ExtUser.class)
                    .query("select c from naklei_ExtUser c where c.id = :userId")
                    .parameter("userId", user.getId())
                    .viewProperties("fcmToken")
                    .one();

            extUser.setFcmToken(token);
            response.setCode("0");
            response.setResult(result);
            dataManager.commit(extUser);
        } catch (Exception e) {
            response.setCode("-1");
            response.setMessage("Ошибка при получении обновлении токена. Текст ошибки: " + e.getMessage());
            response.setStackTrace(Arrays.toString(e.getStackTrace()));
            log.error("Error", e);
        } finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);

        return response;
    }

    @Override
    public GetHistoryAdvertisementResponse getHistoryAdvertisements() {
        long start = System.currentTimeMillis();
        GetHistoryAdvertisementResponse response = new GetHistoryAdvertisementResponse();

        User user = authentication.begin().getUser();

        try {
            Driver driver = driverService.getDriverByUserId(user.getId())
                    .viewProperties(
                            "car",
                            "car.carClass",
                            "car.carClass.code",
                            "car.carClass.name",
                            "advertisementDrivers",
                            "advertisementDrivers.purpose",
                            "advertisementDrivers.purpose.carClass",
                            "advertisementDrivers.purpose.carClass.code",
                            "advertisementDrivers.purpose.stickerType",
                            "advertisementDrivers.purpose.stickerType",
                            "advertisementDrivers.purpose.stickerType.name",
                            "advertisementDrivers.purpose.advertisement",
                            "advertisementDrivers.purpose.advertisement.advertiser",
                            "advertisementDrivers.purpose.advertisement.advertiser.fullName",
                            "advertisementDrivers.purpose.advertisement.advertiser.desc",
                            "advertisementDrivers.purpose.advertisement.startDate",
                            "advertisementDrivers.purpose.advertisement.endDate",
                            "advertisementDrivers.routes",
                            "advertisementDrivers.routes.date",
                            "advertisementDrivers.routes.points",
                            "advertisementDrivers.routes.distance",
                            "advertisementDrivers.routes.time",
                            "advertisementDrivers.endDate",
                            "advertisementDrivers.status",
                            "creditCardName",
                            "creditCardNumber"
                    )
                    .one();

            response.setCode("0");
            List<DPointCost> pointCosts = getPointCosts();
            response.setResult(driver.getAdvertisementDrivers().stream()
                    .filter(advertisementDriver -> !isCurrent(advertisementDriver))
                    .map(advertisementDriver ->
                            getCurrentAdvertisement(advertisementDriver, driver.getCar().getCarClass(), getPointCosts())
                    )
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            response.setCode("-1");
            response.setMessage("Ошибка при получении данных. Текст ошибки: " + e.getMessage());
            response.setStackTrace(Arrays.toString(e.getStackTrace()));
            log.error("Error", e);
        } finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);

        return response;
    }

    @Override
    public ProfileDataResponse getProfileData() {
        long start = System.currentTimeMillis();
        ProfileDataResponse response = new ProfileDataResponse();

        User user = authentication.begin().getUser();
        try {
            ProfileDataResponse.Result result = new ProfileDataResponse.Result();

            result.setFullName(user.getName());
            result.setPhoneNumber(user.getLogin());

            //нужно отрефакторить, слишком много джоинов
            Driver driver = driverService.getDriverByUserId(user.getId())
                    .viewProperties(
                            "user",
                            "currentMoney",
                            "withdrawnMoney",
                            "car",
                            "user.city",
                            "user.city.name",
                            "user.city.code",
                            "driverStatus",
                            "car.number",
                            "car.model",
                            "car.model.code",
                            "car.model.name",
                            "car.carClass",
                            "car.carClass.code",
                            "car.carClass.name",
                            "car.photos",
                            "car.photos.id",
                            "car.photos.createDate",
                            "car.photos.extension",
                            "advertisementDrivers",
                            "advertisementDrivers.purpose",
                            "advertisementDrivers.purpose.carClass",
                            "advertisementDrivers.purpose.carClass.code",
                            "advertisementDrivers.purpose.stickerType",
                            "advertisementDrivers.purpose.stickerType.name",
                            "advertisementDrivers.purpose.pastingDate",
                            "advertisementDrivers.purpose.advertisement",
                            "advertisementDrivers.purpose.advertisement.advertiser",
                            "advertisementDrivers.purpose.advertisement.advertiser.fullName",
                            "advertisementDrivers.purpose.advertisement.advertiser.desc",
                            "advertisementDrivers.purpose.advertisement.startDate",
                            "advertisementDrivers.purpose.advertisement.endDate",
                            "advertisementDrivers.routes",
                            "advertisementDrivers.routes.date",
                            "advertisementDrivers.routes.points",
                            "advertisementDrivers.routes.distance",
                            "advertisementDrivers.routes.time",
                            "advertisementDrivers.endDate",
                            "advertisementDrivers.status",
                            "creditCardName",
                            "creditCardNumber"
                    )
                    .one();

            result.setCityCode(driver.getUser().getCity().getCode());
            result.setCityName(driver.getUser().getCity().getName());
            //todo create balance for driver
            List<PayOut> payOuts = dataManager.load(PayOut.class)
                    .query("select e from naklei_PayOut e where e.status <> :status and e.advertisementDriver =:advertisementDriver")
                    .parameter("status", "Выплачено")
                    .parameter("advertisementDriver", driver.getAdvertisementDrivers().stream().filter(this::isCurrent).findFirst().orElse(null))
                    .viewProperties("sum")
                    .list();
            result.setOutBalance(
                    payOuts.size() > 0
                            ? payOuts.stream().map(PayOut::getSum).reduce(BigDecimal.ZERO, BigDecimal::add)
                            : BigDecimal.ZERO
            );
            result.setCurrentBalance(driver.getCurrentMoney());

            //карточку
            result.setCreditCardName(driver.getCreditCardName());
            result.setCreditCardNumber(driver.getCreditCardNumber());

            if (driver.getCar() != null) {
                ProfileDataResponse.CarDto car = new ProfileDataResponse.CarDto();
                car.setNumber(driver.getCar().getNumber());
                car.setClassCode(driver.getCar().getCarClass().getCode());
                car.setClassName(driver.getCar().getCarClass().getName());
                car.setModelCode(driver.getCar().getModel().getCode());
                car.setModelName(driver.getCar().getModel().getName());
                //Передаем первое фото в авто водителя
                if (driver.getCar().getPhotos().size() > 0) {
                    FileDescriptor firstPhoto = driver.getCar().getPhotos().get(0);
                    try {
                        car.setPhoto(Base64.getEncoder().encodeToString(fileStorageAPI.loadFile(firstPhoto)));
                    } catch (Exception e) {
                        log.error("Error", e);
                    }
                }

                result.setCar(car);

                //Текущий водитель рекл. кампании
                AdvertisementDriver currentAdvDriver = driver.getAdvertisementDrivers().stream().filter(this::isCurrent).findFirst().orElse(null);
                //Проверяем наличиие рекл/кампаний
                if (currentAdvDriver != null) {
                    ProfileDataResponse.AdvertisementDto advertisement = getCurrentAdvertisement(currentAdvDriver, driver.getCar().getCarClass(), getPointCosts());
                    result.setCurrentAdvertisement(advertisement);
                }
            }

            List<Route> allRoutes = driver.getAdvertisementDrivers()
                    .stream()
                    .flatMap(advertisementDriver -> advertisementDriver.getRoutes().stream())
                    .collect(Collectors.toList());

            if (allRoutes.size() > 0) {
                result.setAllPoints((int) allRoutes.stream().mapToDouble(Route::getPoints).sum());
                result.setAllDistance((int) allRoutes.stream().mapToDouble(Route::getDistance).sum());
                result.setAllTime((int) allRoutes.stream().filter(route -> route.getTime() != null).mapToDouble(Route::getTime).sum());
            } else {
                result.setAllPoints(0);
                result.setAllDistance(0);
                result.setAllTime(0);
            }

            response.setCode("0");
            response.setResult(result);
        } catch (Exception e) {
            response.setCode("-1");
            response.setMessage("Ошибка при получении данных для profile. Текст ошибки: " + e.getMessage());
            response.setStackTrace(Arrays.toString(e.getStackTrace()));
            log.error("Error", e);
        } finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);

        return response;
    }

    private ProfileDataResponse.AdvertisementDto getCurrentAdvertisement(AdvertisementDriver currentAdvDriver, DClass carClass, List<DPointCost> pointCosts) {

        ProfileDataResponse.AdvertisementDto advertisementDto = new ProfileDataResponse.AdvertisementDto();
        advertisementDto.setStatus(currentAdvDriver.getStatus().getId());
        //we set purpose car class here, the car class of the driver's car is already sent on higher layer
        advertisementDto.setCarClass(currentAdvDriver.getPurpose().getCarClass().getCode());

        List<Visit> visits = dataManager.load(Visit.class)
                .query("select e from naklei_Visit e " +
                        "where e.type = 'STICK' " +
                        "and e.advertisementDriver =:advDriver")
                .parameter("advDriver", currentAdvDriver)
                .viewProperties(
                        "status",
                        "type",
                        "cancelReason",
                        "comment",
                        "visitStart",
                        "visitEnd",
                        "createTs",
                        "company",
                        "company.addressText",
                        "company.mapLink"
                ).list();

        if (visits.size() > 0) {
            Visit visit = visits.stream().max(Comparator.comparing(Visit::getCreateTs)).orElse(null);

            ProfileDataResponse.VisitDto visitDto = new ProfileDataResponse.VisitDto();
            visitDto.setStatus(visit.getStatus().getId());
            visitDto.setCompanyAddress(visit.getCompany().getAddressText());
            visitDto.setCompanyAddressMapUrl(visit.getCompany().getMapLink());
            visitDto.setEndDate(visit.getVisitEnd());
            visitDto.setStartDate(visit.getVisitStart());
            visitDto.setType(visit.getType().getId());
            visitDto.setCancelReason(visit.getCancelReason() != null ? visit.getCancelReason().getId() : null);
            visitDto.setComment(visit.getComment());
            advertisementDto.setVisit(visitDto);
        }
        advertisementDto.setPastingDate(
                currentAdvDriver.getPurpose().getPastingDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
        );

        advertisementDto.setName(currentAdvDriver.getPurpose().getAdvertisement().getAdvertiser().getFullName());
        advertisementDto.setActivityName(currentAdvDriver.getPurpose().getAdvertisement().getAdvertiser().getDesc());
        advertisementDto.setStickerTypeName(currentAdvDriver.getPurpose().getStickerType().getName());
        advertisementDto
                .setDateString(dateFormat.format(currentAdvDriver.getPurpose().getAdvertisement().getStartDate())
                        + " "
                        + dateFormat.format(currentAdvDriver.getPurpose().getAdvertisement().getEndDate()));

        Date today = new Date();
        List<Route> routesForCalcAverageData = currentAdvDriver.getRoutes().stream()
                .filter(route -> DateUtils.isSameDay(today, route.getDate()))
                .collect(Collectors.toList());

        double averagePoints = 0;
        double averageDistance = 0;
        double averageTime = 0;
        int totalPoints = 0;
        double totalDistance = 0;
        double totalTime = 0;

        if (routesForCalcAverageData.size() > 0) {
            double pointsSum = routesForCalcAverageData.stream().mapToDouble(Route::getPoints).sum();
            advertisementDto.setTodayPoints(pointsSum);
            averagePoints = pointsSum / routesForCalcAverageData.size();
            double distanceSum = routesForCalcAverageData.stream().mapToDouble(Route::getDistance).sum();
            advertisementDto.setTodayDistance(distanceSum);
            averageDistance = distanceSum / routesForCalcAverageData.size();
            double timeSum = routesForCalcAverageData.stream().mapToDouble(Route::getTime).sum();
            averageTime = (timeSum / routesForCalcAverageData.size());
        }

        totalPoints = (int) currentAdvDriver.getRoutes().stream().mapToDouble(Route::getPoints).sum();
        totalDistance = currentAdvDriver.getRoutes().stream().mapToDouble(Route::getDistance).sum();
        totalTime = routesForCalcAverageData.stream().mapToDouble(Route::getTime).sum();

        advertisementDto.setAveragePoints(averagePoints);
        advertisementDto.setAverageDistance(averageDistance);
        advertisementDto.setAverageTimes(NumberUtility.formatDouble(averageTime));
        advertisementDto.setTotalPoints(totalPoints);
        advertisementDto.setTotalDistance(totalDistance);
        advertisementDto.setTotalTimes(NumberUtility.formatDouble(totalTime));

        advertisementDto.setAdvertiserPointCost(
                pointCosts.stream().filter(dPointCost ->
                        dPointCost.getCarClass().getCode().equals(carClass.getCode())).findFirst().get().getAdvertiserCost().intValue()
        );
//        //Проверить наличие ожидающих визитов на оклейку в текущей рекл.кампании
//        List<Visit> visits = dataManager.load(Visit.class)
//                .query("select e from naklei_Visit e " +
//                        "where e.type = 'STICK' " +
//                        "and e.advertisementDriver =:advDriver")
//                .parameter("advDriver", currentAdvDriver)
//                .viewProperties("status", "type", "cancelReason", "comment", "visitStart", "visitEnd").list();
//
        List<Moderation> moderations = dataManager.load(Moderation.class)
                .query("select m from naklei_Moderation m " +
                        "where m.advertisementDriver.id = :id ")
//                        "where m.advertisementDriver.id = :id " +
//                        "and m.status = :status")
                .parameter("id", currentAdvDriver.getId())
//                .parameter("status", EModerationStatus.IN_WORK)
                .viewProperties(
                        "type",
                        "status",
                        "message",
                        "reason"
                )
                .list();
        moderations.forEach(moderation -> {
            ModerationDto dto = new ModerationDto();
            dto.setModerationId(String.valueOf(moderation.getId()));
            dto.setModerationType(String.valueOf(moderation.getType()));
            dto.setModerationStatus(String.valueOf(moderation.getStatus()));
            dto.setMessage(moderation.getMessage());
            dto.setReason(moderation.getReason());
            advertisementDto.getModerations().add(dto);
        });

        advertisementDto.setPurposeId(String.valueOf(currentAdvDriver.getPurpose().getId()));
        advertisementDto.setAdvertiserId(String.valueOf(currentAdvDriver.getPurpose().getAdvertisement().getAdvertiser().getId()));

        return advertisementDto;
    }

    private boolean isCurrent(AdvertisementDriver advertisementDriver) {
        if (advertisementDriver.getStatus().equals(EAdvDriverStatus.REJECTED) || advertisementDriver.getStatus().equals(EAdvDriverStatus.FINISHED))
            return false;
        return true;
    }

    private List<DPointCost> getPointCosts() {
        return dataManager.load(DPointCost.class).view("dPointCost-view").list();
    }


}