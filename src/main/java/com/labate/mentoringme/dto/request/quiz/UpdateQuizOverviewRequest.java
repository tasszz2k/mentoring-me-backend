package com.labate.mentoringme.dto.request.quiz;

import java.util.Set;
import javax.validation.constraints.NotNull;
import com.labate.mentoringme.dto.model.CategoryDto;
import lombok.Data;

@Data
public class UpdateQuizOverviewRequest {
  @NotNull(message = "id must not be null")
  private Long id;
  @NotNull(message = "title must not be null")
  private String title;
  @NotNull(message = "numberOfQuestion must not be null")
  private Integer numberOfQuestion;
  @NotNull(message = "time must not be null")
  private Integer time;
  @NotNull(message = "isDraft must not be null")
  private Boolean isDraft;
  private Set<CategoryDto> categories;
}
