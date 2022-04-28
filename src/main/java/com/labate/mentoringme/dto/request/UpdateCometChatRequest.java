package com.labate.mentoringme.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCometChatRequest {
  private String userId;
  private String imgUrl;
  private String name;
}
