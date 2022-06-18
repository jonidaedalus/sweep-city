package kz.alash.naklei.web.screens.advertiser;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.screen.LookupComponent;
import kz.alash.naklei.entity.Advertisement;
import kz.alash.naklei.entity.Advertiser;
import kz.alash.naklei.entity.dict.EAdvStatus;
import kz.alash.naklei.web.screens.advertisement.AdvertisementEdit;
import kz.alash.naklei.web.screens.advertiser.statitics.AdvertiserStatisticsRead;

import javax.inject.Inject;
import java.util.Set;

import static kz.alash.naklei.ConstantsRole.ADVERTISER_EMPLOYEE;

@UiController("naklei_Advertiser.browse")
@UiDescriptor("advertiser-browse.xml")
@LookupComponent("advertisersTable")
@LoadDataBeforeShow
public class AdvertiserBrowse extends StandardLookup<Advertiser> {
    @Inject
    private Notifications notifications;
    @Inject
    private Screens screens;
    @Inject
    private Table<Advertiser> advertisersTable;
    @Inject
    private DataManager dataManager;
    @Inject
    private Button addAdvertisement;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private Button createBtn;
    @Inject
    private Filter filter;
    @Inject
    private CollectionContainer<Advertiser> advertisersDc;
    @Inject
    private Button openStats;

    @Subscribe
    public void onInit(InitEvent event) {
        advertisersTable.addGeneratedColumn("logo", advertiser -> {
            if (advertiser.getLogo() != null) {
                Image avatarImage = uiComponents.create(Image.class);
                avatarImage.setScaleMode(Image.ScaleMode.SCALE_DOWN);
                avatarImage.setAlignment(Component.Alignment.MIDDLE_CENTER);
                avatarImage.setWidth("100%");
                avatarImage.setHeight("30px");

                avatarImage.setValueSource(new ContainerValueSource<>(advertisersTable.getInstanceContainer(advertiser), "logo"));
                return avatarImage;
            }
            return null;
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if(userSessionSource.getUserSession().getRoles().contains(ADVERTISER_EMPLOYEE)){
            createBtn.setVisible(false);
            addAdvertisement.setVisible(false);
            filter.setVisible(false);
        }
    }

    @Subscribe("advertisersTable")
    public void onAdvertisersTableSelection(Table.SelectionEvent<Advertiser> event) {
        Set<Advertiser> selectedAdvertisers = event.getSelected();
        boolean enable = selectedAdvertisers.size() == 1;
        addAdvertisement.setEnabled(enable);
        openStats.setEnabled(enable);
    }

    public void addAdvertisement() {
        Advertiser advertiser = advertisersTable.getSingleSelected();
        AdvertisementEdit advertisementCreateScreen = screens.create(AdvertisementEdit.class);
        Advertisement advertisement = dataManager.create(Advertisement.class);
        advertisement.setAdvertiser(advertiser);
        advertisement.setStatus(EAdvStatus.IN_PROCESS);
        advertisementCreateScreen.setEntityToEdit(advertisement);
        advertisementCreateScreen.show();
    }

    public void openStats() {
        AdvertiserStatisticsRead advertiserStatisticsRead = screens.create(AdvertiserStatisticsRead.class);
        advertiserStatisticsRead.setEntityToEdit(advertisersTable.getSingleSelected());
        advertiserStatisticsRead.show();
    }
}