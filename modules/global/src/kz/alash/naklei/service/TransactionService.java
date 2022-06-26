package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.CommitContext;
import kz.alash.naklei.entity.PayOut;

import java.math.BigDecimal;

public interface TransactionService {
    String NAME = "naklei_TransactionService";

    void createTransaction(
            PayOut payOut,
            BigDecimal sum
    );
}