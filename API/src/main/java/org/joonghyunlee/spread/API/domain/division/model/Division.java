package org.joonghyunlee.spread.API.domain.division.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.joonghyunlee.spread.API.support.division.model.DividendEntity;
import org.joonghyunlee.spread.API.support.division.model.DivisionEntity;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
public class Division {

    @JsonInclude
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private ZonedDateTime createdAt;

    private String tokenId;
    private String roomId;
    private String ownerId;

    private Long amount;
    private Long completedAmount;

    private List<Complete> completes;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Complete {
        private final String userId;
        private final Long amount;
    }

    public static Division of(DivisionEntity entity) {
        Division division = new Division();
        division.createdAt = entity.getCreatedAt();
        division.tokenId = entity.getTokenId();
        division.ownerId = entity.getOwnerId();
        division.roomId = entity.getRoomId();
        division.amount = entity.getAmount();
        division.completedAmount = entity.getDividends().stream()
                .mapToLong(DividendEntity::getAmount)
                .sum();

        division.completes = entity.getDividends().stream()
                .map(div -> new Complete(div.getUserId(), div.getAmount()))
                .collect(Collectors.toList());

        return division;
    }
}
