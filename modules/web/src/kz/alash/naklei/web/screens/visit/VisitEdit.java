package kz.alash.naklei.web.screens.visit;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogActions;
import com.haulmont.cuba.gui.app.core.inputdialog.DialogOutcome;
import com.haulmont.cuba.gui.app.core.inputdialog.InputParameter;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.export.ExportFormat;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.AdvertisementDriver;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.entity.dict.EVisitCancelReason;
import kz.alash.naklei.entity.dict.EVisitStatus;
import kz.alash.naklei.entity.visit.Visit;
import kz.alash.naklei.service.EsbFirebaseService;
import kz.alash.naklei.service.esb.dto.firebase.PushRequest;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UiController("naklei_Visit.edit")
@UiDescriptor("visit-edit.xml")
@EditedEntityContainer("visitDc")
@LoadDataBeforeShow
@Route(value = "visits/edit", parentPrefix = "visits")
public class VisitEdit extends StandardEditor<Visit> {
//    @Inject
//    private Image stickerImage;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private InstanceContainer<Visit> visitDc;
    @Inject
    private Dialogs dialogs;
    @Inject
    private UiComponents uiComponents;
    @Inject
    private Label<String> cancelReasonEmptyLabel;
    @Inject
    private TextArea<String> commentField;
    @Inject
    private TextField<EVisitCancelReason> cancelReasonField;
    @Inject
    private EsbFirebaseService esbFirebaseService;
    @Inject
    private ScreenBuilders screenBuilders;
    @Inject
    private Metadata metadata;
    @Inject
    private EntityStates entityStates;
    @Inject
    private Form fieldGroup;
    @Inject
    private DateField<LocalDateTime> visitEndField;
    @Inject
    private DataManager dataManager;
    @Inject
    private Button saveActionBox;
    @Inject
    private Button approveBtn;
    @Inject
    private Button rejectBtn;
    @Inject
    private DateField<LocalDateTime> visitStartField;
    @Inject
    private TextField<LocalDateTime> visitStartReadOnlyField;
    @Inject
    private TextField<LocalDateTime> visitEndReadOnlyField;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        if(entityStates.isNew(getEditedEntity())){
            fieldGroup.setEditable(true);
            visitStartField.setVisible(true);
            visitStartReadOnlyField.setVisible(false);
            visitEndField.setVisible(true);
            visitEndReadOnlyField.setVisible(false);
            visitDc.getItem().setStatus(EVisitStatus.PENDING);

            getWindow().setCaption("Новый визит");
        }

        saveActionBox.setVisible(entityStates.isNew(getEditedEntity()));

//        updateTypeImage();

        if(getEditedEntity().getStatus() != null ){
            if(getEditedEntity().getStatus().equals(EVisitStatus.CANCELED)){
                cancelReasonEmptyLabel.setVisible(true);
                cancelReasonField.setVisible(true);
                commentField.setVisible(true);
            }

            if(!getEditedEntity().getStatus().equals(EVisitStatus.PENDING) || entityStates.isNew(getEditedEntity())){
                approveBtn.setVisible(false);
                rejectBtn.setVisible(false);
            }
        }
    }

