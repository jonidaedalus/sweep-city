package kz.alash.naklei.web.screens.payout;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.PayOut;

@UiController("naklei_PayOut.browse")
@UiDescriptor("pay-out-browse.xml")
@LookupComponent("payOutsTable")
@LoadDataBeforeShow
public class PayOutBrowse extends StandardLookup<PayOut> {
}