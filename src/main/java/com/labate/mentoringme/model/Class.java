package com.labate.mentoringme.model;

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
  private Long categoryId;
  private Long createdBy;
  private String title;
  private Date startDate;
  private Date endDate;
  private Integer duration;
  private Long type;
  private Long status;
  private Float price;

  private String detailAddress;

  @ManyToOne
  @JoinColumn(name = "address_id")
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

  @ManyToMany
  @JoinTable(
      name = "classes_shifts",
      joinColumns = {@JoinColumn(name = "class_id")},
      inverseJoinColumns = {@JoinColumn(name = "shift_id")})
  private Set<StaticShift> shifts = new HashSet<>();
}
