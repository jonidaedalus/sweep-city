package kz.alash.naklei.service;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.AdvPurpose;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.dict.EAdvStatus;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.service.esb.dto.GeneralDictDTO;
import kz.alash.naklei.service.esb.dto.purpose.GetPotentialPurposesResponse;
import kz.alash.naklei.service.esb.dto.purpose.PurposeDTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service(PurposeService.NAME)
public class PurposeServiceBean implements PurposeService {
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private static final String fileDirectoryURL = "/APP/connector/0/103/source/";

    @Inject
    private DataManager dataManager;
    @Inject
    private FileStorageAPI fileStorageAPI;
    @Inject
    private GlobalConfig globalConfig;
    @Inject
    private Authentication authentication;
    @Inject
    private DriverService driverService;

    @Override
    public GetPotentialPurposesResponse getPotentialPurposes() {
        User user = authentication.begin().getUser();

        Driver driver = driverService.getDriverByUserId(user.getId())
                .viewProperties("driverStatus", "car", "car.carClass", "car.carClass.code")
                .one();

        List<AdvPurpose> purposeList = dataManager.load(AdvPurpose.class)
                .query("select a from naklei_AdvPurpose a where a.advertisement.status in ('ACTIVE','PENDING')")
                .viewProperties(
                        "advertisement.name",
                        "advertisement.status",
                        "advertisement.startDate",
                        "advertisement.endDate",
                        "advertisement.city.code",
                        "advertisement.city.name",
                        "advertisement.advertiser",
                        "advertisement.advertiser.fullName",
                        "advertisement.advertiser.desc",
                        "advertisement.advertiser.id",
                        "id",
                        "carAmount",
                        "sticker",
                        "sticker.name",
                        "stickerType.code",
                        "stickerType.name",
                        "stickerType.pointCoef",
                        "carClass.code",
                        "carClass.name",
                        "rewardAmount",
                        "pastingDate",
                        "carClass",
                        "carClass.pointCoef"
                )
                .list();


        GetPotentialPurposesResponse response = GetPotentialPurposesResponse.newInstance();
        for (AdvPurpose purpose : purposeList) {
            //Ищем водит. рекл. кампаний по цели и статусу(КОЛ-ВО УЧАСТНИКИ)
            int participants = dataManager.load(AdvertisementDriver.class)
                    .query("select d from naklei_AdvertisementDriver d where d.purpose = :purpose and d.status not in ('REJECTED','FINISHED')")
                    .parameter("purpose", purpose)
                    .list()
                    .size();

            //Показываем только тех у кого кол-во участникиков меньше машин заложенных в цель(3/5)
            if(participants < purpose.getCarAmount() && driverCarClassMatchPurposeCarClass(driver.getCar().getCarClass(), purpose.getCarClass())){

                PurposeDTO purposeDTO = new PurposeDTO();
                purposeDTO.setId(String.valueOf(purpose.getId()));
                purposeDTO.setName(purpose.getAdvertisement().getAdvertiser().getFullName());
                purposeDTO.setNiche(purpose.getAdvertisement().getAdvertiser().getDesc());
                purposeDTO.setDescription(purpose.getAdvertisement().getName());
                purposeDTO.setParticipants(participants);

                purposeDTO.setCarMaket(purpose.getSticker().getId().toString());
                purposeDTO.setStatus(purpose.getAdvertisement().getStatus().getId());
                purposeDTO.setCarAmount(purpose.getCarAmount());
                purposeDTO.setStickerType(GeneralDictDTO.of(purpose.getStickerType().getCode(), purpose.getStickerType().getName()));
                purposeDTO.setCarClass(GeneralDictDTO.of(purpose.getCarClass().getCode(), purpose.getCarClass().getName()));
                purposeDTO.setCity(GeneralDictDTO.of(purpose.getAdvertisement().getCity().getCode(), purpose.getAdvertisement().getCity().getName()));
                purposeDTO.setReward(purpose.getRewardAmount());
                purposeDTO.setPastingDate(dateFormat.format(purpose.getPastingDate()));
                purposeDTO.setStartDate(dateFormat.format(purpose.getAdvertisement().getStartDate()));
                purposeDTO.setEndDate(dateFormat.format(purpose.getAdvertisement().getEndDate()));
                Date today = new Date();
                purposeDTO.setActive(purpose.getAdvertisement().getStatus().equals(EAdvStatus.ACTIVE) || (purpose.getPastingDate().equals(today) || purpose.getPastingDate().before(today)));
                //доход за км = коэф за поинт * коэф за статус водителя(бронз, ...) * коэф тип наклейки * 1,37 (коэф зоны(макс)) * 1,37(коэф за время) * коэф класс машины
                //15 - стоимость за поинт хардкод - можно было метод написать для поиска по атрибутам(но там все 15)
                BigDecimal income = BigDecimal.ONE;
                income = income.multiply(BigDecimal.valueOf(purpose.getCarClass().getPointCoef()))
                        .multiply(driver.getDriverStatus().getPointCoef())
                        .multiply(BigDecimal.valueOf(15))
                        .multiply(BigDecimal.valueOf(1.37))
                        .multiply(BigDecimal.valueOf(1.37))
                        .multiply(BigDecimal.valueOf(purpose.getStickerType().getPointCoef()));

                purposeDTO.setIncome(income);
                purposeDTO.setAdvertiserId(purpose.getAdvertisement().getAdvertiser().getId().toString());
                response.getResult().add(purposeDTO);
            }
        }

        return response;
    }

    private boolean driverCarClassMatchPurposeCarClass(DClass driverCarClass, DClass purposeCarClass) {
        if(purposeCarClass.getCode().equals(driverCarClass.getCode()))
            return true;
        //Комфорт видит Эконом
        if(driverCarClass.getCode().equals("COMFORT") && purposeCarClass.getCode().equals("ECONOM"))
            return true;

        return driverCarClass.getCode().equals("BUSINESS") && (purposeCarClass.getCode().equals("COMFORT") || purposeCarClass.getCode().equals("ECONOM"));
    }

    @Override
    public byte[] sticker(String purposeId) throws FileStorageException {
        AdvPurpose purpose = dataManager.load(AdvPurpose.class).query("select a from naklei_AdvPurpose a where a.id = :id")
                .parameter("id", UUID.fromString(purposeId))
                .viewProperties(
                        "sticker",
                        "sticker.name",
                        "sticker.extension",
                        "sticker.size",
                        "sticker.createDate"
                )
                .one();
        return fileStorageAPI.loadFile(purpose.getSticker());
    }

    @Override
    public byte[] maket(String purposeId) throws FileStorageException {
        AdvPurpose purpose = dataManager.load(AdvPurpose.class).query("select a from naklei_AdvPurpose a where a.id = :id")
                .parameter("id", UUID.fromString(purposeId))
                .viewProperties(
                        "maket",
                        "maket.name",
                        "maket.extension",
                        "maket.size",
                        "maket.createDate"
                )
                .one();
        return fileStorageAPI.loadFile(purpose.getMaket());
    }

    @Override
    public String stickerURL(String purposeId) {
        AdvPurpose purpose = dataManager.load(AdvPurpose.class).query("select a from naklei_AdvPurpose a where a.id = :id")
                .parameter("id", UUID.fromString(purposeId))
                .viewProperties(
                        "sticker",
                        "sticker.name",
                        "sticker.extension",
                        "sticker.size",
                        "sticker.createDate"
                )
                .one();
        return globalConfig.getWebAppUrl() + fileDirectoryURL + purpose.getSticker().getName();
    }
}