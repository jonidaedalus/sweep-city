package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.FileStorageException;

public interface AdvertiserService {
    String NAME = "advertiserService";

    byte[] advertiserLogo(String advertiserId) throws FileStorageException;
}