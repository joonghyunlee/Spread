package org.joonghyunlee.spread.API.domain;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.joonghyunlee.spread.API.common.exception.SpreadException;
import org.joonghyunlee.spread.API.common.exception.SpreadExceptionCode;
import org.joonghyunlee.spread.API.domain.division.DivisionService;
import org.joonghyunlee.spread.API.domain.division.model.Division;
import org.joonghyunlee.spread.API.support.division.DivisionRepository;
import org.joonghyunlee.spread.API.support.division.model.DivisionEntity;
import org.joonghyunlee.spread.API.web.model.CreateDivisionRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DivisionServiceTest {

    @Autowired
    @InjectMocks
    private DivisionService divisionService;

    @MockBean
    private DivisionRepository divisionRepository;

    @Test
    public void createDivisionSuccessfulTest() {
        // given
        String roomId = RandomStringUtils.randomAlphanumeric(3);
        String userId = RandomStringUtils.randomAlphabetic(5);

        CreateDivisionRequest request = new CreateDivisionRequest();
        request.setAmount(1000000L);
        request.setDenominator(10);

        // when
        Division division = divisionService.create(roomId, userId, request);

        // then
        assertNotNull(division);
        assertEquals(roomId, division.getRoomId());
        assertEquals(userId, division.getOwnerId());
        assertEquals(Long.valueOf(1000000L), division.getAmount());
        log.debug(division.toString());
    }

    @Test
    public void createDivisionInvalidRoomIdFailedTest() {
        // given
        String roomId = "";
        String userId = RandomStringUtils.randomAlphabetic(5);

        CreateDivisionRequest request = new CreateDivisionRequest();
        request.setAmount(1000000L);
        request.setDenominator(10);

        // when
        Exception exception = Assert.assertThrows(
                SpreadException.class,
                () -> divisionService.create(roomId, userId, request)
        );

        // then
        assertEquals(SpreadExceptionCode.INVALID_ROOM_ID.getMessage(), exception.getMessage());
    }

    @Test
    public void createDivisionInvalidUserIdFailedTest() {
        // given
        String roomId = RandomStringUtils.randomAlphanumeric(3);
        String userId = "";

        CreateDivisionRequest request = new CreateDivisionRequest();
        request.setAmount(1000000L);
        request.setDenominator(10);

        // when
        Exception exception = Assert.assertThrows(
                SpreadException.class,
                () -> divisionService.create(roomId, userId, request)
        );

        // then
        assertEquals(SpreadExceptionCode.INVALID_OWNER_ID.getMessage(), exception.getMessage());
    }

    @Test
    public void getDivisionSuccessfulTest() {
        // given
        String roomId = RandomStringUtils.randomAlphanumeric(3);
        String userId = RandomStringUtils.randomAlphanumeric(5);

        Long amount = 1000000L;

        CreateDivisionRequest request = new CreateDivisionRequest();
        request.setAmount(amount);
        request.setDenominator(10);

        DivisionEntity entity = DivisionEntity.builder()
                .roomId(roomId)
                .ownerId(userId)
                .amount(amount)
                .denominator(5)
                .build();
        assertNotNull(entity);
        String tokenId = entity.getId();
        assertNotNull(tokenId);

        when(divisionRepository.findById(eq(tokenId)))
                .thenReturn(java.util.Optional.of(entity));

        // when
        Division division = divisionService.get(roomId, userId, tokenId);

        // then
        assertNotNull(division);
        log.debug(division.toString());
        assertEquals(tokenId, division.getTokenId());
        assertEquals(tokenId, division.getTokenId());
    }

    @Test
    public void getDivisionOutdatedFailedTest() {
        // given
        String roomId = RandomStringUtils.randomAlphanumeric(3);
        String userId = RandomStringUtils.randomAlphanumeric(5);

        Long amount = 1000000L;

        CreateDivisionRequest request = new CreateDivisionRequest();
        request.setAmount(amount);
        request.setDenominator(10);

        DivisionEntity entity = DivisionEntity.builder()
                .roomId(roomId)
                .ownerId(userId)
                .amount(amount)
                .denominator(5)
                .build();
        assertNotNull(entity);
        String tokenId = entity.getId();
        assertNotNull(tokenId);

        entity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC"))
                .minusDays(7)
                .minusSeconds(1));

        when(divisionRepository.findById(eq(tokenId)))
                .thenReturn(java.util.Optional.of(entity));

        // when
        Exception exception = Assert.assertThrows(
                SpreadException.class,
                () -> divisionService.get(roomId, userId, tokenId)
        );

        // then
        assertEquals(String.format(SpreadExceptionCode.OUTDATED_DIVISION.getMessage(), tokenId),
                exception.getMessage());
    }

    @Test
    public void receiveDivisionPassAwayFailedTest() {
        // given
        String roomId = RandomStringUtils.randomAlphanumeric(3);
        String userId = RandomStringUtils.randomAlphanumeric(5);
        String receiverId = RandomStringUtils.randomAlphanumeric(5);

        Long amount = 1000000L;

        CreateDivisionRequest request = new CreateDivisionRequest();
        request.setAmount(amount);
        request.setDenominator(10);

        DivisionEntity entity = DivisionEntity.builder()
                .roomId(roomId)
                .ownerId(userId)
                .amount(amount)
                .denominator(5)
                .build();
        assertNotNull(entity);
        String tokenId = entity.getId();
        assertNotNull(tokenId);

        entity.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC"))
                .minusMinutes(10)
                .minusSeconds(1));

        when(divisionRepository.findById(eq(tokenId)))
                .thenReturn(java.util.Optional.of(entity));

        // when
        Exception exception = Assert.assertThrows(
                SpreadException.class,
                () -> divisionService.receive(roomId, receiverId, tokenId)
        );

        // then
        assertEquals(String.format(SpreadExceptionCode.PASSED_DIVISION.getMessage(), tokenId),
                exception.getMessage());
    }
}
