package kz.alash.naklei.web.screens.moderation;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.inputdialog.InputDialog;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.inputdialog.InputDialogAction;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.*;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.entity.dict.EModerationStatus;
import kz.alash.naklei.entity.dict.EModerationType;
import kz.alash.naklei.entity.dict.EPayOutStatus;
import kz.alash.naklei.entity.moderation.Moderation;
import kz.alash.naklei.service.AdvertisementService;
import kz.alash.naklei.service.EsbFirebaseService;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@UiController("naklei_Moderation.edit")
@UiDescriptor("moderation-edit.xml")
@EditedEntityContainer("moderationDc")
@LoadDataBeforeShow
public class ModerationEdit extends StandardEditor<Moderation> {
    @Inject
    private Image typeImage;

    private int index = 0;
    private int photoSize = 0;
    @Inject
    private Image carPhoto;
    //    @Inject
//    private Button statusBtn;
    @Inject
    private Notifications notifications;
    @Inject
    private EsbFirebaseService esbFirebaseService;
    @Inject
    private Dialogs dialogs;

    @Inject
    private Button approveBtn;
    @Inject
    private Button rejectBtn;
    //    @Inject
//    private TextField<String> tachometerDistance;
//    @Inject
//    private TextField<Integer> gpsDistance;
//    @Inject
//    private Button calculateButton;
//    @Inject
//    private VBoxLayout tachometerBox;
//    @Inject
//    private VBoxLayout photosBox;
//    @Inject
//    private VBoxLayout calculateBox;
    @Inject
    private HBoxLayout smallPhotoImgHBox;
    //    @Inject
//    private Screens screens;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private TextField<Double> totalRunField;
    @Inject
    private TextField<BigDecimal> balanceField;
    @Inject
    private HBoxLayout reasonHbox;
    @Inject
    private Label<String> reasonLabel;
    @Inject
    private Messages messages;
    @Inject
    private TextField<BigDecimal> sumToPayOutField;
    @Inject
    private Metadata metadata;
    @Inject
    private DataManager dataManager;
    @Inject
    private TextField<BigDecimal> driverBalanceField;
    @Inject
    private AdvertisementService advertisementService;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if (getEditedEntity().getType().equals(EModerationType.PAYOUT)) {
            sumToPayOutField.setVisible(true);
        }
        if (getEditedEntity().getType().equals(EModerationType.PAYOUT) || getEditedEntity().getType().equals(EModerationType.FINISH)) {
            driverBalanceField.setVisible(true);
        }
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        //updateBtn();
        Car car = getEditedEntity().getAdvertisementDriver().getDriver().getCar();
        List<FileDescriptor> carPhotos = car.getPhotos();
        if (carPhotos != null && carPhotos.size() > 0) {
            carPhoto.setSource(FileDescriptorResource.class).setFileDescriptor(carPhotos.get(index));
        }

        EModerationType type = getEditedEntity().getType();
        photoSize += getEditedEntity().getPhotos() != null ? getEditedEntity().getPhotos().size() : 0;

        for (int i = 0; i < photoSize; i++) {
            createSmallPhoto(i);
        }

        if ((EModerationType.START.equals(type) || EModerationType.FINISH.equals(type))) {
            FileDescriptor initialTachometerPhoto = getEditedEntity().getAdvertisementDriver().getInitialTachometerPhoto();
            FileDescriptor finalTachometerPhoto = getEditedEntity().getAdvertisementDriver().getFinalTachometerPhoto();

            if (initialTachometerPhoto != null) {
                photoSize++;
                createSmallPhoto(photoSize - 1);
            }

            if (finalTachometerPhoto != null) {
                photoSize++;
                createSmallPhoto(photoSize - 1);
            }
        }

        updateTypeImage();

        if (!EModerationStatus.IN_WORK.equals(getEditedEntity().getStatus()) && !EModerationStatus.IN_CHECK.equals(getEditedEntity().getStatus())) {
            updateModerationButtons(false);
            if (getEditedEntity().getStatus().equals(EModerationStatus.REJECTED) && getEditedEntity().getReason() != null) {
                reasonHbox.setVisible(true);
                ERejectModerationReason reason = ERejectModerationReason.fromId(getEditedEntity().getReason());

                String value = messages.getMessage(reason);

                if (reason.equals(ERejectModerationReason.OTHER))
                    value += "(" + getEditedEntity().getMessage() + ")";

                reasonLabel.setValue(value);
            }
        }

