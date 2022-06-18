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

@Role(name = NoRegisteredRole.NAME,
        description = "Не зарегистрированный")
public class NoRegisteredRole extends AnnotatedRoleDefinition {

    public static final String NAME = ConstantsRole.NO_REGISTERED;
    public static final String LOC_NAME = "Не зарегистрированный";

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

    @EntityAttributeAccess(entityName = "naklei_DMark", view = "*")
    @EntityAttributeAccess(entityName = "naklei_DModel", view = "*")
    @EntityAttributeAccess(entityName = "naklei_DCity", view = "*")
    @EntityAttributeAccess(entityName = "naklei_Advertisement", view = "*")
    @EntityAttributeAccess(entityName = "naklei_Advertiser", view = "*")
    @EntityAttributeAccess(entityName = "naklei_DClass", view = "*")
    @EntityAttributeAccess(entityName = "naklei_DColor", view = "*")
    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @EntityAccess(entityName = "naklei_DMark", operations = {EntityOp.READ})
    @EntityAccess(entityName = "naklei_DModel", operations = {EntityOp.READ})
    @EntityAccess(entityName = "naklei_DCity", operations = {EntityOp.READ})
    @EntityAccess(entityName = "naklei_Advertisement", operations = {EntityOp.READ})
    @EntityAccess(entityName = "naklei_Advertiser", operations = {EntityOp.READ})
    @EntityAccess(entityName = "naklei_DClass", operations = {EntityOp.READ})
    @EntityAccess(entityName = "naklei_DColor", operations = {EntityOp.READ})
    @EntityAccess(entityClass = FileDescriptor.class, operations = {EntityOp.CREATE, EntityOp.READ})
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
