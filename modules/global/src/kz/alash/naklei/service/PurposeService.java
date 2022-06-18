package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.FileStorageException;
import kz.alash.naklei.service.esb.dto.purpose.GetPotentialPurposesResponse;

import java.math.BigDecimal;

public interface PurposeService {
    String NAME = "purposeService";

    GetPotentialPurposesResponse getPotentialPurposes();

    byte[] sticker(String purposeId) throws FileStorageException;

    byte[] maket(String purposeId) throws FileStorageException;

    String stickerURL(String purposeId);
}