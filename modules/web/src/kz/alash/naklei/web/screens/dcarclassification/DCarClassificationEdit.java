package kz.alash.naklei.web.screens.dcarclassification;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.dict.car.DCarClassification;
import kz.alash.naklei.entity.dict.car.DModel;

import javax.inject.Inject;
import java.util.Objects;

@UiController("naklei_DCarClassification.edit")
@UiDescriptor("d-car-classification-edit.xml")
@EditedEntityContainer("dCarClassificationDc")
@LoadDataBeforeShow
public class DCarClassificationEdit extends StandardEditor<DCarClassification> {
    @Inject
    private TextField<String> mark;

    @Subscribe("modelField")
    public void onModelFieldValueChange(HasValue.ValueChangeEvent<DModel> event) {
        if (event != null)
            mark.setValue(Objects.requireNonNull(event.getValue()).getMark().getName());
    }
}