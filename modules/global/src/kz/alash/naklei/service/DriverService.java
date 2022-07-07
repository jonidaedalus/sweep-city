package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.FluentLoader;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.service.esb.dto.NotificationResponseDto;
import kz.alash.naklei.service.esb.dto.driver.DriverInfoResponse;
import kz.alash.naklei.service.esb.dto.driver.DriverRegistrationRequest;
import kz.alash.naklei.service.esb.dto.driver.DriverRegistrationResponse;
import kz.alash.naklei.service.esb.dto.driver.DriverUpdateRequest;
import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.util.UUID;

public interface DriverService {
    String NAME = "driverService";

    DriverRegistrationResponse register(DriverRegistrationRequest request);

    DriverRegistrationResponse deleteAccount();

    void update(DriverUpdateRequest request);

    DriverInfoResponse driverInfo();

    GenericResponse<String> setCreditCard(String cardNumber, String cardName);

    FluentLoader.ByQuery<Driver, UUID> getDriverByUserId(UUID userId);

    NotificationResponseDto getNotifications();

}