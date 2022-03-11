package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.labate.mentoringme.constant.MentorshipRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "classes")
@SQLDelete(sql = "update classes set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Class {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long mentorId;

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;

  private Long createdBy;
  private String title;
  private Date startDate;
  private Date endDate;
  private Integer duration;
  private Long type;

  @Enumerated(EnumType.ORDINAL)
  private MentorshipRequestStatus status;

  private Float price;

  private String detailAddress;

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

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

  @OneToMany(mappedBy = "classId", cascade = CascadeType.REMOVE)
  private Set<Shift> shifts = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "class_enrollments",
      joinColumns = {@JoinColumn(name = "class_id")},
      inverseJoinColumns = {@JoinColumn(name = "requester_id")})
  private Set<User> users = new HashSet<>();
}
