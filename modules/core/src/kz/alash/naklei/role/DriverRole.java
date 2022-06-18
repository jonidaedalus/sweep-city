package kz.alash.naklei.role;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.security.app.role.AnnotatedRoleDefinition;
import com.haulmont.cuba.security.app.role.annotation.EntityAccess;
import com.haulmont.cuba.security.app.role.annotation.EntityAttributeAccess;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.app.role.annotation.SpecificAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;
import com.haulmont.cuba.security.role.ScreenPermissionsContainer;
import com.haulmont.cuba.security.role.SpecificPermissionsContainer;
import kz.alash.naklei.ConstantsRole;

@Role(name = DriverRole.NAME,
        description = "Водитель")
public class DriverRole extends AnnotatedRoleDefinition {

    public static final String NAME = ConstantsRole.DRIVER;
    public static final String LOC_NAME = "Водитель";

    @Override
    public String getLocName() {
        return LOC_NAME;
    }

    @Override
    public String getSecurityScope() {
        return "REST";
    }

    @Override
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    @EntityAttributeAccess(entityName = "*", view = "*", modify = "*")
    //@EntityAttributeAccess(entityClass = Customer.class, modify = {"grade", "comments"})
    //@EntityAttributeAccess(entityClass = Order.class, modify = "*")
    //@EntityAttributeAccess(entityClass = FileDescriptor.class, modify = "*")
    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @EntityAccess(entityName = "*", operations = {EntityOp.READ, EntityOp.CREATE})
    @EntityAccess(entityClass = FileDescriptor.class, operations = {EntityOp.CREATE, EntityOp.READ, EntityOp.UPDATE, EntityOp.DELETE})
    @Override
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @SpecificAccess(permissions = "cuba.restApi.enabled")
    @SpecificAccess(permissions = "cuba.restApi.fileUpload.enabled")
    @Override
    public SpecificPermissionsContainer specificPermissions() {
        return super.specificPermissions();
    }




}
