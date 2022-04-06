package com.labate.mentoringme.dto.response;

import com.labate.mentoringme.dto.model.NotificationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetNotificationsResponse {
  private List<NotificationDto> notifications;
  private Integer unreadNotificationsCounter;
}
