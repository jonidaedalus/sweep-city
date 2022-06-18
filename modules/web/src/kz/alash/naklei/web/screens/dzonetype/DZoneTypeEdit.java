package kz.alash.naklei.web.screens.dzonetype;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.Notifications.Position;
import com.haulmont.cuba.gui.components.Form;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.Validatable;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.wizard.gui.components.Wizard;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardCancelClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardFinishClickEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardTabChangeEvent;
import de.diedavids.cuba.wizard.gui.components.Wizard.WizardTabPreChangeEvent;
import kz.alash.naklei.entity.dict.DTraffic;
import kz.alash.naklei.entity.dict.DZoneType;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UiController("naklei_DZoneType.edit")
@UiDescriptor("d-zone-type-edit.xml")
@EditedEntityContainer("dZoneTypeDc")
@LoadDataBeforeShow
public class DZoneTypeEdit extends StandardEditor<DZoneType> {
    @Inject
    protected Wizard wizard;
    @Inject
    protected Notifications notifications;
    @Inject
    protected DataManager dataManager;
    @Inject
    private Form step1Form;
    @Inject
    private Metadata metadata;
    @Inject
    private CollectionContainer<DTraffic> trafficsDc;
    @Inject
    private InstanceContainer<DZoneType> dZoneTypeDc;
    @Inject
    private TextField<BigDecimal> coveragePerHourField;
    @Inject
    private TextField<BigDecimal> coveragePerHourWeekendField;

    private void createTraffic() {
        List<DTraffic> traffics = new ArrayList<>();

        for(int i = 1; i<=24;i++){
            DTraffic traffic = getScreenData().getDataContext().merge(metadata.create(DTraffic.class));
            traffic.setHour(i);
            traffic.setHourString((i-1) + " - " + i);
            traffic.setZoneType(getEditedEntity());
            traffic.setValue(1);
            traffic.setAverageSpeed(BigDecimal.valueOf(45));
            traffic.setAverageSpeedWeekend(BigDecimal.valueOf(45));
            traffic.setValueWeekends(1);

            traffics.add(traffic);
        }
        trafficsDc.setItems(traffics);
    }

    @Subscribe("wizard")
    protected void onCancelWizardClick(WizardCancelClickEvent event) {
        close(WINDOW_CLOSE_ACTION);
    }

    @Subscribe("wizard")
    protected void onWizardStepPreChangeEvent(WizardTabPreChangeEvent event) {
        if(event.getOldTab().getName().equals("step1")){
            List<Validatable> fields = new ArrayList<>();
            step1Form.getComponents().forEach(component -> {
                if(component instanceof Validatable)
                    fields.add((Validatable)component);
            });

            if(!getWindow().validate(fields)){
                event.preventTabChange();
                notifications.create(NotificationType.ERROR)
                        .withPosition(Position.BOTTOM_RIGHT)
                        .withCaption("Заполните обязательные поля")
                        .show();
            }else{
                //Если все ОК
                if(getEditedEntity().getTraffics() == null || getEditedEntity().getTraffics().size() == 0){
                    createTraffic();
                }

                removeDuplicates();
                calculateCoveragePerHour();
            }
        }
    }

    private void removeDuplicates() {
        List<DTraffic> traffics = trafficsDc.getItems();
        for (int i = 0; i<24; i++){
            int finalI = i + 1;

            List<DTraffic> trafficsWithFinalI = traffics.stream().filter(traffic -> traffic.getHour().equals(finalI)).collect(Collectors.toList());

            if(trafficsWithFinalI.size()  > 1 ){
                for(int j = 1; j < trafficsWithFinalI.size(); j++){
                    getScreenData().getDataContext().remove(trafficsWithFinalI.get(j));
                }
            }
        }
    }

