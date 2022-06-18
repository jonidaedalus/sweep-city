package kz.alash.naklei.web.screens.visitcompany;

import com.haulmont.cuba.gui.screen.*;
import kz.alash.naklei.entity.visit.VisitCompany;

@UiController("naklei_VisitCompany.browse")
@UiDescriptor("visit-company-browse.xml")
@LookupComponent("visitCompaniesTable")
@LoadDataBeforeShow
public class VisitCompanyBrowse extends StandardLookup<VisitCompany> {
}