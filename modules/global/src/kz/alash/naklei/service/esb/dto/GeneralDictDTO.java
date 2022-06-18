package kz.alash.naklei.service.esb.dto;

import java.io.Serializable;

public class GeneralDictDTO implements Serializable {

    private String code;
    private String name;

    public static GeneralDictDTO of(String code, String name) {
        return new GeneralDictDTO(code, name);
    }

    private GeneralDictDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
