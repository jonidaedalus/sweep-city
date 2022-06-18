package kz.alash.naklei.web.screens.dappconf;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.DAppConf;

@UiController("naklei_DAppConf.edit")
@UiDescriptor("d-app-conf-edit.xml")
@EditedEntityContainer("dAppConfDc")
@LoadDataBeforeShow
public class DAppConfEdit extends StandardEditor<DAppConf> {
}