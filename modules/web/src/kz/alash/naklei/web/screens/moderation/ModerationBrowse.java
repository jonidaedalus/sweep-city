package kz.alash.naklei.web.screens.moderation;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.ExtUser;
import kz.alash.naklei.entity.dict.EModerationType;
import kz.alash.naklei.entity.moderation.Moderation;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.SimpleFormatter;

import static kz.alash.naklei.ConstantsRole.ADVERTISER_EMPLOYEE;

@UiController("naklei_Moderation.browse")
@UiDescriptor("moderation-browse.xml")
@LookupComponent("moderationsTable")
@LoadDataBeforeShow
public class ModerationBrowse extends StandardLookup<Moderation> {
    @Inject
    private GroupTable<Moderation> moderationsTable;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionLoader<Moderation> moderationsDl;

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

        if (userSessionSource.getUserSession().getRoles().contains(ADVERTISER_EMPLOYEE)) {
            ExtUser user = (ExtUser) userSessionSource.getUserSession().getUser();
            user = dataManager.reload(user, "advertiser-user-view");
            if (user.getAdvertiser() != null) {
                moderationsDl.setQuery("select e from naklei_Moderation e where e.advertisementDriver.purpose.advertisement.advertiser = :advertiser and e.type = :type");
                Map<String, Object> params = new HashMap<>();
                params.put("advertiser", user.getAdvertiser());
                params.put("type", EModerationType.START);
                moderationsDl.setParameters(params);
                moderationsTable.getColumn("advertisementDriver.driver").setValueProvider(moderation ->
                        moderation.getAdvertisementDriver().getDriver().getUser().getName());
            }
        }
    }
    
}