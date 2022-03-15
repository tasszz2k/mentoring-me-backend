package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.labate.mentoringme.model.Post;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)))
public class UpdatePostStatusRequest {
  @NotNull Post.Status status;
}
