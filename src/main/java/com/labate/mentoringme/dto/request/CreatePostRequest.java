package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.model.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CreatePostRequest {
  @ApiModelProperty(hidden = true)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private String title;
  private Long categoryId;
  private String content;
  private Date startDate;
  private Date endDate;
  private Integer type;
  private Post.Status status;
  private Float price;
  private Long addressId;
  private String detailAddress;
}
