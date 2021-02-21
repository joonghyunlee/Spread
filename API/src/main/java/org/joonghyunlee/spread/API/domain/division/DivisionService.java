package org.joonghyunlee.spread.API.domain.division;

import lombok.RequiredArgsConstructor;
import org.joonghyunlee.spread.API.common.exception.SpreadException;
import org.joonghyunlee.spread.API.common.exception.SpreadExceptionCode;
import org.joonghyunlee.spread.API.domain.division.model.Division;
import org.joonghyunlee.spread.API.support.division.DividendRepository;
import org.joonghyunlee.spread.API.support.division.DivisionRepository;
import org.joonghyunlee.spread.API.support.division.model.DividendEntity;
import org.joonghyunlee.spread.API.support.division.model.DivisionEntity;
import org.joonghyunlee.spread.API.web.model.CreateDivisionRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class DivisionService {

    private static final int MAX_GET_DAY_LIMIT = 7;
    private static final int MAX_RECEIVE_MINUTE_LIMIT = 10;

    private final DivisionRepository divisionRepository;

    public Division create(String roomId, String userId, CreateDivisionRequest request) {
        DivisionEntity entity = DivisionEntity.builder()
                .roomId(roomId)
                .ownerId(userId)
                .amount(request.getAmount())
                .denominator(request.getDenominator())
                .build();
        divisionRepository.save(entity);

        return Division.of(entity);
    }

    public Division get(String roomId, String userId, String tokenId) {
        DivisionEntity entity = divisionRepository.findById(tokenId)
                .orElse(null);
        if (entity == null) {
            throw SpreadException.getInstance(SpreadExceptionCode.NOT_FOUND_DIVISION, tokenId);
        }

        if (!StringUtils.hasText(entity.getRoomId()) || !entity.getRoomId().equals(roomId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_ROOM_ID);
        } else if (!StringUtils.hasText(entity.getOwnerId()) || !entity.getOwnerId().equals(userId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_OWNER_ID);
        }

        ZonedDateTime limit = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(MAX_GET_DAY_LIMIT);
        if (entity.getCreatedAt().isBefore(limit)) {
            throw SpreadException.getInstance(SpreadExceptionCode.OUTDATED_DIVISION, entity.getTokenId());
        }

        return Division.of(entity);
    }

    public long receive(String roomId, String userId, String tokenId) {
        DivisionEntity entity = divisionRepository.findById(tokenId)
                .orElse(null);
        if (entity == null) {
            throw SpreadException.getInstance(SpreadExceptionCode.NOT_FOUND_DIVISION, tokenId);
        }

        if (!StringUtils.hasText(entity.getRoomId()) || !entity.getRoomId().equals(roomId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_ROOM_ID);
        } else if (!StringUtils.hasText(entity.getOwnerId()) || entity.getOwnerId().equals(userId)) {
            throw SpreadException.getInstance(SpreadExceptionCode.INVALID_OWNER_ID);
        }

        long sum = entity.getDividends().stream()
                .mapToLong(DividendEntity::getAmount)
                .sum();
        if (sum >= entity.getAmount()) {
            throw SpreadException.getInstance(SpreadExceptionCode.NO_EXIST_AMOUNT, entity.getAmount());
        }

        int received = entity.getDividends().size();
        if (received >= entity.getDenominator()) {
            throw SpreadException.getInstance(SpreadExceptionCode.COMPLETED_DIVISION, entity.getTokenId());
        }

        ZonedDateTime limit = ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(MAX_RECEIVE_MINUTE_LIMIT);
        if (entity.getCreatedAt().isBefore(limit)) {
            throw SpreadException.getInstance(SpreadExceptionCode.PASSED_DIVISION, entity.getTokenId());
        }

        long share = (entity.getAmount() - sum) / (entity.getDenominator() - received);
        entity.subtract(userId, share);
        divisionRepository.save(entity);

        return share;
    }
}
