package kz.alash.naklei.service;

import com.haulmont.cuba.core.app.FileStorageAPI;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import kz.alash.naklei.entity.Advertiser;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

@Service(AdvertiserService.NAME)
public class AdvertiserServiceBean implements AdvertiserService {

    @Inject
    private DataManager dataManager;
    @Inject
    private FileStorageAPI fileStorageAPI;

    @Override
    public byte[] advertiserLogo(String advertiserId) throws FileStorageException {
        Advertiser advertiser = dataManager.load(Advertiser.class).query("select a from naklei_Advertiser a where a.id = :id")
                .parameter("id", UUID.fromString(advertiserId))
                .viewProperties(
                        "logo",
                        "logo.name",
                        "logo.extension",
                        "logo.size",
                        "logo.createDate"
                )
                .one();
        return fileStorageAPI.loadFile(advertiser.getLogo());
    }

}