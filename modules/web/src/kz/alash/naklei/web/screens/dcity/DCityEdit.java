package kz.alash.naklei.web.screens.dcity;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DCity;

@UiController("naklei_DCity.edit")
@UiDescriptor("d-city-edit.xml")
@EditedEntityContainer("dCityDc")
@LoadDataBeforeShow
public class DCityEdit extends StandardEditor<DCity> {
}