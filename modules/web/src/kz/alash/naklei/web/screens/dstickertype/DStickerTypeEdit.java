package kz.alash.naklei.web.screens.dstickertype;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DStickerType;

@UiController("naklei_DStickerType.edit")
@UiDescriptor("d-sticker-type-edit.xml")
@EditedEntityContainer("dStickerTypeDc")
@LoadDataBeforeShow
public class DStickerTypeEdit extends StandardEditor<DStickerType> {
}