    @Subscribe
    public void onBeforeCommitChanges(BeforeCommitChangesEvent event) {
        event.getDataContext();
    }

    
    private void calculateCoveragePerHour() {
        DZoneType zoneType = dZoneTypeDc.getItem();

        BigDecimal totalCoverage = zoneType.getCoverage();
        BigDecimal totalCoverageWeekend = zoneType.getCoverageWeekend();
        BigDecimal totalTraffic = trafficsDc.getItems().stream().map(traffic -> BigDecimal.valueOf(traffic.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTrafficWeekend = trafficsDc.getItems().stream().map(traffic -> BigDecimal.valueOf(traffic.getValueWeekends())).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal coveragePerHour = totalCoverage.divide(totalTraffic, RoundingMode.UP);
        BigDecimal coveragePerHourWeekend = totalCoverageWeekend.divide(totalTrafficWeekend, RoundingMode.UP);

        if(coveragePerHourField.getValue() == null || !coveragePerHourField.getValue().equals(coveragePerHour))
            coveragePerHourField.setValue(coveragePerHour);

        if(coveragePerHourWeekendField.getValue() == null || !coveragePerHourWeekendField.getValue().equals(coveragePerHourWeekend))
            coveragePerHourWeekendField.setValue(coveragePerHourWeekend);
    }

    @Subscribe("wizard")
    protected void onWizardStepChangeEvent(WizardTabChangeEvent event) {
        notifications.create(NotificationType.TRAY)
                .withCaption("Tab has changed2")
                .show();
    }

    @Subscribe("wizard")
    protected void onFinishWizardClick(WizardFinishClickEvent event) {
        getScreenData().getDataContext().commit();
        notifications.create(NotificationType.TRAY)
                .withCaption("Wizard finished")
                .show();
    }

    @Subscribe(id = "trafficsDc", target = Target.DATA_CONTAINER)
    public void onTrafficsDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<DTraffic> event) {
        if(event.getProperty().equals("value")
                || event.getProperty().equals("valueWeekends")){
            calculateCoveragePerHour();
        }

        if(event.getProperty().equals("averageSpeed") || event.getProperty().equals("value")){
            setCoveragePerKm(coveragePerHourField.getValue());
        }

        if(event.getProperty().equals("averageSpeedWeekend") || event.getProperty().equals("valueWeekend")){
            setCoveragePerKmWeekend(coveragePerHourWeekendField.getValue());
        }
    }

    @Subscribe("coveragePerHourField")
    public void onCoveragePerHourFieldValueChange(HasValue.ValueChangeEvent<BigDecimal> event) {
        if(event.getValue() != null && trafficsDc.getItems().size() > 0)
            setCoveragePerKm(event.getValue());
    }

    private void setCoveragePerKm(BigDecimal coveragePerHour) {
        trafficsDc.getItems()
                .forEach(traffic -> {
                    BigDecimal coveragePerKm = coveragePerHour.multiply(BigDecimal.valueOf(traffic.getValue())).divide(traffic.getAverageSpeed(), 2, RoundingMode.HALF_UP);

                    if(traffic.getCoveragePerKm() == null || !traffic.getCoveragePerKm().equals(coveragePerKm))
                        traffic.setCoveragePerKm(coveragePerKm);
                });
    }

    @Subscribe("coveragePerHourWeekendField")
    public void onCoveragePerHourWeekendFieldValueChange(HasValue.ValueChangeEvent<BigDecimal> event) {
        if(event.getValue() != null && trafficsDc.getItems().size() > 0)
            setCoveragePerKmWeekend(event.getValue());
    }

    private void setCoveragePerKmWeekend(BigDecimal coveragePerHourWeekend) {
        trafficsDc.getItems()
                .forEach(traffic -> {
                    BigDecimal coveragePerKmWeekend = coveragePerHourWeekend.multiply(BigDecimal.valueOf(traffic.getValueWeekends())).divide(traffic.getAverageSpeedWeekend(), 2, RoundingMode.HALF_UP);
                    if(traffic.getCoveragePerKmWeekend() == null || !traffic.getCoveragePerKmWeekend().equals(coveragePerKmWeekend))
                        traffic.setCoveragePerKmWeekend(coveragePerKmWeekend);
                });
    }
}