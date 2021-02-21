package org.joonghyunlee.spread.API.support;

import lombok.extern.slf4j.Slf4j;
import org.joonghyunlee.spread.API.common.exception.SpreadException;
import org.joonghyunlee.spread.API.domain.division.model.Division;
import org.joonghyunlee.spread.API.support.division.DividendRepository;
import org.joonghyunlee.spread.API.support.division.DivisionRepository;
import org.joonghyunlee.spread.API.support.division.model.DividendEntity;
import org.joonghyunlee.spread.API.support.division.model.DivisionEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
public class DividendRepositoryTest {

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private DividendRepository dividendRepository;

    @Test
    @Rollback(value = false)
    public void saveDividendSuccessfulTest() {
        // given
        DivisionEntity entity = DivisionEntity.builder()
                .ownerId("AAA")
                .roomId("1")
                .amount(100000L)
                .denominator(5)
                .build();
        String tokenId = entity.getId();
        Assert.assertNotNull(tokenId);

        divisionRepository.save(entity);

        // when
        entity.subtract("BBB", 10000L);
        divisionRepository.save(entity);

        // then
        DivisionEntity saved = divisionRepository.findById(tokenId)
                .orElse(null);
        Assert.assertNotNull(saved);
        Assert.assertFalse(saved.getDividends().isEmpty());
        Assert.assertEquals("BBB", saved.getDividends().get(0).getUserId());
        Assert.assertEquals(10000L, saved.getDividends().stream()
                .mapToLong(DividendEntity::getAmount)
                .sum());
    }

    @Test
    @Rollback(value = false)
    public void saveDividendGreaterExceptionFailedTest() {
        // given
        DivisionEntity entity = DivisionEntity.builder()
                .ownerId("AAA")
                .roomId("2")
                .amount(100000L)
                .denominator(5)
                .build();
        String tokenId = entity.getId();
        Assert.assertNotNull(tokenId);

        divisionRepository.save(entity);

        // when
        Exception exception = Assert.assertThrows(
                SpreadException.class,
                () -> entity.subtract("BBB", 1000000L)
        );

        // then
        Assert.assertEquals(String.format("Dividend should be less than the amount: %d", 100000L),
                exception.getMessage());
        log.debug(exception.getMessage());
    }

    @Test
    @Rollback(value = false)
    public void findDividendByDivisionAndUserIdSuccessfulTest() {
        // given
        DivisionEntity entity = DivisionEntity.builder()
                .ownerId("AAA")
                .roomId("3")
                .amount(100000L)
                .denominator(5)
                .build();
        entity.subtract("BBB", 10000L);
        divisionRepository.save(entity);

        // when
        Long count = dividendRepository.countByDivisionAndUserId(entity, "BBB");

        // then
        Assert.assertEquals(1L, count.longValue());
    }
}
