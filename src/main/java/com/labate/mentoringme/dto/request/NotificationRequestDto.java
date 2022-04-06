package com.labate.mentoringme.dto.request;

import lombok.Data;

@Data
public class NotificationRequestDto {

    private String target;
    private String title;
    private String body;
}
