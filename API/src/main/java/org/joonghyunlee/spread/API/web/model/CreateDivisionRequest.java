package org.joonghyunlee.spread.API.web.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CreateDivisionRequest {
    private Long amount;
    private Integer denominator;
}
