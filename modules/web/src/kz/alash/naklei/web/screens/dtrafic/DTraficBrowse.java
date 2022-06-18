package kz.alash.naklei.web.screens.dtrafic;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DTraffic;

@UiController("naklei_DTrafic.browse")
@UiDescriptor("d-trafic-browse.xml")
@LookupComponent("dTraficsTable")
@LoadDataBeforeShow
public class DTraficBrowse extends StandardLookup<DTraffic> {
}