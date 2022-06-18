package kz.alash.naklei.web.screens.dmark;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DMark;

@UiController("naklei_DMark.edit")
@UiDescriptor("d-mark-edit.xml")
@EditedEntityContainer("dMarkDc")
@LoadDataBeforeShow
public class DMarkEdit extends StandardEditor<DMark> {
}