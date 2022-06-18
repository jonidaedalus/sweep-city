package kz.alash.naklei.service.esb.dto;

import java.io.Serializable;

public class DocumentSaveRequest implements Serializable {

    private String docTypeCode;
    private String fileId;

    public String getDocTypeCode() {
        return docTypeCode;
    }

    public void setDocTypeCode(String docTypeCode) {
        this.docTypeCode = docTypeCode;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
