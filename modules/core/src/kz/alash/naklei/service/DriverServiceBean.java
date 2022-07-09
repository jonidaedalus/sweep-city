package kz.alash.naklei.service;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Car;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.ExtUser;
import kz.alash.naklei.entity.Notification;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.entity.dict.EDriverStatus;
import kz.alash.naklei.entity.dict.EModerationType;
import kz.alash.naklei.entity.dict.car.DModel;
import kz.alash.naklei.service.esb.dto.NotificationResponseDto;
import kz.alash.naklei.service.esb.dto.driver.DriverInfoResponse;
import kz.alash.naklei.service.esb.dto.driver.DriverRegistrationRequest;
import kz.alash.naklei.service.esb.dto.driver.DriverRegistrationResponse;
import kz.alash.naklei.service.esb.dto.driver.DriverUpdateRequest;
import kz.alash.naklei.service.esb.dto.GenericResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

import static kz.alash.naklei.service.esb.EsbUserServiceBean.dateFormatWithTime;

@Service(DriverService.NAME)
public class DriverServiceBean implements DriverService {

    @Inject
    private Authentication authentication;
    @Inject
    private DataManager dataManager;
    @Inject
    private Logger log;
    @Inject
    private RegistrationService registrationService;
    @Inject
    private FileStorageAPI fileStorageAPI;
    @Inject
    private CarService carService;
    @Inject
    private DriverService driverService;

    @Override
    public DriverRegistrationResponse register(DriverRegistrationRequest request) {
        long start = System.currentTimeMillis();

        DriverRegistrationResponse response = new DriverRegistrationResponse();
        try {
            CommitContext context = new CommitContext();
            //TODO обработать валидацию(MethodParametersValidationException) и написать тест на postman
            log.info("Trying to register {}", request.getPhoneNumber());
            ExtUser user = registrationService.registerUser(request.getPhoneNumber(), request.getPassword(), "Driver", "DRIVER",
                    request.getCityCode(), request.getFullName(), request.getAppleID(), context);
            driverRegistration(user, request, context);
            //save everything into database
            dataManager.commit(context);

            log.info("Registered {}", request.getPhoneNumber());

            response.setCode("0");
            response.setMessage("Успешно зарегистрирован");
        } catch (Exception e) {
            response.setCode("-1");
            response.setMessage("Ошибка при регистрации водителя. Текст ошибки: " + e.getMessage());
            response.setStackTrace(Arrays.toString(e.getStackTrace()));
            //response.setTimeElapsedMillis();
            log.error("Error", e);
            //return response;
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);

        return response;
    }

