package kz.alash.naklei.role;

import com.haulmont.cuba.security.app.role.AnnotatedRoleDefinition;
import com.haulmont.cuba.security.app.role.annotation.Role;
import kz.alash.naklei.ConstantsRole;

@Role(name = StickEmployeeRole.NAME,
        description = "Оклейщик")
public class StickEmployeeRole extends AnnotatedRoleDefinition {

    public static final String NAME = ConstantsRole.STICK_EMPLOYEE;
    public static final String LOC_NAME = "Оклейщик";


}
