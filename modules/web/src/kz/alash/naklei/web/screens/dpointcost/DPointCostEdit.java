package kz.alash.naklei.web.screens.dpointcost;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DPointCost;

@UiController("naklei_DPointCost.edit")
@UiDescriptor("d-point-cost-edit.xml")
@EditedEntityContainer("dPointCostDc")
@LoadDataBeforeShow
public class DPointCostEdit extends StandardEditor<DPointCost> {
}