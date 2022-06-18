package kz.alash.naklei.service;

import kz.alash.naklei.entity.Car;
import kz.alash.naklei.service.esb.dto.moderation.ModerationRequest;
import kz.alash.naklei.service.esb.dto.moderation.ModerationResponse;

public interface ModerationService {
    String NAME = "naklei_ModerationService";

//    ModerationResponse sendPhotosToModeration(ModerationRequest moderationRequest);
//
//    ModerationResponse sendToCarWashModeration(ModerationRequest moderation);
//
//    ModerationResponse sendToStartModeration(ModerationRequest moderation);
//
//    ModerationResponse sendToFinishModeration(ModerationRequest moderation);
//
//    ModerationResponse sendToPrecheckModeration(ModerationRequest moderation);

    ModerationResponse sendModeration(ModerationRequest moderationRequest);
}