package kz.alash.naklei.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.global.DataManager;
import kz.alash.naklei.entity.dict.DZone;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(RedisService.NAME)
public class RedisServiceBean implements RedisService {

    private static final int DEFAULT_KEY_LIFE_TIME_HOURS = 24;

//    @Value("${redis.events.lifetime}")
    private long keyLifeTime = DEFAULT_KEY_LIFE_TIME_HOURS;

    @Autowired
    private RedisTemplate<String, String> template;

    @Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
    @Inject
    private DataManager dataManager;
    @Inject
    private EntitySerializationAPI entitySerializationAPI;
    @Inject
    private Logger log;

    @Override
    public String getZonesByCode(String cityCode) {
        try {
            String key = buildZoneKey(cityCode);
            if (Boolean.FALSE.equals(template.hasKey(key))) {
                List<DZone> zones = dataManager.load(DZone.class)
                        .query("select d from naklei_DZone d where d.city.code = :cityCode")
                        .parameter("cityCode", "ALA")
                        .view("dZone-rest")
                        .list();

                String response = entitySerializationAPI.toJson(zones);
                BoundValueOperations<String, String> boundValueOperations = template.boundValueOps(key);
                boundValueOperations.set(response);
                boundValueOperations.expire(keyLifeTime, TimeUnit.HOURS);
                return response;
            }
            return template.opsForValue().get(key);
        } catch (Exception e) {
            log.error("ERROR WHILE GETTING A ZONE QUERY");
        }
        return null;
    }


    private String buildZoneKey(String code) {
        return "SWEEP_ZONE_" + code;
    }
}