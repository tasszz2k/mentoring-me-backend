package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.dto.model.ShiftDto;
import com.labate.mentoringme.model.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.util.Date;
import java.util.Set;

@Data
public class CreateEventRequest {
  @ApiModelProperty(hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;
  private Long mentorshipId;
  private String title;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  @Valid
  private Set<ShiftDto> shifts;

  private Integer type;
}
