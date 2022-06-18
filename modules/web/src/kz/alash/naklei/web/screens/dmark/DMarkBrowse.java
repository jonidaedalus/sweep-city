package kz.alash.naklei.web.screens.dmark;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DMark;

@UiController("naklei_DMark.browse")
@UiDescriptor("d-mark-browse.xml")
@LookupComponent("dMarksTable")
@LoadDataBeforeShow
public class DMarkBrowse extends StandardLookup<DMark> {
}