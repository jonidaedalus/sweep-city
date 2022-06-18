package kz.alash.naklei.web.screens.dmodel;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DModel;

@UiController("naklei_DModel.browse")
@UiDescriptor("d-model-browse.xml")
@LookupComponent("dModelsTable")
@LoadDataBeforeShow
public class DModelBrowse extends StandardLookup<DModel> {
}