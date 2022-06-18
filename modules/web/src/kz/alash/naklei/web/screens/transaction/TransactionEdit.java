package kz.alash.naklei.web.screens.transaction;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.Transaction;

@UiController("naklei_Transaction.edit")
@UiDescriptor("transaction-edit.xml")
@EditedEntityContainer("transactionDc")
@LoadDataBeforeShow
public class TransactionEdit extends StandardEditor<Transaction> {
}