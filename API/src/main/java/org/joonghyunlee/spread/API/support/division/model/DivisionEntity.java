package org.joonghyunlee.spread.API.support.division.model;

import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.joonghyunlee.spread.API.common.exception.SpreadException;
import org.joonghyunlee.spread.API.common.exception.SpreadExceptionCode;
import org.springframework.data.domain.Persistable;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude = "dividends")
@NoArgsConstructor
@Entity
@Table(name = "division")
public class DivisionEntity implements Persistable<String> {

    @Id
    @Column(name = "token_id", nullable = false, length = 3)
    private String tokenId;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "nr_receiver", nullable = false)
    private Integer denominator;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Setter
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    private List<DividendEntity> dividends;

    @Transient
    private boolean isNew = false;

    @Override
    public String getId() {
        return this.tokenId;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @Builder
    public DivisionEntity(String roomId, String ownerId, Long amount, Integer denominator) {
        if (!StringUtils.hasText(roomId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_ROOM_ID);
        } else if (!StringUtils.hasText(ownerId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_OWNER_ID);
        } else if (amount == null) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_AMOUNT);
        } else if (denominator == null) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_DENOMINATOR);
        }

        this.tokenId = RandomStringUtils.randomAlphanumeric(3);
        this.roomId = roomId;
        this.ownerId = ownerId;

        this.amount = amount;
        this.denominator = denominator;
        this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
        this.dividends = new ArrayList<>();
        this.isNew = true;
    }

    public void subtract(String userId, Long dividendAmount) {
        if (!StringUtils.hasText(userId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_OWNER_ID);
        } else if (dividendAmount == null) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_AMOUNT);
        } else if (dividendAmount > this.amount) {
            throw SpreadException.getInstance(SpreadExceptionCode.NO_EXIST_AMOUNT, this.getAmount());
        }

        DividendEntity dividendEntity = DividendEntity.builder()
                .division(this)
                .userId(userId)
                .amount(dividendAmount)
                .build();

        this.dividends.add(dividendEntity);
    }
}
