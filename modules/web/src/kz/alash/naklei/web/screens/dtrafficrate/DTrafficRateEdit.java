package kz.alash.naklei.web.screens.dtrafficrate;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DTrafficRate;

@UiController("naklei_DTrafficRate.edit")
@UiDescriptor("d-traffic-rate-edit.xml")
@EditedEntityContainer("dTrafficRateDc")
@LoadDataBeforeShow
public class DTrafficRateEdit extends StandardEditor<DTrafficRate> {
}