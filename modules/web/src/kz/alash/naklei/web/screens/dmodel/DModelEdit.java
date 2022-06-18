package kz.alash.naklei.web.screens.dmodel;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DModel;

@UiController("naklei_DModel.edit")
@UiDescriptor("d-model-edit.xml")
@EditedEntityContainer("dModelDc")
@LoadDataBeforeShow
public class DModelEdit extends StandardEditor<DModel> {
}