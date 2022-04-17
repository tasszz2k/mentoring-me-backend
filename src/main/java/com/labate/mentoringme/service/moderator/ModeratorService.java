package com.labate.mentoringme.service.moderator;

import com.labate.mentoringme.dto.request.CreateModeratorRequest;
import com.labate.mentoringme.dto.response.BasicInforResponse;

public interface ModeratorService {
  BasicInforResponse createModerator(CreateModeratorRequest request);
}
