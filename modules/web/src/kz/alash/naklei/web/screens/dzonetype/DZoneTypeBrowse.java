package kz.alash.naklei.web.screens.dzonetype;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DZoneType;

@UiController("naklei_DZoneType.browse")
@UiDescriptor("d-zone-type-browse.xml")
@LookupComponent("dZoneTypesTable")
@LoadDataBeforeShow
public class DZoneTypeBrowse extends StandardLookup<DZoneType> {
}