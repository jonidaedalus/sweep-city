package kz.alash.naklei.web.screens.advertisement;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.AdvPurpose;
import kz.alash.naklei.entity.Advertisement;
import kz.alash.naklei.entity.AdvertisementDriver;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static kz.alash.naklei.ConstantsRole.ADVERTISER_EMPLOYEE;

@UiController("naklei_Advertisement.browse")
@UiDescriptor("advertisement-browse.xml")
@LookupComponent("advertisementsTable")
@LoadDataBeforeShow
public class AdvertisementBrowse extends StandardLookup<Advertisement> {
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private Filter filter;
    @Inject
    private DataManager dataManager;
    @Inject
    private UiComponents uiComponents;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        if(userSessionSource.getUserSession().getRoles().contains(ADVERTISER_EMPLOYEE)){
            filter.setVisible(false);
        }
    }

    @Install(to = "advertisementsTable.carAmount", subject = "columnGenerator")
    private Component advertisementsTableCarAmountColumnGenerator(Advertisement advertisement) {
        int carAmount = 0;
        List<AdvertisementDriver> advertisementDrivers = dataManager.load(AdvertisementDriver.class)
                .query("select d from naklei_AdvertisementDriver d where d.status not in ('REJECTED', 'FINISHED') and d.purpose in :purposes")
                .parameter("purposes", advertisement.getPurposes())
                .list();

        carAmount = advertisementDrivers.size();

        TextField<Integer> textField = uiComponents.create(TextField.TYPE_INTEGER);

        textField.setEditable(false);
        textField.setStyleName("borderless");
        textField.setValue(carAmount);

        return textField;
    }

    @Install(to = "advertisementsTable.budget", subject = "columnGenerator")
    private Component advertisementsTableBudgetColumnGenerator(Advertisement advertisement) {
        BigDecimal budget;

        TextField<BigDecimal> textField = uiComponents.create(TextField.TYPE_BIGDECIMAL);

        budget = advertisement.getPurposes().stream().map(AdvPurpose::getBudget).reduce(BigDecimal.ZERO, BigDecimal::add);

        textField.setEditable(false);
        textField.setStyleName("borderless");
        textField.setValue(budget);

        return textField;
    }

}