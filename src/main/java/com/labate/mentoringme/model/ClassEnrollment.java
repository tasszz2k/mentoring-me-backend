package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@Table(name = "class_enrollments")
@SQLDelete(sql = "update class_enrollments set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class ClassEnrollment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "class_id", referencedColumnName = "id")
  private Class classEntity;

  @Column(name = "requester_id")
  private Long requesterId;

  @Column(name = "assignee_id")
  private Long assigneeId;

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "requester_role_id", referencedColumnName = "id")
  private Role RequesterRole;

  private Date enrollDate;

  @Enumerated(EnumType.ORDINAL)
  private Status status;

  @Builder.Default
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

  public enum Status {
    ON_GOING,
    ACCEPTED,
    REJECTED,
    CANCELED,
    EXPIRED;
  }
}
