package kz.alash.naklei.web.screens.dclass;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DClass;

@UiController("naklei_DClass.browse")
@UiDescriptor("d-class-browse.xml")
@LookupComponent("dClassesTable")
@LoadDataBeforeShow
public class DClassBrowse extends StandardLookup<DClass> {
}