package kz.alash.naklei.web.screens.dpointcost;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DPointCost;

@UiController("naklei_DPointCost.browse")
@UiDescriptor("d-point-cost-browse.xml")
@LookupComponent("dPointCostsTable")
@LoadDataBeforeShow
public class DPointCostBrowse extends StandardLookup<DPointCost> {
}