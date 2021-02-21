package org.joonghyunlee.spread.API.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpreadExceptionCode {
    // Bad Request
    INVALID_ROOM_ID(400, "Invalid Room ID") {},
    INVALID_OWNER_ID(400, "Invalid Owner ID") {},
    INVALID_AMOUNT(400, "Invalid Amount") {},
    INVALID_DENOMINATOR(400, "Invalid denominator") {},
    ALREADY_RECEIVED_USER(400, "This user %s is already received") {},

    NO_EXIST_AMOUNT(400, "Dividend should be less than the amount: %d") {},
    COMPLETED_DIVISION(400, "This division %s is already completed") {},
    PASSED_DIVISION(400, "This division %s is already passed") {},
    OUTDATED_DIVISION(400, "This division %s is out-dated") {},

    // Not Found
    NOT_FOUND_DIVISION(404, "Could not find the division %s") {};

    private final int code;
    private final String message;
}
