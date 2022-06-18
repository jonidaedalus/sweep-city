package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.CommitContext;
import kz.alash.naklei.entity.ExtUser;
import kz.alash.naklei.validation.CheckUserExists;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

public interface RegistrationService {
    String NAME = "naklei_RegistrationService";


    //Создание пользователя с ролью
    //добавил commitContext чтоб соблюдать атомарность транзакций - либо транзакция проходит, либо нет
    @Validated
    ExtUser registerUser(@CheckUserExists(message = "{msg://UserExistsValidator.message}") String phoneNumber,
                         String password,
                         String groupNames,
                         String roleName,
                         String cityCode,
                         String fullName,
                         String appleID,
                         CommitContext context);

}