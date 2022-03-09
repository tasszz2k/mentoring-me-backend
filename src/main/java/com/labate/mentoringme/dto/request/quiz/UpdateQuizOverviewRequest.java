package com.labate.mentoringme.dto.request.quiz;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateQuizOverviewRequest {
  @NotNull
  private Long id;
  @NotNull
  private String title;
  @NotNull
  private Integer time;
  @NotNull
  private Boolean isDraft;
  @NotNull
  private List<Long> categoryIds;
}
