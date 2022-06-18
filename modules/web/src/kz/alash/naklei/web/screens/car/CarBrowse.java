package kz.alash.naklei.web.screens.car;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ContentMode;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction;
import com.haulmont.cuba.gui.screen.*;
import com.vaadin.event.MouseEvents;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Car;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.ExtUser;
import kz.alash.naklei.entity.dict.EModerationStatus;
import kz.alash.naklei.entity.dict.EModerationType;
import kz.alash.naklei.entity.moderation.Moderation;
import kz.alash.naklei.service.EsbFirebaseService;
import kz.alash.naklei.web.screens.moderation.ModerationEdit;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@UiController("naklei_Car.browse")
@UiDescriptor("car-browse.xml")
@LookupComponent("carsTable")
@LoadDataBeforeShow
public class CarBrowse extends StandardLookup<Car> {
    @Inject
    private GroupTable<Car> carsTable;
    @Inject
    private Button sendToModeration;
    @Inject
    private Screens screens;
    @Inject
    private DataManager dataManager;
    @Inject
    private EsbFirebaseService esbFirebaseService;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Notifications notifications;

    @Install(to = "carsTable", subject = "lookupSelectHandler")
    protected void carsTableLookupSelectHandler(Collection<Car> cars) {
        Car selectedCar = cars.stream().findFirst().orElse(null);
        if(selectedCar != null){
            CarEdit carEdit = screens.create(CarEdit.class);
            carEdit.setEntityToEdit(selectedCar);
            carEdit.setReadOnly(true);
            carEdit.setShowSaveNotification(true);
            carEdit.show();
        }
    }

    @Subscribe("carsTable")
    public void onCarsTableSelection(Table.SelectionEvent<Car> event) {
        Set<Car> selectedCars = event.getSelected();
        sendToModeration.setEnabled(selectedCars.size() == 1);
    }

    @Subscribe("sendToModeration")
    public void onSendToModerationClick(Button.ClickEvent event) {
        Car car = dataManager.reload(Objects.requireNonNull(carsTable.getSingleSelected()), "car-edit");
        ModerationEdit moderationScreen = screens.create(ModerationEdit.class);
        Moderation moderation = dataManager.create(Moderation.class);

        Driver driver = dataManager.reload(car.getDriver(), "driver-edit");
        ExtUser user = driver.getUser();
        AdvertisementDriver advertisementDriver = driver.getAdvertisementDrivers()
                .stream()
                .filter(ad -> ad.getEndDate() == null)
                .findFirst()
                .orElse(null);
        if (advertisementDriver == null) {
            notifications.create().withCaption("У данного водителя нет активной кампании").show();
            return;
        }
        moderation.setStatus(EModerationStatus.IN_WORK);
        moderation.setType(EModerationType.VERIFY);
        moderation.setAdvertisementDriver(advertisementDriver);
        dataManager.commit(moderation);

        moderationScreen.setEntityToEdit(moderation);
        esbFirebaseService.sendPushMessage("Проверка",
                "Пожалуйста, отправьте фотографии на плановую проверку",
                user.getFcmToken(), driver, advertisementDriver.getPurpose().getAdvertisement(), moderation, null);
        moderationScreen.show();
    }

    @Subscribe("createPush")
    public void onCreatePushClick(Button.ClickEvent event) {
        dialogs.createInputDialog(this)
                .withCaption("Enter the push message")
                .withParameters(
                        InputParameter.stringParameter("title").withCaption("Title"),
                        InputParameter.stringParameter("message").withCaption("Message")
                )
                .withActions(
                        InputDialogAction.action("confirm")
                                .withCaption("Confirm")
                                .withPrimary(true)
                                .withHandler(actionEvent -> {
                                    InputDialog dialog = actionEvent.getInputDialog();
                                    String title = dialog.getValue("title");
                                    String message = dialog.getValue("message");
                                    dialog.closeWithDefaultAction();

                                    carsTable.getSelected().forEach(car ->
                                            esbFirebaseService.sendPushMessage(title, message, car.getDriver().getUser().getFcmToken(), car.getDriver(), null, null, null));

                                    notifications.create()
                                            .withCaption("Entered Values")
                                            .withDescription("<strong>Name:</strong> " + message)
                                            .withContentMode(ContentMode.HTML)
                                            .show();
                                }),
                        InputDialogAction.action("refuse")
                                .withCaption("Refuse")
                                .withValidationRequired(false)
                                .withHandler(actionEvent ->
                                        actionEvent.getInputDialog().closeWithDefaultAction())
                )
                .show();

    }



}