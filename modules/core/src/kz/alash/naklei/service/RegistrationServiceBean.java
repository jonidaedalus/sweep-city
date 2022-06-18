package kz.alash.naklei.service;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.UserRole;
import kz.alash.naklei.entity.ExtUser;
import kz.alash.naklei.entity.dict.DCity;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(RegistrationService.NAME)
public class RegistrationServiceBean implements RegistrationService {

    @Inject
    private Metadata metadata;
    @Inject
    private PasswordEncryption passwordEncryption;
    @Inject
    private DataManager dataManager;

    //Создание пользователя с ролью
    //добавил commitContext чтоб соблюдать атомарность транзакций - либо транзакция проходит, либо нет
    @Override
    public ExtUser registerUser(String phoneNumber,
                             String password,
                             String groupNames,
                             String roleName,
                             String cityCode,
                             String fullName,
                             String appleID,
                             CommitContext context) {
        // Create a user instance
        ExtUser newUser = metadata.create(ExtUser.class);
        newUser.setLogin(phoneNumber);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(passwordEncryption.getPasswordHash(newUser.getId(), password));
        newUser.setActive(true);
        newUser.setAppleID(appleID);

        DCity city = dataManager.load(DCity.class).query("select c from naklei_DCity c where c.code = :code")
                .parameter("code", cityCode).one();

        newUser.setName(fullName);
        newUser.setCity(city);

        // Note that the platform does not support the default group out of the box, so here we define the default group id and set it for the newly registered users.
        newUser.setGroupNames(groupNames);

        /* Create a link to the role
         * Here we programmatically set the default role.
         * Another way is to set the default role by using the DB scripts. Set IS_DEFAULT_ROLE parameter to true in the insert script for the role.
         * Also, this parameter might be changed in the Role Editor screen.
         */
        UserRole userRole = metadata.create(UserRole.class);
        userRole.setUser(newUser);

        userRole.setRoleName(roleName);

        // Save new entities
        context.addInstanceToCommit(newUser);
        context.addInstanceToCommit(userRole);

        return newUser;
    }

}