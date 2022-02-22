package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_profiles")
public class UserProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Boolean gender;
  private Date dob;
  private Float rating;
  private String detailAddress;
  private Long addressId;
  private Integer status;

  @Column(columnDefinition = "BIT", length = 1)
  private Boolean isDeleted;

  @Column(name = "created", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createdDate;

  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date modifiedDate;

  @JsonIgnore
  @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private User user;
}
