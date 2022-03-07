package com.labate.mentoringme.exception.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseException extends RuntimeException {
    private ResponseError error;
    private Object[] params;

    public ResponseException(String message, ResponseError error) {
        this(message, null, error);
    }

    public ResponseException(String message, Throwable cause, ResponseError error) {
        this(message, cause, error, new Object[]{});
    }

    public ResponseException(String message, ResponseError error, Object... params) {
        this(message, null, error, params);
    }

    public ResponseException(String message, Throwable cause, ResponseError error, Object... params) {
        super(message, cause);
        this.error = error;
        this.params = params == null ? new Object[]{} : params;
    }
}
