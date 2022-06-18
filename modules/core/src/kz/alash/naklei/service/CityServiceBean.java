package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.DataManager;
import kz.alash.naklei.entity.dict.DCity;
import kz.alash.naklei.entity.dict.DZone;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(CityService.NAME)
public class CityServiceBean implements CityService {

    @Inject
    private DataManager dataManager;

    @Override
    public List<DZone> getCityZones(DCity city) {
        return dataManager.load(DZone.class)
                .query("select e from naklei_DZone e where e.city =:city")
                .parameter("city", city)
                .viewProperties("polygon","zoneType","zoneType.pointCoef", "zoneType.name", "name")
                .list();
    }
}