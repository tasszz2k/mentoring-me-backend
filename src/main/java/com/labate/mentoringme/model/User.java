package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labate.mentoringme.constant.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/** The persistent class for the user database table. */
@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements Serializable {

  /***/
  private static final long serialVersionUID = 65981149772133526L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String phoneNumber;

  private String password;
  private String hash;
  private String fullName;
  private String imageUrl;
  private String provider;
  private String providerUserId;

  private Integer status;

  @Column(name = "verified_email", columnDefinition = "BIT", length = 1)
  private boolean verifiedEmail;

  @Column(name = "verified_phone_number", columnDefinition = "BIT", length = 1)
  private boolean verifiedPhoneNumber;

  @Column(name = "enabled", columnDefinition = "BIT", length = 1)
  private boolean enabled = true;

  @CreatedDate
  @Column(name = "created", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @LastModifiedDate
  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedDate;

  // bidirectional many-to-many association to Role
  @JsonIgnore
  @ManyToMany
  @JoinTable(
      name = "users_roles",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private Set<Role> roles;

  @JsonIgnore
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "profile_id")
  private UserProfile userProfile;

  // @JsonIgnore
  // @OneToMany(mappedBy = "user")
  // private Set<SecureToken> tokens;

  public UserRole getRole() {
    return roles.stream().findFirst().map(Role::getName).map(UserRole::valueOf).orElse(null);
  }
}
