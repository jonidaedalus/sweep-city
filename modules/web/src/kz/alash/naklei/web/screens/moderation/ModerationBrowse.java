package kz.alash.naklei.web.screens.moderation;

import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.moderation.Moderation;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.logging.SimpleFormatter;

@UiController("naklei_Moderation.browse")
@UiDescriptor("moderation-browse.xml")
@LookupComponent("moderationsTable")
@LoadDataBeforeShow
public class ModerationBrowse extends StandardLookup<Moderation> {
    @Inject
    private GroupTable<Moderation> moderationsTable;
    @Inject
    private UiComponents uiComponents;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        moderationsTable.addStyleProvider((moderation, property) -> {
            if ("statusIcon".equals(property)) {
                if (moderation.getStatus() == null) {
                    return null;
                }
                switch (moderation.getStatus()) {
                    case IN_WORK:
                        return "order-in-work-icon";
                    case ACCEPTED:
                        return "order-accepted-icon";
                    case REJECTED:
                        return "order-rejected-icon";
                }
            }
            return null;
        });
    }
    
}