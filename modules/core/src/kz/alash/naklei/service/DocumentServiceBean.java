package kz.alash.naklei.service;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.security.app.Authentication;
import com.haulmont.cuba.security.entity.User;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.service.esb.dto.DocumentSaveRequest;
import kz.alash.naklei.service.esb.dto.DocumentSaveResponse;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service(DocumentService.NAME)
public class DocumentServiceBean implements DocumentService {

    private final String TECH_PASSPORT_DOC_TYPE_CODE = "TECHNICAL_PASSPORT";
    private final String PHOTO_WITHOUT_STICKER = "PHOTO_WITHOUT_STICKER";
    private final String PHOTO_WITH_STICKER = "PHOTO_WITH_STICKER";
    @Inject
    private DataManager dataManager;
    @Inject
    private Authentication authentication;
    @Inject
    private DriverService driverService;

    @Override
    public DocumentSaveResponse save(DocumentSaveRequest request) {
        DocumentSaveResponse response = new DocumentSaveResponse();
        response.setCode("0");

        User user = authentication.begin().getUser();
//        Driver driver = id(UUID.fromString(request.getEntityId())).one()
        Driver driver = driverService.getDriverByUserId(user.getId())
                .view("driver-edit")
                .one();
        FileDescriptor file = dataManager.load(FileDescriptor.class).id(UUID.fromString(request.getFileId())).one();

        switch (request.getDocTypeCode()) {
            case TECH_PASSPORT_DOC_TYPE_CODE:
                driver.setTechPassportDoc(file);
                break;
            case PHOTO_WITHOUT_STICKER:
                if (driver.getCar().getPhotos() == null) {
                    List<FileDescriptor> photos = new ArrayList<>();
                    driver.getCar().setPhotos(photos);
                }
                driver.getCar().getPhotos().add(file);
//                driver.setPhotoWithoutSticker(file);

                break;
            //todo to finish
            case PHOTO_WITH_STICKER:
                break;
        }
        dataManager.commit(driver.getCar());
        authentication.end();

        return response;
    }
}