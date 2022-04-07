package com.labate.mentoringme.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labate.mentoringme.constant.Gender;
import com.labate.mentoringme.constant.MentorStatus;
import com.labate.mentoringme.constant.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class FindUsersRequest {
  private String keyword;

  private UserRole role;
  private Boolean enabled;
  private Set<Gender> genders;
  private Float minPrice;
  private Float maxPrice;
  private Boolean isOfflineStudy;
  private Boolean isOnlineStudy;
  private Float ratingFrom;

  private List<Long> categoryIds;
  private List<Long> addressIds;

  @JsonIgnore // FIXME: config swagger to hidden this field
  public String getRoleName() {
    return role != null ? role.name() : null;
  }

  @JsonIgnore // FIXME: config swagger to hidden this field
  public MentorStatus getMentorStatus() {
    return UserRole.ROLE_MENTOR.equals(role) ? MentorStatus.ACCEPTED : null;
  }

  @JsonIgnore // FIXME: config swagger to hidden this field
  public Set<Integer> getGenderValues() {
    if (genders == null) {
      return null;
    }
    return genders.stream().map(Gender::getValue).collect(Collectors.toSet());
  }
}
