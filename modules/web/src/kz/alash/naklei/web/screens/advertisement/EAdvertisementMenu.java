package kz.alash.naklei.web.screens.advertisement;

import kz.alash.naklei.entity.dict.EModerationType;

import javax.annotation.Nullable;

public enum EAdvertisementMenu {

    PURPOSE("purposeMenu", "Цели", "purposeGroupBox"),
    CARS("carsMenu", "Авто", "carsGroupBox"),
    STATISTICS("statisticsMenu", "Статистика", "statisticGroupBox");

    private final String id;
    private final String name;
    private final String contentId;

    EAdvertisementMenu(String id, String name, String contentId){
        this.id = id;
        this.name = name;
        this.contentId = contentId;
    }

    @Nullable
    public static EAdvertisementMenu fromId(String id) {
        for (EAdvertisementMenu at : EAdvertisementMenu.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getContentId() {
        return contentId;
    }
}
