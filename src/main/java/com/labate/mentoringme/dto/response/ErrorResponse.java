package com.labate.mentoringme.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represent http response body
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> extends ServiceResponse<T> {
    private String error;

    @Builder
    public ErrorResponse(int code, T data, String message, String error) {
        super(code, data, message);
        this.error = error;
    }
}
