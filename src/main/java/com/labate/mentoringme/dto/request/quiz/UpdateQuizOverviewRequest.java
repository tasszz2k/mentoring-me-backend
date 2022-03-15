package com.labate.mentoringme.dto.request.quiz;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateQuizOverviewRequest {
  @NotNull private Long id;
  @NotNull private String title;
  @NotNull private Integer time;
  @NotNull private Boolean isDraft;
  @NotNull private List<Long> categoryIds;
}
