package kz.alash.naklei.web.screens.dtrafic;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DTraffic;

@UiController("naklei_DTrafic.edit")
@UiDescriptor("d-trafic-edit.xml")
@EditedEntityContainer("dTraficDc")
@LoadDataBeforeShow
public class DTraficEdit extends StandardEditor<DTraffic> {
}