package kz.alash.naklei.web.screens.advpurpose;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.AdvPurpose;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Car;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.entity.dict.car.DColor;
import kz.alash.naklei.entity.dict.car.DModel;
import kz.alash.naklei.web.screens.car.CarEdit;

import javax.inject.Inject;
import java.util.List;

import static kz.alash.naklei.ConstantsRole.ADVERTISER_EMPLOYEE;

@UiController("naklei_AdvPuproseShowFragment")
@UiDescriptor("adv-puprose-show-fragment.xml")
public class AdvPuproseShowFragment extends ScreenFragment {
    @Inject
    private DataManager dataManager;
    @Inject
    private InstanceContainer<AdvPurpose> advPurposeDc;
    @Inject
    private CollectionLoader<AdvertisementDriver> purposeDriversDl;
    @Inject
    private Label<String> allCarColor;
    @Inject
    private Label<List<DColor>> carColorLabel;
    @Inject
    private Label<String> allCarModel;
    @Inject
    private Label<List<DModel>> carModelLabel;
    @Inject
    private CollectionContainer<AdvertisementDriver> purposeDriversDc;
    @Inject
    private Label<String> carAmountLabel;
    @Inject
    private Table<AdvertisementDriver> purposeDriversTable;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private UserSessionSource userSessionSource;


    public void setAdvPurposeParam(AdvPurpose advPurpose){
        if(!PersistenceHelper.isNew(advPurpose))
            advPurpose = dataManager.reload(advPurpose, "advPurpose-edit");
        advPurposeDc.setItem(advPurpose);

        purposeDriversDl.setParameter("purpose", advPurpose);
        changeAdvertiserScreen();
        purposeDriversDl.load();


        carColorLabel.setVisible(advPurpose.getCarColor() != null && advPurpose.getCarColor().size() > 0);
        allCarColor.setVisible(advPurpose.getCarColor() != null && advPurpose.getCarColor().size() == 0);
        carModelLabel.setVisible(advPurpose.getCarModel() != null && advPurpose.getCarModel().size() > 0);
        allCarModel.setVisible(advPurpose.getCarModel() != null && advPurpose.getCarModel().size() == 0);

        String carAmount = purposeDriversDc.getItems().stream()
                .filter(purposeDriver -> !purposeDriver.getStatus().equals(EAdvDriverStatus.REJECTED)
                        && !purposeDriver.getStatus().equals(EAdvDriverStatus.BLOCKED)
                        && !purposeDriver.getStatus().equals(EAdvDriverStatus.FINISHED))
                .count() + "/" + advPurpose.getCarAmount();
        carAmountLabel.setValue(carAmount);
    }

    private void changeAdvertiserScreen() {
        if (isAdvertiser()) {
            purposeDriversTable.getColumn("driver").setValueProvider(advertisementDriver -> advertisementDriver.getDriver().getUser().getName());
            purposeDriversTable.getActionNN("view").setVisible(false);
        }
    }

    private boolean isAdvertiser() {
        return userSessionSource.getUserSession().getRoles().contains(ADVERTISER_EMPLOYEE);
    }

    @Subscribe("purposeDriversTable.view")
    public void onPurposeDriversTableView(Action.ActionPerformedEvent event) {
        if (purposeDriversTable.getSingleSelected() == null) return;
        if (isAdvertiser()) return;
        CarEdit viewScreen =
                screenBuilders.editor(Car.class, this)
                        .withScreenClass(CarEdit.class)
                        .withLaunchMode(OpenMode.THIS_TAB)
                        .build();

        viewScreen.setEntityToEdit(purposeDriversTable.getSingleSelected().getDriver().getCar());
        viewScreen.setReadOnly(true);
        viewScreen.show();
    }
}