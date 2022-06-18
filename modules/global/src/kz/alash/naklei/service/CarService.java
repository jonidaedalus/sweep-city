package kz.alash.naklei.service;

import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.dict.car.DColor;
import kz.alash.naklei.entity.dict.car.DModel;

public interface CarService {
    String NAME = "naklei_CarService";

    DModel getModelByCode(String modelCode);

    DColor getColorByCode(String colorCode);

    DClass getCarClassByModelAndYear(DModel model, int year);
}