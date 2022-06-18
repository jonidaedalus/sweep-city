package kz.alash.naklei.web.screens.advlocation;

import com.haulmont.addon.maps.web.gui.components.layer.VectorLayer;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.queryconditions.JpqlCondition;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.AdvLocation;
import kz.alash.naklei.entity.Advertiser;

import javax.inject.Inject;
import java.util.List;

@UiController("naklei_AdvLocation.browse")
@UiDescriptor("adv-location-browse.xml")
@LookupComponent("advLocationsTable")
@LoadDataBeforeShow
public class AdvLocationBrowse extends StandardLookup<AdvLocation> {
    @Inject
    private CollectionLoader<AdvLocation> advLocationsDl;
    @Inject
    private DataManager dataManager;
    @Inject
    private CollectionContainer<AdvLocation> advLocationsDc;
    @Inject
    private GroupTable<AdvLocation> advLocationsTable;

    @Subscribe("advertiserFilter")
    public void onAdvertiserFilterValueChange(HasValue.ValueChangeEvent<Advertiser> event) {
        Advertiser advertiser = event.getValue();

        if(advertiser != null){
            LoadContext<AdvLocation> loadContext = advLocationsDl.createLoadContext();
            loadContext.getQuery()
                    .setCondition(JpqlCondition.where("e.advertiser = :advertiser"))
                    .setParameter("advertiser",advertiser);

            List<AdvLocation> locations = dataManager.loadList(loadContext);
            advLocationsDc.setItems(locations);
        }
    }


    @Subscribe("map.advLocationsLayer")
    private void onAdvLocationSelected(VectorLayer.GeoObjectSelectedEvent<AdvLocation> event){
        advLocationsTable.setSelected(event.getItem());
    }
}