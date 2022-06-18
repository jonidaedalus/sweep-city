package kz.alash.naklei.web.screens.advertisement;

import com.haulmont.addon.maps.web.gui.components.GeoMap;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.Advertisement;

import javax.inject.Inject;

@UiController("naklei_Advertisement.map")
@UiDescriptor("advertisement-map.xml")
@EditedEntityContainer("advertisementDc")
@LoadDataBeforeShow
public class AdvertisementMap extends StandardEditor<Advertisement> {
    @Inject
    private GeoMap map;
    @Inject
    private Notifications notifications;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        Advertisement advertisement = getEditedEntity();
        //todo: создать справочник городов с кординатами x,y
        if(advertisement.getCity().getCode().equals("TSE"))
            map.setCenter( 71.435412168,51.1307045107);

        map.setEnabled(true);
    }

//    @Subscribe("map")
//    public void onMapClick(GeoMap.ClickEvent event) {
//        notifications.create(Notifications.NotificationType.HUMANIZED)
//                .withCaption("Координаты")
//                .withDescription(event.getPoint().toText())
//                .show();
//
//    }

}