package kz.alash.naklei.web.screens.advertiser.statitics;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.*;
import kz.alash.naklei.entity.dict.DPointCost;
import kz.alash.naklei.entity.dict.EAdvDriverStatus;
import kz.alash.naklei.service.AdvertisementService;
import kz.alash.naklei.service.utils.DateUtility;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@UiController("naklei_Advertiser.read")
@UiDescriptor("advertiser-statistics.xml")
@EditedEntityContainer("advertiserDc")
@LoadDataBeforeShow
public class AdvertiserStatisticsRead extends StandardEditor<Advertiser> {
    @Inject
    private AdvertisementService advertisementService;
    @Inject
    private DataManager dataManager;
    private StringBuilder sb;
    @Inject
    private ResizableTextArea pasting;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) throws Exception {
        sb = new StringBuilder();
        calculatePastingExpenses(getEditedEntity().getAdvertisements());
        pasting.setValue(sb.toString());
    }



    private void calculatePastingExpenses(List<Advertisement> advertisements) throws Exception {
        BigDecimal theoreticalPastingCost = BigDecimal.ZERO;
        BigDecimal actualPastingCost = BigDecimal.ZERO;

        StringBuilder purposeRes = new StringBuilder();

        Long theoreticalCars = 0L;
        Long actualCars = 0L;
        int i = 1;
        for (Advertisement advertisement : advertisements) {

            BigDecimal theoreticalPurposeCost = BigDecimal.ZERO;
            BigDecimal actualPurposeCost = BigDecimal.ZERO;

            for (AdvPurpose advPurpose : advertisement.getPurposes()) {
                long operatedDays = DateUtility.differenceBetween(advertisement.getEndDate(), advertisement.getStartDate());
                Long maxCarAmount = advertisementService.calculateCarAmount(
                        advPurpose.getBudget(),
                        advPurpose.getCarClass(),
                        advPurpose.getStickerType(),
                        advPurpose.getPastingCost(),
                        advPurpose.getRewardAmount(),
                        operatedDays
                );
                theoreticalCars += maxCarAmount;
                //number of cars with stickers
                long advCarNumber = advPurpose.getAdvertisementDrivers().stream()
                        .filter(purposeDriver ->
                                !purposeDriver.getStatus().equals(EAdvDriverStatus.NEW)).count();
                actualCars += advCarNumber;


                // PASTING COSTS
                theoreticalPastingCost = theoreticalPastingCost
                        .add(advPurpose.getPastingCost().multiply(BigDecimal.valueOf(maxCarAmount)));
                actualPastingCost = actualPastingCost
                        .add(advPurpose.getPastingCost().multiply(BigDecimal.valueOf(advCarNumber)));

                //PURPOSE COSTS
                DPointCost thePointCost = dataManager.load(DPointCost.class).view("dPointCost-view").list().stream()
                        .filter(pointCost -> pointCost.getCarClass().equals(advPurpose.getCarClass())
                                && pointCost.getStickerType().equals(advPurpose.getStickerType()))
                        .findFirst()
                        .orElse(null);

                theoreticalPurposeCost = theoreticalPurposeCost.add(
                        thePointCost.getAdvertiserCost()
                                .multiply(BigDecimal.valueOf(advPurpose.getAdvertisementDrivers().size() * operatedDays))
                                .multiply(BigDecimal.valueOf(advPurpose.getCarClass().getMaxPointPerDay()))
                );
                actualPurposeCost = actualPurposeCost.add(
                        advPurpose.getAdvertisementDrivers()
                                .stream()
                                .map(AdvertisementDriver::getEarnedMoney)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
            }
            purposeRes.append("Цель " + i++ + ". ")
                    .append("Actual - " + actualPurposeCost + " , Theoretical - " + theoreticalPurposeCost);
        }
        sb.append("cars " + actualCars + "/" + theoreticalCars + " : price " + actualPastingCost + "/" + theoreticalPastingCost)
                .append("\n").append(purposeRes);
    }

    
    
    
    
}