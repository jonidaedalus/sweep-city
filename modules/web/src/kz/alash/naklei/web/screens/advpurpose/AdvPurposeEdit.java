package kz.alash.naklei.web.screens.advpurpose;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import kz.alash.naklei.entity.AdvPurpose;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.Car;
import kz.alash.naklei.entity.dict.DPointCost;
import kz.alash.naklei.entity.dict.DStickerType;
import kz.alash.naklei.entity.dict.car.DClass;
import kz.alash.naklei.entity.dict.car.DColor;
import kz.alash.naklei.entity.dict.car.DModel;
import kz.alash.naklei.service.AdvertisementService;
import kz.alash.naklei.web.screens.car.CarEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;

@UiController("naklei_AdvPurpose.edit")
@UiDescriptor("adv-purpose-edit.xml")
@EditedEntityContainer("advPurposeDc")
public class AdvPurposeEdit extends StandardEditor<AdvPurpose> {
    private static final Logger log = LoggerFactory.getLogger(AdvPurposeEdit.class);

    private static final Date today = new Date();
    private long numberOfDaysBetweenStartAndEnd;
    private Date startAdvertisementDate = null;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Table<AdvertisementDriver> purposeDriversTable;
    @Inject
    private Image maketImage;
    @Inject
    private Button downloadMaketImageBtn;
    @Inject
    private Button clearMaketImageBtn;
    @Inject
    private FileUploadField uploadMaketField;

    public void setNumberOfDaysBetweenStartAndEnd(long numberOfDaysBetweenStartAndEnd){
        this.numberOfDaysBetweenStartAndEnd = numberOfDaysBetweenStartAndEnd;
    }

    public void setStartAdvertisementDate(Date startAdvertisementDate){
        this.startAdvertisementDate = startAdvertisementDate;
    }

    @Inject
    private TextField<BigDecimal> rewardAmountField;
    @Inject
    private CollectionLoader<DStickerType> stickerTypesDl;
    @Inject
    private CollectionLoader<DClass> carClassesDl;
    @Inject
    private CollectionLoader<DColor> colorsDl;
    @Inject
    private Button commitAndCloseBtn;
    @Inject
    private HBoxLayout stickerImageBtnsHbox;
    @Inject
    private Form form;
    @Inject
    private GroupBoxLayout carsGroupBox;
    @Inject
    private CollectionLoader<AdvertisementDriver> purposeDriverDl;
    @Inject
    private AdvertisementService advertisementService;
    @Inject
    private TextField<Date> pastingDateReadOnlyField;
    @Inject
    private DateField<Date> pastingDateField;
    @Inject
    private FileUploadField uploadField;
    @Inject
    private Button downloadImageBtn;
    @Inject
    private Button clearImageBtn;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private DataManager dataManager;
    @Inject
    private Image image;
    @Inject
    private Label<Integer> carAmountLabel;
    @Inject
    private LookupField<DStickerType> stickerTypeField;
    @Inject
    private LookupField<DClass> classsField;
    @Inject
    private TextField<BigDecimal> budgetField;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private CollectionContainer<DPointCost> dPointCostDc;

    @Inject
    private InstanceContainer<AdvPurpose> advPurposeDc;
    @Inject
    private HBoxLayout carAmountHbox;
    @Inject
    private TokenList<DModel> modelTokenList;
    @Inject
    private TokenList<DColor> colorTokenList;
    @Inject
    private HBoxLayout allModelsLabel;
    @Inject
    private HBoxLayout allColorsLabel;
    @Inject
    private Button showModelTokenListBtn;
    @Inject
    private Button showColorTokenListBtn;
    @Inject
    private TextField<Integer> pastingTimeField;
    @Inject
    private TextField<BigDecimal> pastingCostField;
    @Inject
    private Notifications notifications;
    @Inject
    private CollectionLoader<DPointCost> dPointCostDl;

