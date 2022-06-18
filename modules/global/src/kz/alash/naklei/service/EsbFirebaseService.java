package kz.alash.naklei.service;

import kz.alash.naklei.entity.Advertisement;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.moderation.Moderation;
import kz.alash.naklei.entity.visit.Visit;
import kz.alash.naklei.service.esb.dto.firebase.PushRequest;
import kz.alash.naklei.service.esb.dto.firebase.PushResponse;

public interface EsbFirebaseService {
    String NAME = "naklei_EsbFirebaseService";

    PushResponse sendPushMessage(
            PushRequest request,
            Driver driver,
            Advertisement advertisement,
            Moderation moderation,
            Visit visit
    );

    PushResponse sendPushMessage(
            String title,
            String body,
            String token,
            Driver driver,
            Advertisement advertisement,
            Moderation moderation,
            Visit visit
    );

    PushResponse sendModerationPush(
            String title,
            String body,
            String token,
            Driver driver,
            Advertisement advertisement,
            Moderation moderation,
            Visit visit
    );
}