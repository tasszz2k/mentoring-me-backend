package com.labate.mentoringme.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidInputResponse extends ErrorResponse<Void> {

    private Set<FieldErrorResponse> errors;

    public InvalidInputResponse(int code, String message, String error, Set<FieldErrorResponse> errors) {
        super(code, message, null, error);
        this.errors = errors;
    }
}
