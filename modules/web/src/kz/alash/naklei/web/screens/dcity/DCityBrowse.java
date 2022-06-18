package kz.alash.naklei.web.screens.dcity;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DCity;

@UiController("naklei_DCity.browse")
@UiDescriptor("d-city-browse.xml")
@LookupComponent("dCitiesTable")
@LoadDataBeforeShow
public class DCityBrowse extends StandardLookup<DCity> {
}