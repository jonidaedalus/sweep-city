package kz.alash.naklei.web.screens.dcoverage;

import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.data.value.ContainerValueSource;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.DCoverage;
import kz.alash.naklei.entity.dict.DZone;

import javax.inject.Inject;

@UiController("naklei_DCoverage.edit")
@UiDescriptor("d-coverage-edit.xml")
@EditedEntityContainer("dCoverageDc")
@LoadDataBeforeShow
public class DCoverageEdit extends StandardEditor<DCoverage> {

}