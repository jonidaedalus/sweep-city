package kz.alash.naklei.web.screens.dcolor;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DColor;

@UiController("naklei_DColor.edit")
@UiDescriptor("d-color-edit.xml")
@EditedEntityContainer("dColorDc")
@LoadDataBeforeShow
public class DColorEdit extends StandardEditor<DColor> {
}