    @Subscribe
    public void onInit(InitEvent event) {
        uploadField.addFileUploadSucceedListener(uploadSucceedEvent -> {
            FileDescriptor fd = uploadField.getFileDescriptor();

            if(fd == null) return;

            try {
                fileUploadingAPI.putFileIntoStorage(uploadField.getFileId(), fd);
            } catch (FileStorageException e) {
                throw new RuntimeException("Error saving file to FileStorage", e);
            }
            getEditedEntity().setSticker(dataManager.commit(fd));
            displayImage();
        });

        uploadMaketField.addFileUploadSucceedListener(uploadSucceedEvent -> {
            FileDescriptor fd = uploadMaketField.getFileDescriptor();

            if(fd == null) return;

            try {
                fileUploadingAPI.putFileIntoStorage(uploadMaketField.getFileId(), fd);
            } catch (FileStorageException e) {
                throw new RuntimeException("Error saving file to FileStorage", e);
            }
            getEditedEntity().setMaket(dataManager.commit(fd));
            displayMaketImage();
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if(!PersistenceHelper.isNew(getEditedEntity())){
            AdvPurpose purpose = dataManager.reload(getEditedEntity(), "advPurpose-edit");
            purpose = getScreenData().getDataContext().merge(purpose);
            setEntityToEdit(purpose);
            advPurposeDc.setItem(purpose);
        }

        if(!isReadOnly()){
            dPointCostDl.load();
            stickerTypesDl.load();
            carClassesDl.load();
            colorsDl.load();

            setCarAmount();
        }else{
            commitAndCloseBtn.setVisible(false);
            stickerImageBtnsHbox.setVisible(false);
            showModelTokenListBtn.setVisible(false);
            showColorTokenListBtn.setVisible(false);
            carsGroupBox.setVisible(true);
            pastingDateReadOnlyField.setVisible(true);
            pastingDateField.setVisible(false);

            form.setCaptionPosition(Form.CaptionPosition.LEFT);
            form.getComponents().forEach(component -> component.setStyleName("borderless huge"));

            purposeDriverDl.setParameter("purpose", getEditedEntity());
            purposeDriverDl.load();
        }

        AdvPurpose purpuse = getEditedEntity();
        if(PersistenceHelper.isNew(purpuse)){
            getWindow().setCaption("Создать цель рекламной кампании");
            pastingTimeField.setValue(15);
            pastingCostField.setValue(BigDecimal.valueOf(3000));
        }

        if(purpuse.getCarAmount() != null && purpuse.getCarAmount() > 0)
            carAmountHbox.setVisible(true);

        if(purpuse.getCarModel() != null && purpuse.getCarModel().size() > 0)
            showModelTokenList();

        if(purpuse.getCarColor() != null && purpuse.getCarColor().size() > 0)
            showColorTokenList();

        if(purpuse.getRewardAmount() == null)
            rewardAmountField.setValue(BigDecimal.ZERO);

        advPurposeDc.setItem(purpuse);
        setEntityToEdit(purpuse);
    }

    @Subscribe(id = "advPurposeDc", target = Target.DATA_CONTAINER)
    public void onAdvPurposeDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<AdvPurpose> event) {
        if ("sticker".equals(event.getProperty()))
            updateImageButtons(event.getValue() != null);
        if(event.getProperty().equals("budget") ||
                event.getProperty().equals("stickerType") ||
                event.getProperty().equals("classs") ||
                event.getProperty().equals("pastingCost") ||
                event.getProperty().equals("rewardAmount")){
            setCarAmount();
        }
    }

    private void setCarAmount() {
        BigDecimal budget;
        DClass carClass;
        DStickerType stickerType;
        BigDecimal pastingCost;

        if(budgetField.getValue() == null || classsField.getValue() == null || stickerTypeField.getValue() == null || pastingCostField.getValue() == null)
            return;
        else{
            budget = budgetField.getValue();
            carClass = classsField.getValue();
            stickerType = stickerTypeField.getValue();
            pastingCost = pastingCostField.getValue();
        }

        Long carAmount = null;
        try {
            carAmount = advertisementService.calculateCarAmount(
                    budget,
                    carClass,
                    stickerType,
                    pastingCost,
                    rewardAmountField.getValue(),
                    numberOfDaysBetweenStartAndEnd);
        } catch (Exception e) {
            notifications
                    .create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription(e.getMessage())
                    .show();
            return;
        }

        if(carAmount == null){

            notifications
                    .create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription("Произошла ошибка при подсчете кол-ва машин")
                    .show();
            return;
        }

        carAmountLabel.setValue(Math.toIntExact(carAmount));
    }

    @Subscribe(target = Target.DATA_CONTEXT)
    public void onPostCommit(DataContext.PostCommitEvent event) {
        displayImage();
        displayMaketImage();
        updateImageButtons(getEditedEntity().getSticker() != null);
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        if(pastingDateField.getValue() == null || startAdvertisementDate == null || !(pastingDateField.getValue().after(today) && pastingDateField.getValue().before(startAdvertisementDate))){
            notifications
                    .create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withDescription("Дата оклейки не должна быть меньше текущей даты и больше даты начала рекл. кампании")
                    .show();
            event.preventCommit();
        }

        getEditedEntity().setCurrentBudget(getEditedEntity().getBudget());
    }
    
    

//    private void calculateCarsAmount(){
//        BigDecimal budget;
//        DClass carClass;
//        DStickerType stickerType;
//        BigDecimal pastingCost;
//
//        if(budgetField.getValue() == null || classsField.getValue() == null || stickerTypeField.getValue() == null || pastingCostField.getValue() == null)
//            return;
//        else{
//            budget = budgetField.getValue();
//            carClass = classsField.getValue();
//            stickerType = stickerTypeField.getValue();
//            pastingCost = pastingCostField.getValue();
//        }
//
//        DPointCost pointCost = dPointCostDc.getItems().stream()
//                .filter(dPointCost -> dPointCost.getCarClass().equals(carClass) && dPointCost.getStickerType().equals(stickerType))
//                .findFirst()
//                .orElse(null);
//
//        if(pointCost == null){
//            notifications
//                    .create()
//                    .withType(Notifications.NotificationType.ERROR)
//                    .withDescription("Не найдена цена за поинт с классом " +
//                            carClass.getName() +
//                            " и типом наклейки " + stickerType.getName())
//                    .show();
//            return;
//        }
//
//        //вычислим стоимость пути максимального поинта каждый день в заданный период(стоимость для рекламодателя)
//        long maxCostOfMaxPointForEveryDay = carClass.getMaxPointPerDay() * numberOfDaysBetweenStartAndEnd * pointCost.getAdvertiserCost().longValue();
//
//        long costForWash = numberOfDaysBetweenStartAndEnd/14 * 500;
//
//        long rewardAmount = 0;
//        if(rewardAmountField.getValue() != null)
//            rewardAmount = rewardAmountField.getValue().longValue();
//
//        int carAmount = (int) (budget.longValue()/(maxCostOfMaxPointForEveryDay + pastingCost.longValue() + costForWash + rewardAmount));
//        log.info("carAmount = " + carAmount);
//        log.info("budget = " + budget);
//        log.info("maxCostOfMaxPointForEveryDay = " + maxCostOfMaxPointForEveryDay);
//        log.info("pastingCost = " + pastingCost);
//        log.info("costForWash = " + costForWash);
//        log.info("rewardAmount = " + rewardAmount);
//
//        carAmountLabel.setValue(carAmount);
//    }

    private void displayImage() {
        if (getEditedEntity().getSticker() != null) {
            image.setSource(FileDescriptorResource.class).setFileDescriptor(getEditedEntity().getSticker());
            image.setVisible(true);
        } else {
            image.setVisible(false);
        }
    }

    private void displayMaketImage() {
        if (getEditedEntity().getMaket() != null) {
            maketImage.setSource(FileDescriptorResource.class).setFileDescriptor(getEditedEntity().getMaket());
            maketImage.setVisible(true);
        } else {
            maketImage.setVisible(false);
        }
    }

    private void updateImageButtons(boolean enable) {
        downloadImageBtn.setEnabled(enable);
        clearImageBtn.setEnabled(enable);
    }

    private void updateMaketImageButtons(boolean enable) {
        downloadMaketImageBtn.setEnabled(enable);
        clearMaketImageBtn.setEnabled(enable);
    }

    public void onDownloadImageBtnClick() {
        if (getEditedEntity().getSticker() != null)
            exportDisplay.show(getEditedEntity().getSticker(), ExportFormat.OCTET_STREAM);
    }

    public void onDownloadMaketImageBtnClick() {
        if (getEditedEntity().getMaket() != null)
            exportDisplay.show(getEditedEntity().getMaket(), ExportFormat.OCTET_STREAM);
    }

    public void onClearImageBtnClick() {
        getEditedEntity().setSticker(null);
        displayImage();
    }

    public void onClearMaketImageBtnClick() {
        getEditedEntity().setMaket(null);
        displayMaketImage();
    }

    public void showModelTokenList() {
        showModelTokenListBtn.setVisible(false);
        modelTokenList.setVisible(true);
        allModelsLabel.setVisible(false);
    }

    public void showColorTokenList() {
        showColorTokenListBtn.setVisible(false);
        colorTokenList.setVisible(true);
        allColorsLabel.setVisible(false);
    }

    @Subscribe("purposeDriversTable.view")
    public void onPurposeDriversTableView(Action.ActionPerformedEvent event) {
        if(purposeDriversTable.getSingleSelected() == null) return;

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