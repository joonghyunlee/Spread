package org.joonghyunlee.spread.API.web.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "dividend")
public class ReceiveDividendResponse {
    private Long amount;

    public static ReceiveDividendResponse of(long amount) {
        ReceiveDividendResponse response = new ReceiveDividendResponse();
        response.amount = amount;

        return response;
    }
}
