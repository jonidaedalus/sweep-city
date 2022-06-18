package kz.alash.naklei.web.screens.dpushmessage;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DPushMessage;

@UiController("naklei_DPushMessage.browse")
@UiDescriptor("d-push-message-browse.xml")
@LookupComponent("dPushMessagesTable")
@LoadDataBeforeShow
public class DPushMessageBrowse extends StandardLookup<DPushMessage> {
}