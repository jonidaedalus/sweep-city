package kz.alash.naklei.web.screens.payout;

import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.LinkButton;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.PayOut;
import kz.alash.naklei.service.TransactionService;
import kz.alash.naklei.web.screens.car.CarEdit;

import javax.inject.Inject;
import java.math.BigDecimal;

@UiController("naklei_PayOut.edit")
@UiDescriptor("pay-out-edit.xml")
@EditedEntityContainer("payOutDc")
@LoadDataBeforeShow
public class PayOutEdit extends StandardEditor<PayOut> {
    @Inject
    private TextField<BigDecimal> totalField;
    @Inject
    private Button approveBtn;
    @Inject
    private Screens screens;
    @Inject
    private LinkButton carCardLink;
    @Inject
    private TransactionService transactionService;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        totalField.setValue(getEditedEntity().getSum().add(getEditedEntity().getSum().multiply(BigDecimal.valueOf(getEditedEntity().getPercent()))));
        if(getEditedEntity().getStatus().equals("Выплачено"))
            approveBtn.setVisible(false);

        carCardLink.setCaption(getEditedEntity().getAdvertisementDriver().getDriver().getUser().getName());
    }
    
    @Subscribe("approveBtn")
    public void onApproveBtnClick(Button.ClickEvent event) {
        getEditedEntity().setStatus("Выплачено");
        getScreenData().getDataContext().merge(
                transactionService.createTransaction(
                        getEditedEntity(), getEditedEntity().getSum()).getCommitInstances()
        );
        closeWithCommit();
    }

    @Subscribe("carCardLink")
    public void onCarCardLinkClick(Button.ClickEvent event) {
        CarEdit carEdit = screens.create(CarEdit.class);
        carEdit.setEntityToEdit(getEditedEntity().getAdvertisementDriver().getDriver().getCar());
        carEdit.show();
        
    }
}