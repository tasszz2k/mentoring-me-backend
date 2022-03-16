package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.model.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CreatePostRequest {
  @ApiModelProperty(hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private String title;
  private Long categoryId;
  private String content;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  private Integer type;

  private Post.Status status = Post.Status.ON_GOING;
  private Float price;
  private Long addressId;
  private String detailAddress;
}
