package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.labate.mentoringme.model.Post;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class PostDto {
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private String title;
  private CategoryDto category;
  private String content;
  private Date startDate;
  private Date endDate;
  private Integer type;
  private Post.Status status;
  private Float price;
  private String detailAddress;
  private AddressDto address;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long createdBy;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Set<Integer> likedUserIds;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Date createdDate;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Date modifiedDate;
}
