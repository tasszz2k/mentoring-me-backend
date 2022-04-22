package com.labate.mentoringme.dto.excel;

import io.github.millij.poi.ss.model.annotations.Sheet;
import io.github.millij.poi.ss.model.annotations.SheetColumn;
import lombok.Data;

@Sheet
@Data
public class QuestionMappingDto {
  @SheetColumn("Id")
  private Long id;

  @SheetColumn("Question")
  private String question;

  @SheetColumn("Multiple Choice")
  private Boolean isMultipleChoice;

  @SheetColumn("Answer")
  private String answer;

  @SheetColumn("Correct")
  private Boolean isCorrect;
}
