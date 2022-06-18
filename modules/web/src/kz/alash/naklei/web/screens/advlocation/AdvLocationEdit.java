package kz.alash.naklei.web.screens.advlocation;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.AdvLocation;
import kz.alash.naklei.entity.Advertiser;

import javax.inject.Inject;

@UiController("naklei_AdvLocation.edit")
@UiDescriptor("adv-location-edit.xml")
@EditedEntityContainer("advLocationDc")
@LoadDataBeforeShow
public class AdvLocationEdit extends StandardEditor<AdvLocation> {
    @Inject
    private CollectionLoader<AdvLocation> advLocationsDl;
    @Inject
    private DataManager dataManager;

    @Subscribe
    public void onInit(InitEvent event) {
        getOtherAdvLocations(null);
    }

    @Subscribe("advertiserField")
    public void onAdvertiserFieldValueChange(HasValue.ValueChangeEvent<Advertiser> event) {
        Advertiser advertiser = event.getValue();

        getOtherAdvLocations(advertiser);

    }

    private void getOtherAdvLocations(Advertiser advertiser) {
        advLocationsDl.setParameter("advertiser", advertiser);
        advLocationsDl.load();
    }
}