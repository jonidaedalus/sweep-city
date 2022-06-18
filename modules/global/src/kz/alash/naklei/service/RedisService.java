package kz.alash.naklei.service;

import kz.alash.naklei.entity.dict.DZone;

public interface RedisService {
    String NAME = "naklei_RedisService";

    String getZonesByCode(String cityCode);

}