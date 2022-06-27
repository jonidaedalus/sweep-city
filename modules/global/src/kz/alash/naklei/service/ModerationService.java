package kz.alash.naklei.service;

import kz.alash.naklei.service.esb.dto.GenericResponse;
import kz.alash.naklei.service.esb.dto.moderation.ModerationDto;
import kz.alash.naklei.service.esb.dto.moderation.ModerationRequest;
import kz.alash.naklei.service.esb.dto.moderation.ModerationResponse;

import java.util.List;

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
    GenericResponse<List<ModerationDto>> getValidModerations();
}