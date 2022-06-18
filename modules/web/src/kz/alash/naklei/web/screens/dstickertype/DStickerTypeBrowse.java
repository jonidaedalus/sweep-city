package kz.alash.naklei.web.screens.dstickertype;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DStickerType;

@UiController("naklei_DStickerType.browse")
@UiDescriptor("d-sticker-type-browse.xml")
@LookupComponent("dStickerTypesTable")
@LoadDataBeforeShow
public class DStickerTypeBrowse extends StandardLookup<DStickerType> {
}