        if (getEditedEntity().getType().equals(EModerationType.PRECHECK)) {
            if (totalRunField.getValue() == null || totalRunField.getValue() == 0)
                totalRunField.setVisible(false);

            balanceField.setVisible(false);
        }
    }

    private void createSmallPhoto(int i) {
        VBoxLayout smallPhotoVBox = uiComponents.create(VBoxLayout.NAME);
        smallPhotoVBox.setStyleName("card");
        smallPhotoVBox.setWidth("100px");
        smallPhotoVBox.setHeight("100px");
        smallPhotoVBox.setMargin(true);

        HBoxLayout imageBox = uiComponents.create(HBoxLayout.NAME);
        smallPhotoVBox.add(imageBox);
        imageBox.setMargin(true);
        imageBox.setHeightFull();
        imageBox.setWidthFull();

        Image image = uiComponents.create(Image.NAME);
        image.setAlignment(Component.Alignment.MIDDLE_CENTER);
        imageBox.add(image);
        image.setId("image_" + i);
        image.setHeightFull();
        image.setWidthFull();
        image.setScaleMode(Image.ScaleMode.CONTAIN);

        if (getEditedEntity().getPhotos().size() > 0 && i < getEditedEntity().getPhotos().size()) {
            image.setSource(FileDescriptorResource.class)
                    .setFileDescriptor(getEditedEntity().getPhotos().get(i));
        } else {
            selectAdditionalPhoto(i, image);
        }
        smallPhotoVBox.addLayoutClickListener(layoutClickEvent -> {
            this.index = i;
            updateTypeImage();
        });
        smallPhotoImgHBox.add(smallPhotoVBox);
    }

    private void updateModerationButtons(Boolean val) {
        approveBtn.setVisible(val);
        rejectBtn.setVisible(val);
    }

    private void updateTypeImage() {
        if (photoSize > 0) {
            if (getEditedEntity().getPhotos() != null && getEditedEntity().getPhotos().size() > 0 && index < getEditedEntity().getPhotos().size())
                typeImage.setSource(FileDescriptorResource.class)
                        .setFileDescriptor(getEditedEntity().getPhotos().get(index));
            else {
                selectAdditionalPhoto(index, typeImage);
            }
        }
    }

    private void selectAdditionalPhoto(int index, Image typeImage) {
        FileDescriptor initialTachometerPhoto = getEditedEntity().getAdvertisementDriver().getInitialTachometerPhoto();
        FileDescriptor finalTachometerPhoto = getEditedEntity().getAdvertisementDriver().getFinalTachometerPhoto();
        if (getEditedEntity().getPhotos().size() > 0) {
            if (index - getEditedEntity().getPhotos().size() == 1 || initialTachometerPhoto == null) {
                typeImage.setSource(FileDescriptorResource.class)
                        .setFileDescriptor(finalTachometerPhoto);
            } else {
                typeImage.setSource(FileDescriptorResource.class)
                        .setFileDescriptor(initialTachometerPhoto);
            }
        } else {
            if (photoSize - index == 1 || initialTachometerPhoto == null) {
                typeImage.setSource(FileDescriptorResource.class)
                        .setFileDescriptor(finalTachometerPhoto);
            } else {
                typeImage.setSource(FileDescriptorResource.class)
                        .setFileDescriptor(initialTachometerPhoto);
            }
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

    @Subscribe("approveBtn")
    public void onApproveBtnClick(Button.ClickEvent event) {
        //updateModerationStatus();
        getEditedEntity().setStatus(EModerationStatus.ACCEPTED);

        setModerator();
        //updateModerationButtons(false);
        if (getEditedEntity().getType().equals(EModerationType.PAYOUT) || getEditedEntity().getType().equals(EModerationType.FINISH)) {
            DAppConf appConfig = dataManager.load(DAppConf.class).query("select e from naklei_DAppConf e")
                    .viewProperties("transactionPercent")
                    .firstResult(0)
                    .optional()
                    .orElse(null);

            if (appConfig == null || appConfig.getTransactionPercent() == null) {
                notifications.create().withCaption("Не найдены данные из бизнес конфигураций").show();
                return;
            }
            PayOut payOut = metadata.create(PayOut.class);
            payOut.setAdvertisementDriver(getEditedEntity().getAdvertisementDriver());
            payOut.setPercent(appConfig.getTransactionPercent());

            if (getEditedEntity().getType().equals(EModerationType.FINISH)) {
                getEditedEntity().getAdvertisementDriver().setStatus(EAdvDriverStatus.FINISHED);
                getEditedEntity().getAdvertisementDriver().setEndDate(new Date());
                payOut.setSum(getEditedEntity().getAdvertisementDriver().getDriver().getCurrentMoney());
            } else {
                payOut.setSum(getEditedEntity().getSum());
                getEditedEntity().getAdvertisementDriver().setStatus(EAdvDriverStatus.ACTIVE);
//                getEditedEntity().getAdvertisementDriver().setStartDate(new Date());
            }

            payOut.setStatus(EPayOutStatus.IN_WORK);
            getScreenData().getDataContext().merge(payOut);
        } else if (getEditedEntity().getType().equals(EModerationType.START)) {
            getEditedEntity().getAdvertisementDriver().setStatus(EAdvDriverStatus.ACTIVE);
            Date advertisementStartDate = getEditedEntity().getAdvertisementDriver().getPurpose().getAdvertisement().getStartDate();
            boolean stickedWithinPeriod = getEditedEntity().getCreateTs().before(advertisementStartDate);
            getEditedEntity().getAdvertisementDriver().setStickedWithinPeriod(stickedWithinPeriod);
            getEditedEntity().getAdvertisementDriver().setStartDate(new Date());
            getEditedEntity().getAdvertisementDriver().setIsSticked(true);
        }
        getEditedEntity().setMessage("Отличного дня :)");
        closeModeration();
    }

    @Subscribe("rejectBtn")
    public void onRejectBtnClick(Button.ClickEvent event) {
//        updateModerationStatus(EModerationStatus.REJECTED);

        dialogs.createInputDialog(this)
                .withCaption("Укажите причину")
                .withParameters(
                        InputParameter.enumParameter("reason", ERejectModerationReason.class)
                                .withCaption("Причина").withRequired(true),
                        InputParameter.stringParameter("comment").withCaption("Комментрарий")
                                .withRequired(true)
                )
                .withValidator(validationContext -> {
                    ERejectModerationReason reason = validationContext.getValue("reason");
                    if (reason != null && reason.equals(ERejectModerationReason.OTHER)) {
                        String comment = validationContext.getValue("comment");
                        if (comment == null || comment.isEmpty())
                            return ValidationErrors.of("Необходимо указать комментарий");
                    }

                    return ValidationErrors.none();
                })
                .withActions(
                        InputDialogAction.action("Отклонить")
                                .withCaption("Отклонить")
                                .withPrimary(true)
                                .withHandler(actionEvent -> {
                                    InputDialog dialog = actionEvent.getInputDialog();
                                    String message = dialog.getValue("comment");
                                    ERejectModerationReason reason = dialog.getValue("reason");
                                    getEditedEntity().setMessage(message);
                                    getEditedEntity().setReason(reason.getId());
                                    getEditedEntity().setStatus(EModerationStatus.REJECTED);

                                    if (getEditedEntity().getType().equals(EModerationType.PRECHECK)
                                            || getEditedEntity().getType().equals(EModerationType.START)) {
                                        getEditedEntity().getAdvertisementDriver().setStatus(EAdvDriverStatus.REJECTED);
                                        getEditedEntity().getAdvertisementDriver().setEndDate(new Date());
                                        advertisementService.deleteVisits(getEditedEntity().getAdvertisementDriver());
                                    }

                                    setModerator();
                                    dialog.closeWithDefaultAction();

                                    closeModeration();
                                })
                )
                .show();
    }

    private void closeModeration() {
        ExtUser user = getEditedEntity().getAdvertisementDriver().getDriver().getUser();
        getScreenData().getDataContext().commit();
        sendPush(user, getEditedEntity().getStatus(), getEditedEntity().getMessage());
        notifications.create(Notifications.NotificationType.HUMANIZED)
                .withDescription("Модерация обработана")
                .show();
        closeWithDefaultAction();
    }

    private void setModerator() {
        User user = AppBeans.get(UserSessionSource.class).getUserSession().getUser();
        getEditedEntity().setModerator(user);
    }

    private void sendPush(ExtUser user, EModerationStatus status, String message) {
        //todo изменить to на token устройства(его надо добавить пользователю)
        String token = user.getFcmToken();
        StringBuilder sb = new StringBuilder();
        sb.append("Проверка пройдена, статус: ")
                .append(messages.getMessage(status) + ". ")
                .append(message);

        esbFirebaseService.sendModerationPush(
                "Проверка",
                sb.toString(),
                token,
                getEditedEntity().getAdvertisementDriver().getDriver(),
                getEditedEntity().getAdvertisementDriver().getPurpose().getAdvertisement(),
                getEditedEntity(),
                null);
    }

//    @Subscribe("calculateButton")
//    public void onCalculateButtonClick(Button.ClickEvent event) {
//        BigInteger tachometerDifference = new BigInteger(tachometerDistance.getRawValue());
//        BigInteger gpsVal = new BigInteger(gpsDistance.getRawValue());
//        boolean checkCondition = (BigInteger.ONE.subtract( tachometerDifference.divide(gpsVal) ).doubleValue() < 0.1);
//        if (checkCondition) {
//            getEditedEntity().getAdvertisementDriver().setApprovedTotalRun(gpsVal.intValue());
//            notifications.create(Notifications.NotificationType.HUMANIZED)
//                    .withDescription("Все отлично")
//                    .show();
//        } else {
//            getEditedEntity().getAdvertisementDriver().setApprovedTotalRun(tachometerDifference.intValue());
//            notifications.create(Notifications.NotificationType.HUMANIZED)
//                    .withDescription("Не сходится, одобренный километраж - " + getEditedEntity().getAdvertisementDriver().getApprovedTotalRun())
//                    .show();
//        }
//        updateModerationStatus(EModerationStatus.ACCEPTED);
//    }
//
//    @Subscribe("typeImage")
//    public void onTypeImageClick(Image.ClickEvent event) {
//
//    }
//
//    @Subscribe("photosBox")
//    public void onPhotosBoxLayoutClick(LayoutClickNotifier.LayoutClickEvent event) {
//        notifications.create(Notifications.NotificationType.HUMANIZED)
//                .withDescription("TEMA TEMA")
//                .show();
//    }

}