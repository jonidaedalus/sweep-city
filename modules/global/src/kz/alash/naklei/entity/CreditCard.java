package kz.alash.naklei.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "NAKLEI_CREDIT_CARD")
@Entity(name = "naklei_CreditCard")
@NamePattern("%s|name")
public class CreditCard extends StandardEntity {
    private static final long serialVersionUID = -6260639371578366882L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}