package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import kz.alash.naklei.entity.Advertisement;
import kz.alash.naklei.entity.Driver;
import kz.alash.naklei.entity.Notification;
import kz.alash.naklei.entity.moderation.Moderation;
import kz.alash.naklei.entity.visit.Visit;
import kz.alash.naklei.service.esb.dto.firebase.PushRequest;
import kz.alash.naklei.service.esb.dto.firebase.PushResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Arrays;

@Service(EsbFirebaseService.NAME)
public class EsbFirebaseServiceBean implements EsbFirebaseService {

    private static final Logger log = LoggerFactory.getLogger(EsbFirebaseServiceBean.class);
    private RestTemplate restTemplate;

    private int esbReadTimeout = 60000;

    private boolean esbDebug = false;

    private int connectTimeout = 5000;

    private int connectionRequestTimeout = 5000;
    @Inject
    private DataManager dataManager;
    @Inject
    private Metadata metadata;

    @Override
    public PushResponse sendPushMessage(PushRequest request, Driver driver, Advertisement advertisement, Moderation moderation, Visit visit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key = AAAA90wzcrw:APA91bHfZci4fbCQmCi7pGW8-iKyayHlPvxWQdIDC_tsBf45T90r5k0DHoq_S2k1Ata_bX0JufEd5s7UAKwioaklVTr-eCWD6jYbfNl5hTPMTLWvF8RkObGfawscsGtkDfKsbRFUonEB");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        PushRequest.Data pushData = new PushRequest.Data();
        pushData.setModerationId(moderation != null ? moderation.getId().toString() : null);
        pushData.setAdvertisementId(driver != null ? driver.getId().toString() : null);
        pushData.setDriverId(advertisement != null ? advertisement.getId().toString() : null);
        pushData.setVisitId(visit != null ? visit.getId().toString() : null);
        request.setData(pushData);

        HttpEntity<PushRequest> entity = new HttpEntity<>(request, headers);

        Notification notification = metadata.create(Notification.class);
        notification.setMessage(request.getNotification().getBody());
        notification.setTitle(request.getNotification().getTitle());
        notification.setAdvertisment(advertisement);
        notification.setModeration(moderation);
        notification.setDriver(driver);
        notification.setVisit(visit);

        ResponseEntity<PushResponse> response = null;

        String url = "https://fcm.googleapis.com/fcm/send";
        try {
            response = getRestTemplate().exchange(url, HttpMethod.POST, entity, PushResponse.class);
        } catch (Throwable e) {
            // отлавливаем техническую ошибку только для логирования - оказывается flowable не пишет в логи при ошибках из процессах
            log.error("Error while accessing " + url, e);
            //createLogResponse(extLog, response, e);
            notification.setSuccess(false);
            dataManager.commit(notification);
            throw e;
        }

        //log.debug("Response: " + response);

        if (response.getBody() == null) {
            RuntimeException e = new RuntimeException("Response body is empty.");
            //createLogResponse(extLog, response, e);
            throw e;
        }

        notification.setSuccess(response.getBody().getSuccess() == 200);
        dataManager.commit(notification);
        return response.getBody();
        //createLogResponse(extLog, response, null);

        //return response.getBody();
    }

    @Override
    public PushResponse sendPushMessage(String title, String body, String token, Driver driver, Advertisement advertisement, Moderation moderation, Visit visit) {
        PushRequest request = new PushRequest();
        //todo изменить to на token устройства(его надо добавить пользователю)
        request.setTo(token);
        PushRequest.Notification notification = new PushRequest.Notification();

        notification.setTitle(title);
        notification.setBody(body);

        request.setNotification(notification);
        return sendPushMessage(request, driver, advertisement, moderation, visit);
    }

    @Override
    public PushResponse sendModerationPush(String title, String body, String token, Driver driver, Advertisement advertisement, Moderation moderation, Visit visit) {
        PushRequest request = new PushRequest();
        //todo изменить to на token устройства(его надо добавить пользователю)
        request.setTo(token);
        PushRequest.Notification notification = new PushRequest.Notification();

        notification.setTitle(title);
        notification.setBody(body);
        notification.setSound("default");

        request.setNotification(notification);

        return sendPushMessage(request, driver, advertisement, moderation, visit);
    }


    public RestTemplate getRestTemplate() {
        if (restTemplate != null)
            return restTemplate;

        synchronized (this) {
            // повторно, чтобы не создавать дубликатов
            if (restTemplate != null)
                return restTemplate;
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectTimeout(connectTimeout);
            httpRequestFactory.setReadTimeout(esbReadTimeout);
            httpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
            log.info("esb.connectTimeout set to: " + connectTimeout);
            log.info("esb.readTimeout set to: " + esbReadTimeout);
            log.info("esb.connectionRequestTimeout set to: " + connectionRequestTimeout);


//            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//
//            SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
//                    .loadTrustMaterial(null, acceptingTrustStrategy)
//                    .build();
//
//            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
//            CloseableHttpClient httpClient = null;
//            try {
//                httpClient = HttpClients.custom()
//                        //.setSSLSocketFactory(csf)
//                        .setSSLContext( SSLContext.getDefault() )
//                        .useSystemProperties()
//                        .build();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//
//            httpRequestFactory.setHttpClient(httpClient);

            restTemplate = new RestTemplate(httpRequestFactory);
        }
        return restTemplate;
    }
}