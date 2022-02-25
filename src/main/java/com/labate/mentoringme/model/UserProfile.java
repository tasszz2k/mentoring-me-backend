package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
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

  private String bio;
  private String school;

  private Integer status;

  @Column(columnDefinition = "BIT", length = 1, nullable = false)
  private Boolean isDeleted;

  @CreatedDate
  @Column(name = "created", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createdDate;

  @LastModifiedDate
  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date modifiedDate;

  @JsonIgnore
  @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private User user;
}
