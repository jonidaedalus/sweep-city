package kz.alash.naklei.service.esb.dto.purpose;

import kz.alash.naklei.service.esb.dto.GenericResponse;

import java.util.ArrayList;
import java.util.List;

public class GetPotentialPurposesResponse extends GenericResponse<List<PurposeDTO>> {

    public static GetPotentialPurposesResponse newInstance() {
        return new GetPotentialPurposesResponse();
    }
    private GetPotentialPurposesResponse() {
        this.setResult(new ArrayList<>());
    }
}