//    private void updateTypeImage() {
//        if (getEditedEntity().getAdvertisementDriver().getPurpose().getSticker() != null)
//            stickerImage.setSource(FileDescriptorResource.class)
//                    .setFileDescriptor(getEditedEntity().getAdvertisementDriver().getPurpose().getSticker());
//    }

    public void downloadSticker() {
        if (getEditedEntity().getAdvertisementDriver().getPurpose().getSticker() != null)
            exportDisplay.show(getEditedEntity().getAdvertisementDriver().getPurpose().getSticker(), ExportFormat.OCTET_STREAM);
    }

    public void changeStatusToDone() {
        changeStatus(EVisitStatus.DONE,null, null,null);
    }

    private void changeStatus(EVisitStatus status, EVisitCancelReason reason, String comment, Visit newVisit){
        visitDc.getItem().setStatus(status);
        visitDc.getItem().setCancelReason(reason);
        visitDc.getItem().setComment(comment);
        getScreenData().getDataContext().commit();
        sendPush(status, reason,newVisit);
        close(WINDOW_COMMIT_AND_CLOSE_ACTION);
    }

    private void sendPush(EVisitStatus status, EVisitCancelReason reason, Visit newVisit){
        //Отправить push
        PushRequest request = new PushRequest();
        request.setTo(getEditedEntity().getAdvertisementDriver().getDriver().getUser().getFcmToken());
        PushRequest.Notification notification = new PushRequest.Notification();

        switch (status){
            case DONE:
                notification.setTitle("Проверка");
                notification.setBody("Отлично, теперь вам необходимо пройти проверку.");
                break;
            case CANCELED:
                if(!reason.equals(EVisitCancelReason.REVISIT)){
                    notification.setTitle("Отмена участия");
                    notification.setBody("Нам очень жаль, что вы не смогли явиться на оклейку. Если вы передумаете, то сможете подать заявку еще раз если останутся свободные места.");
                }else{
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    newVisit = dataManager.reload(newVisit,"_local");
                    notification.setTitle("Перенос записи");
                    //todo переместить в какойнить класс который хранит даты константы и сделать их статик
                    notification.setBody("Вас перезаписали на" + newVisit.getVisitStart().format(formatter) + "\n" +
                            "Ждем вас на оклейку автомобиля по адресу: Сатпаева 22а. Пожалуйста не опаздывайте.");
                }
                break;
        }
        request.setNotification(notification);
        esbFirebaseService.sendPushMessage(request, getEditedEntity().getAdvertisementDriver().getDriver(), getEditedEntity().getAdvertisementDriver().getPurpose().getAdvertisement(), null, newVisit);
    }

    public void changeStatusToCancel() {
        dialogs.createInputDialog(this)
                .withCaption("Укажите причину и комментрий отмены записи")
                .withParameters(
                        InputParameter.enumParameter("reason", EVisitCancelReason.class)
                                .withRequired(true)
                                .withCaption("Причина"),
                        InputParameter.stringParameter("comment")
                                .withField(() -> {
                                    TextArea<String> textArea = uiComponents.create(TextArea.NAME);
                                    textArea.setWidthFull();
                                    textArea.setRequired(false);
                                    textArea.setCaption("Комментарии");
                                    return textArea;
                                })
                )
                .withActions(DialogActions.OK_CANCEL)
                .withValidator(context -> {
                    String comment = context.getValue("comment");
                    EVisitCancelReason reason = context.getValue("reason");
                    if(reason!= null && reason.equals(EVisitCancelReason.OTHER) && (comment == null || comment.isEmpty())){
                        return ValidationErrors.of("Необходимо ввести комментарий");
                    }
                    return ValidationErrors.none();
                })
                .withCloseListener(closeEvent -> {
                    if (closeEvent.closedWith(DialogOutcome.OK)) {
                        EVisitCancelReason reason = closeEvent.getValue("reason");
                        String comment = closeEvent.getValue("comment");

//                        Если причина отмены Неявка или Другое, тогда отвязываем от рекл. комп
                        if(reason!= null && reason.equals(EVisitCancelReason.REVISIT))
                            showDialogForRevisit(comment);
                        else{
                            closeAdvDriverContract();
                            changeStatus(EVisitStatus.CANCELED, reason, comment, null);
                        }
                    }
                }).show();
    }

    private void showDialogForRevisit(String comment) {
        Visit currentVisit = visitDc.getItem();
        Visit newVisit = metadata.create(Visit.class);

        newVisit.setAdvertisementDriver(currentVisit.getAdvertisementDriver());
        newVisit.setAssignedUser(currentVisit.getAssignedUser());
        newVisit.setType(currentVisit.getType());
        newVisit.setDescription("Перезапись на оклейку");
        //newVisit.setStatus(EVisitStatus.PENDING);

        VisitEdit editScreen = screenBuilders.editor(Visit.class,this)
                .withScreenClass(VisitEdit.class)
                .editEntity(newVisit)
                .build();

        editScreen.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                currentVisit.setRevisited(true);
                changeStatus(EVisitStatus.CANCELED, EVisitCancelReason.REVISIT, comment, newVisit);
            }
        });

        editScreen.show();
    }

    private void closeAdvDriverContract() {
        AdvertisementDriver advDriver = visitDc.getItem().getAdvertisementDriver();
        advDriver.setStatus(EAdvDriverStatus.REJECTED);
        advDriver.setEndDate(new Date());
    }

    @Subscribe("visitStartField")
    public void onVisitStartFieldValueChange(HasValue.ValueChangeEvent<LocalDateTime> event) {
        LocalDateTime value = event.getValue();
        if(value != null){
            //todo pastingTime
            Integer pastingTime = visitDc.getItem().getAdvertisementDriver().getPurpose().getPastingTime();
            if(pastingTime == null){
                pastingTime = 60;
            }
            visitEndField.setValue(value.plusMinutes(pastingTime));
        }
    }
}