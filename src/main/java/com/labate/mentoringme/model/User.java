package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/** The persistent class for the user database table. */
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

  private String email;
  private String phoneNumber;
  private String password;
  private String hash;
  private String fullName;
  private String imageUrl;
  private String provider;
  private String providerUserId;

  private Integer status;

  @Column(name = "enabled", columnDefinition = "BIT", length = 1)
  private boolean enabled;

  @Column(name = "created", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  protected Date createdDate;

  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  protected Date modifiedDate;

  // bidirectional many-to-many association to Role
  @JsonIgnore
  @ManyToMany
  @JoinTable(
      name = "users_roles",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private Set<Role> roles;

  @JsonIgnore
  @OneToOne
  @MapsId
  @JoinColumn(name = "profile_id")
  private UserProfile userProfile;
}
