package com.labate.mentoringme.dto.request.quiz;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportQuizRequest {
  @NotBlank
  private String title;
  @NotNull
  private Integer numberOfQuestion;
  @NotNull
  private Integer time;
  @NotNull
  private Boolean isDraft;
  @NotNull
  private String categories;

  private MultipartFile file;
}
