package kz.alash.naklei.service.esb;

import kz.alash.naklei.service.esb.dto.visit.CreateVisitRequest;
import kz.alash.naklei.service.esb.dto.visit.CreateVisitResponse;
import kz.alash.naklei.service.esb.dto.visit.FreeTimeSlotResponse;
import kz.alash.naklei.service.esb.dto.visit.VisitCompaniesResponse;

import java.util.Date;


public interface EsbVisitService {
    String NAME = "naklei_EsbVisitService";

    CreateVisitResponse createVisit(CreateVisitRequest request);

    FreeTimeSlotResponse getVisitFreeTime(String visitType, Date day, String city, int visitLength);

    VisitCompaniesResponse visitCompanies(String cityCode, String visitType);
}