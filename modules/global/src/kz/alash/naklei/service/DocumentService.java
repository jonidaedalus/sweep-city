package kz.alash.naklei.service;

import kz.alash.naklei.service.esb.dto.DocumentSaveRequest;
import kz.alash.naklei.service.esb.dto.DocumentSaveResponse;

public interface DocumentService {
    String NAME = "naklei_DocumentService";

    DocumentSaveResponse save(DocumentSaveRequest request);
}