package com.labate.mentoringme.service.moderator;

import com.labate.mentoringme.dto.request.CreateModeratorRequest;
import com.labate.mentoringme.model.User;

public interface ModeratorService {
  User createModerator(CreateModeratorRequest request);
}
