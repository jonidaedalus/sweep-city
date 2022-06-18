package kz.alash.naklei.group;

import com.haulmont.cuba.security.app.group.AnnotatedAccessGroupDefinition;
import com.haulmont.cuba.security.app.group.annotation.AccessGroup;

@AccessGroup(name = "Driver", parent = RootGroup.class)
public class DriverGroup extends AnnotatedAccessGroupDefinition {
}