    @Override
    public DriverRegistrationResponse deleteAccount() {
        long start = System.currentTimeMillis();
        ExtUser user = (ExtUser) authentication.begin().getUser();
        DriverRegistrationResponse response = new DriverRegistrationResponse();
        try {
            Driver driver = driverService.getDriverByUserId(user.getId())
                    .view("driver-edit")
                    .one();
            CommitContext context = new CommitContext();
            for (AdvertisementDriver advertisementDriver : driver.getAdvertisementDrivers()) {

                if (EAdvDriverStatus.ACTIVE.equals(advertisementDriver.getStatus())
                        || EAdvDriverStatus.NEW.equals(advertisementDriver.getStatus()) ) {
                    advertisementDriver.setStatus(EAdvDriverStatus.FINISHED);
                    advertisementDriver.setEndDate(new Date());
                    context.addInstanceToCommit(advertisementDriver);
                }
            }
            dataManager.commit(context);
            dataManager.remove(user);
            response.setCode("0");
        } catch (Exception e) {
            response.setCode("1");
            response.setMessage("Error: DriverServiceBean/deleteAccount");
            log.error("Error: DriverServiceBean/deleteAccount");
        } finally {
            authentication.end();
        }
        response.setTimeElapsedMillis(System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public void update(DriverUpdateRequest request) {
        User user = authentication.begin().getUser();
        Driver driver = getDriverByUserId(user.getId())
                .parameter("userId", user.getId())
                .view("driver-edit")
                .one();
    }

    @Override
    public DriverInfoResponse driverInfo() {
        long start = System.currentTimeMillis();

        DriverInfoResponse response = new DriverInfoResponse();
        DriverInfoResponse.Result result = new DriverInfoResponse.Result();
        response.setResult(result);

        User user = authentication.begin().getUser();

        try {

            Driver driver = driverService.getDriverByUserId(user.getId())
                    .view("driver-edit")
                    .one();
            result.setBalance(driver.getWithdrawnMoney());
            result.setCarNumber(driver.getCar().getNumber());
            result.setPhoneNumber(driver.getUser().getPhoneNumber());
            result.setTotalRun(driver.getTotalRun());
            result.setStatus(driver.getDriverStatus().getId());

            response.setCode("0");

        } catch (Exception e) {
            response.setCode("1");
            response.setMessage("Error: DriverServiceBean/driverInfo");
            log.error("Error: DriverServiceBean/driverInfo");
        } finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public GenericResponse<String> setCreditCard(String cardNumber, String cardName) {
        long start = System.currentTimeMillis();
        GenericResponse<String> response = new GenericResponse<>();

        User user = authentication.begin().getUser();

        try {
            Driver driver = getDriverByUserId(user.getId())
                    .viewProperties("creditCardName", "creditCardNumber")
                    .one();
            driver.setCreditCardName(cardName);
            driver.setCreditCardNumber(cardNumber);
            dataManager.commit(driver);
            response.setCode("0");

        } catch (Exception e) {
            response.setCode("1");
            response.setMessage("Error: DriverServiceBean/setCreditCard");
            log.error("Error: DriverServiceBean/setCreditCard");
        } finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);

        return response;
    }

    @Override
    public FluentLoader.ByQuery<Driver, UUID> getDriverByUserId(UUID userId) {
        return dataManager.load(Driver.class)
                .query("select d from naklei_Driver d where d.user.id = :id")
                .parameter("id", userId);
    }

    @Override
    public NotificationResponseDto getNotifications() {
        long start = System.currentTimeMillis();
        NotificationResponseDto response = new NotificationResponseDto();
        List<NotificationResponseDto.Result> results = new ArrayList<>();
        response.setResult(results);

        User user = authentication.begin().getUser();

        try{
            Driver driver = getDriverByUserId(user.getId())
                    .viewProperties("creditCardName", "creditCardNumber")
                    .one();

            List<Notification> notifications = dataManager.load(Notification.class)
                    .query("select e from naklei_Notification e where e.driver = :driver")
                    .parameter("driver", driver)
                    .viewProperties(
                            "message",
                            "title",
                            "success",
                            "createTs",
                            "visit",
                            "moderation",
                            "moderation.id",
                            "moderation.type",
                            "advertisment",
                            "visit.id",
                            "advertisment.id")
                    .list();

            if (notifications.size() > 0){
                notifications.forEach(notification -> {
                    NotificationResponseDto.Result result = new NotificationResponseDto.Result();
                    result.setTitle(notification.getTitle());
                    result.setMessage(notification.getMessage());
                    result.setSuccess(notification.getSuccess());
                    result.setAdvertisementId(notification.getAdvertisment() != null ? notification.getAdvertisment().getId().toString() : null);
                    result.setModerationId(notification.getModeration() != null ? notification.getModeration().getId().toString() : null);
                    result.setVisitId(notification.getVisit() != null ? notification.getVisit().getId().toString() : null);
                    result.setVerifyModeration(EModerationType.VERIFY.equals(notification.getModeration().getType()));
                    result.setDate(dateFormatWithTime.format(notification.getCreateTs()));
                    results.add(result);
                });
            }
        }catch (Exception e){
            response.setCode("1");
            response.setMessage("Error: DriverServiceBean/getNotifications. ErrorText " + e.getMessage());
        }finally {
            authentication.end();
        }

        return response;
    }

    private void driverRegistration(ExtUser extUser, DriverRegistrationRequest request, CommitContext context) {
//        ExtUser extUser = dataManager.load(ExtUser.class).id(userUUID).optional().orElse(null);
//        if (extUser == null)
//            extUser = dataManager.create(ExtUser.class);
        log.info("Registration of driver {}", request.getPhoneNumber());
        Driver driver = dataManager.create(Driver.class);
        Car car = dataManager.create(Car.class);

        //fill driver info
        driver.setCar(car);
        driver.setUser(extUser);
        driver.setDriverStatus(EDriverStatus.BRONZE);

        //fill car info
        DModel model = carService.getModelByCode(request.getCarModelCode());

        car.setDriver(driver);
        car.setModel(model);
        car.setNumber(request.getCarNumber());
        car.setYear(request.getCarYear());
        car.setColor(carService.getColorByCode(request.getCarColorCode()));
        car.setCarClass(carService.getCarClassByModelAndYear(model, car.getYear()));

        //save entities to commit
        context.addInstanceToCommit(driver);
        context.addInstanceToCommit(car);

        //загрузка фотографий машины
        log.info("Uploading photos of {}", request.getFullName());
        String[] images = request.getCarPhoto();
        if (images != null) {
            for (String image : images) {
                try {
                    byte[] bytes = Base64.getMimeDecoder().decode(image);
                    FileDescriptor fd = dataManager.create(FileDescriptor.class);
                    fd.setCreateDate(new Date());
                    fd.setName(request.getCarNumber() + "_CAR_PHOTO_" + fd.getId());
                    fd.setExtension(".jpg");
                    fileStorageAPI.saveFile(fd, bytes);
                    if (car.getPhotos() == null) {
                        car.setPhotos(new ArrayList<>());
                    }
                    car.getPhotos().add(fd);
                    context.addInstanceToCommit(fd);
                } catch (FileStorageException e) {
                    log.error("CAR PHOTOS UPLOAD", e);
                }
            }
        }
        //загрузка фотографии техпаспорта
        if (request.getTechnicalPassportPhoto() != null) {
            byte[] bytes = Base64.getMimeDecoder().decode(request.getTechnicalPassportPhoto());
            FileDescriptor fd = dataManager.create(FileDescriptor.class);
            fd.setCreateDate(new Date());
            fd.setName(request.getCarNumber() + "_TECH_PASSPORT_PHOTO_");
            fd.setExtension(".jpg");
            try {
                fileStorageAPI.saveFile(fd, bytes);
            } catch (FileStorageException e) {
                log.error("Error on TECH PASSPORT UPLOAD", e);
            }
            car.setTechPassport(fd);
            context.addInstanceToCommit(fd);
        }
    }



}