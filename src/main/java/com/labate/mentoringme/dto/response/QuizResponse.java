package com.labate.mentoringme.dto.response;

import java.util.Set;
import com.labate.mentoringme.dto.model.CategoryDto;
import lombok.Data;

@Data
public class QuizResponse {
  private Long id;
  private String title;
  private Integer numberOfQuestion;
  private Integer time;
  private Boolean isDraft;
  private String author;
  private Set<CategoryDto> categories;
  private Boolean isLiked;
}
