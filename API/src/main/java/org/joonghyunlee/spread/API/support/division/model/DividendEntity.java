package org.joonghyunlee.spread.API.support.division.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;
import org.joonghyunlee.spread.API.common.exception.SpreadException;
import org.joonghyunlee.spread.API.common.exception.SpreadExceptionCode;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Getter
@ToString(exclude = "division")
@NoArgsConstructor
@Entity
@Table(name = "dividend", uniqueConstraints = {
        @UniqueConstraint(
                name = "TOKEN_USER_UNIQUE",
                columnNames = {"token_id", "user_id"}
        )
})
public class DividendEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private Long amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "token_id", nullable = false)
    private DivisionEntity division;

    @Builder
    public DividendEntity(DivisionEntity division, String userId, Long amount) {
        if (!StringUtils.hasText(userId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_OWNER_ID);
        } else if (amount == null) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_AMOUNT);
        }

        this.userId = userId;
        this.amount = amount;
        this.division = division;
    }
}
