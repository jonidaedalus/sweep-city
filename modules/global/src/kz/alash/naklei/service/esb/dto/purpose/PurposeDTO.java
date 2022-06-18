package kz.alash.naklei.service.esb.dto.purpose;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.alash.naklei.service.esb.dto.GeneralDictDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PurposeDTO implements Serializable {
    private String id;
    private String name;
    private String niche;
    private String description;
    private int participants;
    private String startDate;
    private String endDate;
    private String pastingDate;
    private String carMaket;
    private String status;
    private int carAmount;
    private GeneralDictDTO stickerType;
    private GeneralDictDTO carClass;
    private GeneralDictDTO city;
    private boolean isActive;
    private BigDecimal reward;
    private BigDecimal income;
    private String advertiserId;

    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getCarMaket() {
        return carMaket;
    }

    public void setCarMaket(String carMaket) {
        this.carMaket = carMaket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNiche() {
        return niche;
    }

    public void setNiche(String niche) {
        this.niche = niche;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPastingDate() {
        return pastingDate;
    }

    public void setPastingDate(String pastingDate) {
        this.pastingDate = pastingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCarAmount() {
        return carAmount;
    }

    public void setCarAmount(int carAmount) {
        this.carAmount = carAmount;
    }

    public GeneralDictDTO getStickerType() {
        return stickerType;
    }

    public void setStickerType(GeneralDictDTO stickerType) {
        this.stickerType = stickerType;
    }

    public GeneralDictDTO getCarClass() {
        return carClass;
    }

    public void setCarClass(GeneralDictDTO carClass) {
        this.carClass = carClass;
    }

    public GeneralDictDTO getCity() {
        return city;
    }

    public void setCity(GeneralDictDTO city) {
        this.city = city;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }
}

/*
* {
    "fullName": "Kaspi",
    "niche": "Банк", //Ниша компании
    "description": "Бесплатные билеты тем кто купил в Каспи магазине бытовые техники", //Описание рекламной кампании
    "id": "c077a285-b802-e4c4-7621-73d37aade20c",
    "participants": 0, //Участиники
    "startDate": "2022-03-27",
    "endDate": "2022-05-27",
    "pastingDate": "2022-02-27", //(дата оклейки)
    "carMaket": "asda8123i21g4", //Конвертированный рисунок макета наклейки в base64
    "status": "IN_PROCESS",
    "carAmount": 33,
    "stickerType": {
        "code": "SMALL",
        "name": "20%"
    },
    "carClass": {
        "code": "A",
        "name": "Эконом"
    },
    "city": {
        "code": "ALA",
        "name": "Алматы"
    },
    "isActive": "true", //Пришло время оклейки или нет
    "reward": "500", //Вознаграждение за оклейку заранее
    "income": "15", //Прибыль за км в тенге
}*/
