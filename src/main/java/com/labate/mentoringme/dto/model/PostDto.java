package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.model.Post;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Data
public class PostDto {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private String title;
  private CategoryDto category;
  private String content;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  private Integer type;
  private Post.Status status;
  private Float price;
  private String detailAddress;
  private AddressDto address;
  private int likeCount;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private BasicUserInfo createdBy;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Set<Integer> likedUserIds;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createdDate;

  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd HH:mm:ss",
      timezone = "Asia/Bangkok")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date modifiedDate;
}
