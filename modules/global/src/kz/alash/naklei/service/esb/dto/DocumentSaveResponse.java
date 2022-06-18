package kz.alash.naklei.service.esb.dto;

import java.io.Serializable;
import java.util.List;

public class DocumentSaveResponse extends GenericResponse<List<DocumentSaveResponse.Result>> {

    public static class Result implements Serializable {

        private String code;
        private String name;

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
}
