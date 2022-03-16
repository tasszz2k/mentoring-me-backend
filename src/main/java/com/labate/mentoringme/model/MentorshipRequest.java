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
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@Table(name = "mentorship_requests")
@SQLDelete(sql = "update mentorship_requests set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class MentorshipRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne // (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "mentorship_id", referencedColumnName = "id")
  private Mentorship mentorship;

  @Column(name = "assignee_id")
  private Long assigneeId;

  @Column(name = "approver_id")
  private Long approverId;

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "assignee_role_id", referencedColumnName = "id")
  private Role assigneeRole;

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
    APPROVED,
    REJECTED,
    CANCELED;

    public static final Set<Status> COMPLETED_STATUSES = Set.of(APPROVED, REJECTED, CANCELED);
  }
}
