package kz.alash.naklei.web.screens.dcarclassification;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DCarClassification;

@UiController("naklei_DCarClassification.browse")
@UiDescriptor("d-car-classification-browse.xml")
@LookupComponent("dCarClassificationsTable")
@LoadDataBeforeShow
public class DCarClassificationBrowse extends StandardLookup<DCarClassification> {
}