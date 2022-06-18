package kz.alash.naklei.web.screens.car;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityAccessException;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.model.InstanceLoader;
import com.haulmont.cuba.gui.model.KeyValueCollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Car;
import kz.alash.naklei.web.screens.advertisementdriver.AdvDriverShowFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("naklei_Car.edit")
@UiDescriptor("car-edit.xml")
@EditedEntityContainer("carDc")
public class CarEdit extends StandardEditor<Car> {
    private static final Logger log = LoggerFactory.getLogger(CarEdit.class);
    @Inject
    private Image carPhoto;

    private int index = 0;
    private int photoSize = 0;

    @Inject
    private KeyValueCollectionLoader menuDl;
    @Inject
    private Tree<KeyValueEntity> tree;
    @Inject
    private VBoxLayout menuContainer;
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<Car> carDc;
    @Inject
    private InstanceLoader<AdvertisementDriver> currentAdvDriverDl;
    @Inject
    private AdvDriverShowFragment currentAdvDriverFragment;
    @Inject
    private InstanceContainer<AdvertisementDriver> currentAdvDriverDc;

    @Subscribe
    public void onInit(InitEvent event) {
        tree.addSelectionListener(selectionEvent ->
                selectionEvent.getSelected().stream().findFirst().ifPresent(
                        selectedMenu ->
                                tree.getItems().getItems().forEach(item -> {
                                    ECarMenu menu = ECarMenu.fromId(item.getId().toString());
                                    if (menu != null && menu.getContentId() != null) {
                                        Component component = menuContainer.getComponent(menu.getContentId());
                                        if (component != null) {
                                            component.setVisible(menu.getId().equals(selectedMenu.getId()));
                                        }
                                    }
                                })
                )
        );
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        getWindow().setCaption(getWindow().getCaption() + " - " + getEditedEntity().getNumber());
        Car car = dataManager.reload(getEditedEntity(), "car-show");
        carDc.setItem(car);
        setEntityToEdit(car);

        currentAdvDriverDl.setParameter("driver", getEditedEntity().getDriver());
        try {
            currentAdvDriverDl.load();
        }catch (Exception e){
            log.error(e.getMessage() + "\nТекущей рекл. кампаниии нет");
        }
        if(currentAdvDriverDc.getItemOrNull() != null){
            currentAdvDriverFragment.setAdvDriver(currentAdvDriverDc.getItemOrNull());
        }

        menuDl.load();
    }

    //todo кнопки nextBtn/prevBtn
    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if(getEditedEntity().getPhotos() != null && getEditedEntity().getPhotos().size() > 0)
            photoSize += getEditedEntity().getPhotos().size();

        if(getEditedEntity().getTechPassport() != null)
            photoSize++;

        updateTypeImage();

        if(tree.getItems().size() > 0)
            tree.setSelected(tree.getItems().getItems().findFirst().orElse(null));
    }

    @Install(to = "menuDl", target = Target.DATA_LOADER)
    private List<KeyValueEntity> menuDlLoadDelegate(ValueLoadContext valueLoadContext) {
        List<KeyValueEntity> list = new ArrayList<>();

        for (ECarMenu eMenu : ECarMenu.values()) {
            if(eMenu.equals(ECarMenu.CURRENT_ADV) && currentAdvDriverDc.getItemOrNull() == null){
                Component component = menuContainer.getComponent(eMenu.getContentId());
                if (component != null) {
                    component.setVisible(false);
                }
                continue;
            }

            KeyValueEntity menu = new KeyValueEntity();
            menu.setIdName("menuId");
            menu.setId(eMenu.getId());
            menu.setValue("name", eMenu.getName());

            list.add(menu);
        }

        return list;
    }

    private void updateTypeImage() {
//        getEditedEntity();
        if(photoSize > 0){
            if (getEditedEntity().getPhotos() != null && getEditedEntity().getPhotos().size() > 0 && index <= getEditedEntity().getPhotos().size() - 1)
                carPhoto.setSource(FileDescriptorResource.class)
                        .setFileDescriptor(getEditedEntity().getPhotos().get(index));
            else
                carPhoto.setSource(FileDescriptorResource.class)
                        .setFileDescriptor(getEditedEntity().getTechPassport());

        }
    }

    public void typePrevImage() {
        if (getEditedEntity().getPhotos() != null && getEditedEntity().getPhotos().size() > 0) {
            index = --index >= 0 ? index : photoSize - 1;
            updateTypeImage();
        }
    }

    public void typeNextImage() {
        if (getEditedEntity().getPhotos() != null && getEditedEntity().getPhotos().size() > 0) {
            index = ++index < photoSize ? index : 0;
            updateTypeImage();
        }
    }

    @Install(to = "currentAdvDriverDl", target = Target.DATA_LOADER)
    private AdvertisementDriver currentAdvDriverDlLoadDelegate(LoadContext<AdvertisementDriver> loadContext){

        List<AdvertisementDriver> advDrivers = dataManager.load(AdvertisementDriver.class).query(loadContext.getQuery().getQueryString()).setParameters(loadContext.getQuery().getParameters()).list();

        if(advDrivers.size() == 1)
            return advDrivers.get(0);

        return null;
    }
}