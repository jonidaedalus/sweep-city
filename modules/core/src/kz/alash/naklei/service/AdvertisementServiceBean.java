package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.*;
import kz.alash.naklei.entity.dict.DPointCost;
import kz.alash.naklei.entity.dict.DStickerType;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.entity.dict.EAdvStatus;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.visit.Visit;
import kz.alash.naklei.service.esb.dto.GenericResponse;
import kz.alash.naklei.service.esb.dto.advertisement.AdvertisementDriverDto;
import kz.alash.naklei.service.esb.dto.driver.SetAdvertisementResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(AdvertisementService.NAME)
public class AdvertisementServiceBean implements AdvertisementService {

    private static final int DAYS_PERIOD_OF_CAR_WASH = 14;
    private static final int MONEY_FOR_CAR_WASH = 500;
    private static final Logger log = LoggerFactory.getLogger(AdvertisementServiceBean.class);
    @Inject
    private DataManager dataManager;
    @Inject
    private Authentication authentication;
    @Inject
    private DriverService driverService;

    @Override
    public SetAdvertisementResponse setAdvertisement(UUID purposeId) {
        //todo Проверить работоспособность метода
        long start = System.currentTimeMillis();

        SetAdvertisementResponse response = new SetAdvertisementResponse();
        SetAdvertisementResponse.Result result = new SetAdvertisementResponse.Result();
        response.setResult(result);

        User user = authentication.begin().getUser();

        try {
            //puposes
            AdvPurpose purpose = dataManager.load(AdvPurpose.class)
                    .id(purposeId)
                    .one();

            Driver driver = driverService.getDriverByUserId(user.getId())
                    .viewProperties("advertisementDrivers", "advertisementDrivers.status", "advertisementDrivers.driver", "car", "car.number", "advertisementDrivers.routes")
                    .one();

//            AdvertisementDriver advertisementDriver = driver.getAdvertisementDrivers().stream()
//                    .filter(ad -> ad.getAdvertisement().equals(advertisement)
//                                && ad.getDriver().equals(driver)
//                                && !ad.getStatus().equals(EAdvDriverStatus.REJECTED))
//                    .findFirst()
//                    .orElse(null);
            AdvertisementDriver advertisementDriver = null;

            if (advertisementDriver == null){
                advertisementDriver = dataManager.create(AdvertisementDriver.class);
                advertisementDriver.setDriver(driver);
                advertisementDriver.setStatus(EAdvDriverStatus.NEW);
                advertisementDriver.setPurpose(purpose);
                dataManager.commit(advertisementDriver);
            }

            response.setCode("0");

            response.getResult().setCarNumber(driver.getCar().getNumber());
            response.getResult().setAdvertisementID(purpose.getId().toString());
            response.getResult().setStatus(advertisementDriver.getStatus().name());
        } catch (Exception e) {
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
    public GenericResponse<AdvertisementDriverDto> terminationOfCurrentAdvContract() {
        long start = System.currentTimeMillis();
        GenericResponse<AdvertisementDriverDto> response = new GenericResponse<>();

        User user = authentication.begin().getUser();
        try {
            AdvertisementDriverDto result = new AdvertisementDriverDto();

            result.setName(user.getName());

            Driver driver = driverService.getDriverByUserId(user.getId())
                    .viewProperties("user", "advertisementDrivers", "advertisementDrivers.endDate", "advertisementDrivers.status", "currentMoney", "withdrawnMoney")
                    .one();

            AdvertisementDriver advDriver = getCurrentAdvertisementDriver(driver);
            advDriver.setStatus(EAdvDriverStatus.PRETERM_LEAVE);

            if (advDriver == null) {
                response.setCode("-2");
                response.setMessage("Не найдена текущая рекл. кампания");
                response.setResult(null);
            } else {
                advDriver.setStatus(EAdvDriverStatus.FINISHED);
                advDriver.setEndDate(new Date());
//                driver.setWithdrawnMoney(BigDecimal.ZERO);
                deleteVisits(advDriver);
                dataManager.commit(new CommitContext(advDriver, driver));
                response.setCode("0");
                response.setMessage("Успешно покинули рекл. кампанию");
                //result.setDateString();
            }

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

    @Override
    public void deleteVisits(AdvertisementDriver advertisementDriver) {
        List<Visit> visitsToRemove = dataManager.load(Visit.class)
                .query("select v from naklei_Visit v where v.advertisementDriver = :advertisementDriver")
                .parameter("advertisementDriver", advertisementDriver)
                .list();
        visitsToRemove.forEach(visit -> dataManager.remove(visit));
    }

    //CRON JOB
    @Override
    public void changeAdvertisementStatus() {
        //todo оптимизировать
        List<Advertisement> list = dataManager.load(Advertisement.class)
                .query("select a from naklei_Advertisement a where a.status = :status and a.startDate = CURRENT_DATE")
                .parameter("status", EAdvStatus.PENDING)
                .viewProperties("status")
                .list();
        list.forEach(advertisement -> advertisement.setStatus(EAdvStatus.ACTIVE));
        CommitContext context = new CommitContext(list);
        dataManager.commit(context);
    }

    @Override
    public AdvertisementDriver getAdvertisementDriver(UUID userId, UUID purposeId) {
        return dataManager.load(AdvertisementDriver.class)
                .query("select e from naklei_AdvertisementDriver e " +
                        "where e.driver.user.id = :userId  " +
                        "and e.endDate is null " +
                        "and e.purpose.id = :purposeId")
                .parameter("userId", userId)
                .parameter("purposeId", purposeId)
                .viewProperties("purpose", "purpose.pastingTime","driver", "driver.user","driver.user.city", "initialTachometerPhoto", "finalTachometerPhoto")
                .optional().orElse(null);
    }

    //todo STATEFUL SERVICE
    @Override
    public Long calculateMaxCarAmount(
            BigDecimal budget,
            DClass carClass,
            DStickerType stickerType,
            BigDecimal pastingCost,
            BigDecimal rewardAmount,
            long numberOfDaysBetweenStartAndEnd
    ) throws Exception {

        DPointCost thePointCost = dataManager.load(DPointCost.class).view("dPointCost-view").list().stream()
                .filter(pointCost -> pointCost.getCarClass().equals(carClass) && pointCost.getStickerType().equals(stickerType))
                .findFirst()
                .orElse(null);

        DAppConf appConfig = dataManager.load(DAppConf.class).query("select e from naklei_DAppConf e")
                .viewProperties("transactionPercent")
                .firstResult(0)
                .optional()
                .orElse(null);

        if (appConfig == null || appConfig.getTransactionPercent() == null)
            throw new Exception("Бизнес конфигурации не заполнены");

        if (thePointCost == null) {
//            notifications
//                    .create()
//                    .withType(Notifications.NotificationType.ERROR)
//                    .withDescription("Не найдена цена за поинт с классом " +
//                            carClass.getName() +
//                            " и типом наклейки " + stickerType.getName())
//                    .show();
            return null;
        }

        //вычислим стоимость пути максимального поинта каждый день в заданный период(стоимость для рекламодателя)
        long maxCostOfMaxPointForEveryDay = carClass.getMaxPointPerDay() * numberOfDaysBetweenStartAndEnd * thePointCost.getAdvertiserCost().longValue();

        //TODO DAYS_PERIOD_OF_CAR_WASH и MONEY_FOR_CAR_WASH Изменить на справочник(или конфиги), узнать у Тимы. Или же будет создан отновсительно рекл. камп
        long costForWash = numberOfDaysBetweenStartAndEnd/DAYS_PERIOD_OF_CAR_WASH * MONEY_FOR_CAR_WASH;

        long denominator = (long) (maxCostOfMaxPointForEveryDay + maxCostOfMaxPointForEveryDay * appConfig.getTransactionPercent() + pastingCost.longValue() + costForWash + costForWash*appConfig.getTransactionPercent());

        if(rewardAmount != null) {
            denominator += rewardAmount.longValue() + rewardAmount.longValue()*appConfig.getTransactionPercent();
        }


        Long carAmount = budget.longValue() / denominator;
        //Убрать после тестирования
        log.info("carAmount = " + carAmount);
        log.info("budget = " + budget);
        log.info("maxCostOfMaxPointForEveryDay = " + maxCostOfMaxPointForEveryDay);
        log.info("pastingCost = " + pastingCost);
        log.info("costForWash = " + costForWash);
        log.info("rewardAmount = " + rewardAmount);

        return carAmount;
    }

    @Override
    public AdvertisementDriver getCurrentAdvertisementDriver(Driver driver) {
        return driver.getAdvertisementDrivers()
                .stream().filter(ad -> ad.getEndDate() == null)
                .findFirst()
                .orElse(null);
    }

    public CommitContext addMoneyToAdvDriver(
            AdvertisementDriver advertisementDriver,
            Driver driver,
            BigDecimal toAdd) {

        driver.setCurrentMoney(driver.getCurrentMoney().add(toAdd));
        driver.setEarnedMoney(driver.getEarnedMoney().add(toAdd));

        advertisementDriver.setCurrentMoney(advertisementDriver.getCurrentMoney().add(toAdd));
        advertisementDriver.setEarnedMoney(advertisementDriver.getEarnedMoney().add(toAdd));

        return new CommitContext(driver, advertisementDriver);
    }
}