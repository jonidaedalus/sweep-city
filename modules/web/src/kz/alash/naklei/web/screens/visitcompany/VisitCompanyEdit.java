package kz.alash.naklei.web.screens.visitcompany;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.KeyValueCollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.visit.VisitCompany;

import javax.inject.Inject;
import java.time.DayOfWeek;
import java.util.*;

@UiController("naklei_VisitCompany.edit")
@UiDescriptor("visit-company-edit.xml")
@EditedEntityContainer("visitCompanyDc")
@LoadDataBeforeShow
public class VisitCompanyEdit extends StandardEditor<VisitCompany> {
    private Map<String, Integer> weekends;
    @Inject
    private OptionsGroup weekendsOptionGroup;
    @Inject
    private HBoxLayout satWorkTimeHbox;
    @Inject
    private HBoxLayout sunWorkTimeHbox;

    @Subscribe
    public void onInit(InitEvent event) {
        initDayOfWeekCollection();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        LinkedHashSet<Integer> weekendsOptionGroupValue = (LinkedHashSet<Integer>)weekendsOptionGroup.getValue();

        if(getEditedEntity().getSatWorkFrom() != null)
            weekendsOptionGroupValue.add(6);
        if(getEditedEntity().getSunWorkFrom() != null)
            weekendsOptionGroupValue.add(7);

        weekendsOptionGroup.setValue(weekendsOptionGroupValue);
    }

    private void initDayOfWeekCollection() {
        weekends = new LinkedHashMap<>();
        weekends.put("Cуббота", 6);
        weekends.put("Воскресенье", 7);
        weekendsOptionGroup.setOptionsMap(weekends);
    }

    @Subscribe("weekendsOptionGroup")
    public void onWeekendsOptionGroupValueChange(HasValue.ValueChangeEvent<LinkedHashSet<Integer>> event) {
        LinkedHashSet<Integer> values = event.getValue();
        if(values.size() > 0){
            weekends.forEach((s, dayOfWeek) -> {
                if(!values.contains(dayOfWeek)){
                  if(dayOfWeek == 6){
                      getEditedEntity().setSatWorkFrom(null);
                      getEditedEntity().setSatWorkTo(null);
                      satWorkTimeHbox.setVisible(false);
                  }else{
                      getEditedEntity().setSunWorkFrom(null);
                      getEditedEntity().setSunWorkTo(null);
                      sunWorkTimeHbox.setVisible(false);
                  }
                }else{
                    if(dayOfWeek == 6)
                        satWorkTimeHbox.setVisible(true);
                    else
                        sunWorkTimeHbox.setVisible(true);
                }
            });
        }else{
            //почистить время для выходных дней
            getEditedEntity().setSatWorkFrom(null);
            getEditedEntity().setSatWorkTo(null);
            getEditedEntity().setSunWorkFrom(null);
            getEditedEntity().setSunWorkTo(null);
            sunWorkTimeHbox.setVisible(false);
            satWorkTimeHbox.setVisible(false);
        }
    }
}