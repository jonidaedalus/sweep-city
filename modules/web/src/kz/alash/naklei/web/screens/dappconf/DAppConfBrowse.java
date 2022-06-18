package kz.alash.naklei.web.screens.dappconf;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.DAppConf;

@UiController("naklei_DAppConf.browse")
@UiDescriptor("d-app-conf-browse.xml")
@LookupComponent("dAppConfsTable")
@LoadDataBeforeShow
public class DAppConfBrowse extends StandardLookup<DAppConf> {
}