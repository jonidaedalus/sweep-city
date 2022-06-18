package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.DataManager;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.dict.car.DColor;
import kz.alash.naklei.entity.dict.car.DModel;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(CarService.NAME)
public class CarServiceBean implements CarService {

    @Inject
    private DataManager dataManager;
    @Inject
    private Logger log;

    @Override
    public DModel getModelByCode(String modelCode) {
        try {
            return dataManager.load(DModel.class)
                    .query("select a from naklei_DModel a where a.code= :modelCode")
                    .parameter("modelCode", modelCode)
                    .view("dModel-view")
                    .one();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public DColor getColorByCode(String colorCode) {
        try {
            return dataManager.load(DColor.class)
                    .query("select a from naklei_DColor a where a.code= :colorCode")
                    .parameter("colorCode", colorCode)
                    .view("dColor-view")
                    .one();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public DClass getCarClassByModelAndYear(DModel model, int year) {
        try {
            return dataManager.load(DClass.class)
                    .query("select a.classs from naklei_DCarClassification a where a.model= :model and a.firstYear <= :year and a.lastYear > :year")
                    .parameter("model", model)
                    .parameter("year", year)
                    .view("dClass-view")
                    .one();
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}