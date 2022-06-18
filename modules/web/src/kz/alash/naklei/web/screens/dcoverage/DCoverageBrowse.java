package kz.alash.naklei.web.screens.dcoverage;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DCoverage;

@UiController("naklei_DCoverage.browse")
@UiDescriptor("d-coverage-browse.xml")
@LookupComponent("dCoveragesTable")
@LoadDataBeforeShow
public class DCoverageBrowse extends StandardLookup<DCoverage> {
}