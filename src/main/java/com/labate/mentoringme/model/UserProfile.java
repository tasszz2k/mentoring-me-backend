package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labate.mentoringme.constant.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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

  @Column(name = "gender")
  @Basic
  private Integer genderValue;

  @Transient private Gender gender;

  private Date dob;
  private Float rating;
  private String detailAddress;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;

  private String bio;
  private String school;

  private Float price;

  private Integer status;

  @Column(columnDefinition = "BIT", length = 1, nullable = false)
  private Boolean isOnlineStudy;

  @Column(columnDefinition = "BIT", length = 1, nullable = false)
  private Boolean isOfflineStudy;

  @Column(columnDefinition = "BIT", length = 1, nullable = false)
  private Boolean isDeleted = false;

  @CreatedDate
  @Column(name = "created", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @LastModifiedDate
  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedDate;

  @JsonIgnore
  @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private User user;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "profiles_categories",
      joinColumns = {@JoinColumn(name = "profile_id")},
      inverseJoinColumns = {@JoinColumn(name = "category_id")})
  private Set<Category> categories;

  @PostLoad
  void fillTransient() {
    if (genderValue != null) {
      this.gender = Gender.of(genderValue);
    }
  }

  @PrePersist
  void fillPersistent() {
    if (gender != null) {
      this.genderValue = gender.getValue();
    }
  }

  public void setGender(Gender gender) {
    this.gender = gender;
    this.genderValue = gender != null ? gender.getValue() : null;
  }
}
