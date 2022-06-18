package kz.alash.naklei.web.screens.dclass;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DClass;

@UiController("naklei_DClass.edit")
@UiDescriptor("d-class-edit.xml")
@EditedEntityContainer("dClassDc")
@LoadDataBeforeShow
public class DClassEdit extends StandardEditor<DClass> {
}