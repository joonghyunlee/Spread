package org.joonghyunlee.spread.API.web.model;

import lombok.Getter;
import lombok.ToString;
import org.joonghyunlee.spread.API.domain.division.model.Division;

@Getter
@ToString
public class GetDivisionResponse {
    private Division division;

    public static GetDivisionResponse of(Division division) {
        GetDivisionResponse response = new GetDivisionResponse();
        response.division = division;

        return response;
    }
}
