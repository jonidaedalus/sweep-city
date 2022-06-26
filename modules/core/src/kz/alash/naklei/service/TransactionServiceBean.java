package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import kz.alash.naklei.entity.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;

@Service(TransactionService.NAME)
public class TransactionServiceBean implements TransactionService {

    @Inject
    private DataManager dataManager;

    @Override
    public void createTransaction(
            PayOut payOut,
            BigDecimal sum
    ) {
        AdvertisementDriver advertisementDriver = payOut.getAdvertisementDriver();
        Transaction transaction = dataManager.create(Transaction.class);
        transaction.setAdvertisementDriver(advertisementDriver);
        transaction.setSum(sum);
        //get money from purpose
        AdvPurpose advPurpose = advertisementDriver.getPurpose();
        advPurpose.setCurrentBudget(
                advPurpose.getCurrentBudget().subtract(sum)
        );

        //give money to driver
        advertisementDriver.setCurrentMoney(
                advertisementDriver.getCurrentMoney().subtract(sum)
        );
        Driver driver = advertisementDriver.getDriver();

        driver.setWithdrawnMoney(
                driver.getWithdrawnMoney().add(sum)
        );

        driver.setCurrentMoney(
                driver.getCurrentMoney().subtract(sum)
        );
        dataManager.commit(
                new CommitContext(
                        transaction,
                        advertisementDriver,
                        advPurpose,
                        driver
                )
        );
    }
}