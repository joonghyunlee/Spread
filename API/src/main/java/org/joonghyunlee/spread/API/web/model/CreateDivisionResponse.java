package org.joonghyunlee.spread.API.web.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;
import org.joonghyunlee.spread.API.domain.division.model.Division;

@Getter
@ToString
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "division")
public class CreateDivisionResponse {
    private String tokenId;

    public static CreateDivisionResponse of(Division division) {
        CreateDivisionResponse response = new CreateDivisionResponse();
        response.tokenId = division.getTokenId();

        return response;
    }
}
