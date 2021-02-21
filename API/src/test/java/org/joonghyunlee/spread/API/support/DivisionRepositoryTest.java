package org.joonghyunlee.spread.API.support;

import lombok.extern.slf4j.Slf4j;
import org.joonghyunlee.spread.API.support.division.DivisionRepository;
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
public class DivisionRepositoryTest {

    @Autowired
    private DivisionRepository divisionRepository;

    @Test
    @Rollback(value = false)
    public void saveAndFindDivisionTest() {
        // given
        DivisionEntity entity = DivisionEntity.builder()
                .ownerId("AAA")
                .roomId("1")
                .amount(100000L)
                .denominator(5)
                .build();
        String tokenId = entity.getId();
        Assert.assertNotNull(tokenId);

        log.debug(entity.toString());

        // when
        divisionRepository.save(entity);

        // then
        DivisionEntity saved = divisionRepository.findById(tokenId)
                .orElse(null);
        Assert.assertEquals(entity, saved);
    }
}
