package kz.alash.naklei.web.screens.dtrafficrate;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DTrafficRate;

@UiController("naklei_DTrafficRate.browse")
@UiDescriptor("d-traffic-rate-browse.xml")
@LookupComponent("dTrafficRatesTable")
@LoadDataBeforeShow
public class DTrafficRateBrowse extends StandardLookup<DTrafficRate> {
}