package kz.alash.naklei.service;

import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DZone;

import java.util.List;

public interface CityService {
    String NAME = "naklei_CityService";

    List<DZone> getCityZones(DCity city);
}