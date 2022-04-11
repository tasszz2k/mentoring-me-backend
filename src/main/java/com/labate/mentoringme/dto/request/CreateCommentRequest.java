package com.labate.mentoringme.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateCommentRequest {
  @NotBlank private String content;
}
