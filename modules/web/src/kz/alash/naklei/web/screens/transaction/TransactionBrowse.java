package kz.alash.naklei.web.screens.transaction;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.Transaction;

@UiController("naklei_Transaction.browse")
@UiDescriptor("transaction-browse.xml")
@LookupComponent("transactionsTable")
@LoadDataBeforeShow
public class TransactionBrowse extends StandardLookup<Transaction> {
}