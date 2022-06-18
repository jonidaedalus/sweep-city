package kz.alash.naklei.web.screens.car;

import javax.annotation.Nullable;

public enum ECarMenu {

    MAIN("mainMenu", "Основная информация", "mainInfoHBox"),
    CURRENT_ADV("advMenu", "Текущая рекл. кампания", "currentAdvDriverInfoHBox"),
    HISTORY("historyMenu", "История рекл. кампании", "purposeGroupBox");

    private final String id;
    private final String name;
    private final String contentId;

    ECarMenu(String id, String name, String contentId){
        this.id = id;
        this.name = name;
        this.contentId = contentId;
    }

    @Nullable
    public static ECarMenu fromId(String id) {
        for (ECarMenu at : ECarMenu.values()) {
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
