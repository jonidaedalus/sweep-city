package kz.alash.naklei.web.screens.dpushmessage;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DPushMessage;

@UiController("naklei_DPushMessage.edit")
@UiDescriptor("d-push-message-edit.xml")
@EditedEntityContainer("dPushMessageDc")
@LoadDataBeforeShow
public class DPushMessageEdit extends StandardEditor<DPushMessage> {
}