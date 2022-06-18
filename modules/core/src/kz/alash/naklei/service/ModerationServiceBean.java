package kz.alash.naklei.service;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.dict.EModerationStatus;
import kz.alash.naklei.entity.dict.EModerationType;
import kz.alash.naklei.entity.moderation.Moderation;
import kz.alash.naklei.service.esb.dto.moderation.ModerationRequest;
import kz.alash.naklei.service.esb.dto.moderation.ModerationResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service(ModerationService.NAME)
public class ModerationServiceBean implements ModerationService {

    @Inject
    private Authentication authentication;
    @Inject
    private DataManager dataManager;
    @Inject
    private Logger log;
    @Inject
    private FileStorageAPI fileStorageAPI;
    @Inject
    private AdvertisementService advertisementService;
    @Inject
    private DriverService driverService;

    @Override
    public ModerationResponse sendModeration(ModerationRequest request) {
        long start = System.currentTimeMillis();

        ModerationResponse response = new ModerationResponse();
        ModerationResponse.Result result = new ModerationResponse.Result();
        response.setResult(result);
    
        if(request.getModerationType() == null){
            responseWithError(start, response, "Тип модерации не может быть пустым", null);
            return response;
        }

        EModerationType moderationType = EModerationType.fromId(request.getModerationType());

        if (moderationType == null) {
            responseWithError(start, response, "Не найдена модерация с типом " + request.getModerationType(), null);
            return response;
        }

        if (moderationType.equals(EModerationType.PAYOUT) && request.getSum() == null){
            responseWithError(start, response, "Не указана сумма для выплаты", null);
            return response;
        }

        if (request.getTachometerPhoto() == null && (moderationType.equals(EModerationType.FINISH) || moderationType.equals(EModerationType.START))){
            responseWithError(start, response, "Не передано фото тахометра", null);
            return response;
        }

        if (request.getPhotos() == null || request.getPhotos().length == 0){
            responseWithError(start, response, "Не передано фото автомобиля", null);
            return response;
        }

        try {
            User user = authentication.begin().getUser();

            Driver driver = driverService.getDriverByUserId(user.getId())
                    .viewProperties("user", "user.phoneNumber", "advertisementDrivers", "advertisementDrivers.endDate")
                    .one();

            AdvertisementDriver advertisementDriver = advertisementService.getAdvertisementDriver(user.getId(),UUID.fromString(request.getPurposeId()));;

            if (advertisementDriver == null){
                if (moderationType.equals(EModerationType.PRECHECK)) {
                    try {
                        advertisementService.setAdvertisement(UUID.fromString(request.getPurposeId()));
                        advertisementDriver = advertisementService.getAdvertisementDriver(user.getId(), UUID.fromString(request.getPurposeId()));
                    } catch (Exception ex) {
                        responseWithError(start, response, "Ошибка при присоединении водителя к рекл. кампании", String.valueOf(ex));
                        authentication.end();
                        return response;
                    }
                }
            }

            if (advertisementDriver == null) {
                responseWithError(start, response, "Не найден активный водитель рекламной кампании", null);
                authentication.end();
                return response;
            }

            Moderation moderation = getModeration(advertisementDriver, moderationType, request.getModerationID());

            if (moderationType.equals(EModerationType.VERIFY))
                moderation.setStatus(EModerationStatus.IN_CHECK);

            if (moderationType.equals(EModerationType.PAYOUT) || moderationType.equals(EModerationType.FINISH))
                moderation.setSum(request.getSum());

            CommitContext context = new CommitContext(moderation);

            for (String image : request.getPhotos()) {
                try {
                    byte[] bytes = Base64.getMimeDecoder().decode(image);
                    FileDescriptor fd = dataManager.create(FileDescriptor.class);
                    fd.setCreateDate(new Date());
                    StringBuilder sb = new StringBuilder();
                    fd.setName(sb.append(driver.getUser().getPhoneNumber()).append(moderationType.getId())
                            .append(fd.getId()).toString());
                    fd.setExtension(".jpg");
                    fileStorageAPI.saveFile(fd, bytes);
                    if (moderation.getPhotos() == null) {
                        moderation.setPhotos(new ArrayList<>());
                    }
                    moderation.getPhotos().add(fd);
                    context.addInstanceToCommit(fd);
                } catch (FileStorageException e) {
                    responseWithError(start, response, "Ошибка при загрузке фото в модерацию", String.valueOf(e));
                    authentication.end();
                    return response;
                }
            }

            if (moderationType.equals(EModerationType.START) || moderationType.equals(EModerationType.FINISH)){
                context.addInstanceToCommit(advertisementDriver);

                try {
                    byte[] bytes = Base64.getMimeDecoder().decode(request.getTachometerPhoto());
                    FileDescriptor fd = dataManager.create(FileDescriptor.class);
                    fd.setCreateDate(new Date());
                    StringBuilder sb = new StringBuilder();
                    fd.setName(sb.append(driver.getUser().getPhoneNumber()).append("_INIT_TACHOMETER_")
                            .append(fd.getId()).toString());
                    fd.setExtension(".jpg");
                    fileStorageAPI.saveFile(fd, bytes);
                    context.addInstanceToCommit(fd);

                    if(moderationType.equals(EModerationType.FINISH))
                        advertisementDriver.setFinalTachometerPhoto(fd);
                    else
                        advertisementDriver.setInitialTachometerPhoto(fd);

                } catch (FileStorageException e) {
                    responseWithError(start, response, "Ошибка при загрузке фото тахометра в модерацию", String.valueOf(e));
                    authentication.end();
                    return response;
                }
            }

            dataManager.commit(context);

            response.setCode("0");
            response.setMessage("Moderation " + moderation.getId() + " with status " + moderation.getStatus() + " has been created");
        } catch (Exception e) {
            response.setCode("-1");
            response.setMessage("Ошибка при отправке на модерацию : " + e.getMessage());
            log.error("Error: ModerationServiceBean/sendModeration. ErrorMessage " + e.getMessage());
        } finally {
            authentication.end();
        }

        response.setTimeElapsedMillis(System.currentTimeMillis() - start);
        return response;
    }

    private void responseWithError(long start, ModerationResponse response, String errorMsg, String stackTrace) {
        response.setCode("-1");
        response.setMessage(errorMsg);
        response.setTimeElapsedMillis(System.currentTimeMillis() - start);
        if(stackTrace != null)
            response.setStackTrace(stackTrace);
    }

    private Moderation getModeration(AdvertisementDriver advertisementDriver, EModerationType type, String moderationID) {
        if (moderationID != null && !moderationID.isEmpty() && type.equals(EModerationType.VERIFY)) {
            return dataManager.load(Moderation.class)
                    .query("select m from naklei_Moderation m where m.id = :id")
                    .parameter("id", moderationID)
                    .viewProperties(
                            "id",
                            "status",
                            "sum",
                            "photos"
                    )
                    .one();
        }
        Moderation moderation = dataManager.create(Moderation.class);
        moderation.setAdvertisementDriver(advertisementDriver);
        moderation.setStatus(EModerationStatus.IN_WORK);
        moderation.setType(type);
        return moderation;
    }
}