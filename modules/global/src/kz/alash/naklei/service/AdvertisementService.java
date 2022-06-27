package kz.alash.naklei.service;

import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.dict.DStickerType;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.service.esb.dto.GenericResponse;
import kz.alash.naklei.service.esb.dto.advertisement.AdvertisementDriverDto;
import kz.alash.naklei.service.esb.dto.driver.SetAdvertisementResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface AdvertisementService {
    String NAME = "naklei_AdvertisementService";

    SetAdvertisementResponse setAdvertisement(UUID purposeId);

    Long calculateMaxCarAmount(
            BigDecimal budget,
            DClass carClass,
            DStickerType stickerType,
            BigDecimal pastingCost,
            BigDecimal rewardAmount,
            long numberOfDaysBetweenStartAndEnd) throws Exception;

    GenericResponse<AdvertisementDriverDto> terminationOfCurrentAdvContract();

    AdvertisementDriver getAdvertisementDriver(UUID userId, UUID purposeId);

    AdvertisementDriver getCurrentAdvertisementDriver(Driver driver);

    void deleteVisits(AdvertisementDriver advertisementDriver);

    void changeAdvertisementStatus();
}