package kz.alash.naklei.service.esb;

import kz.alash.naklei.service.esb.dto.UpdateFcmTokenResponse;
import kz.alash.naklei.service.esb.dto.advertisement.GetHistoryAdvertisementResponse;
import kz.alash.naklei.service.esb.dto.profile.ProfileDataResponse;

import java.util.List;

public interface EsbUserService {
    String NAME = "userService";

    UpdateFcmTokenResponse updateUserToken(String token);

    ProfileDataResponse getProfileData();

    GetHistoryAdvertisementResponse getHistoryAdvertisements();

}