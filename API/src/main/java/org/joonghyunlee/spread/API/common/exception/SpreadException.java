package org.joonghyunlee.spread.API.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class SpreadException extends RuntimeException {

    private final HttpStatus code;
    private final String message;

    private SpreadException(SpreadExceptionCode exceptionCode) {
        this.code = HttpStatus.valueOf(exceptionCode.getCode());
        this.message = exceptionCode.getMessage();
    }

    private SpreadException(SpreadExceptionCode exceptionCode, Object... args) {
        this.code = HttpStatus.valueOf(exceptionCode.getCode());
        this.message = String.format(exceptionCode.getMessage(), args);
    }

    public static SpreadException getInstance(SpreadExceptionCode exceptionCode) {
        return new SpreadException(exceptionCode);
    }

    public static SpreadException getInstance(SpreadExceptionCode exceptionCode, Object... args) {
        return new SpreadException(exceptionCode, args);
    }
}
