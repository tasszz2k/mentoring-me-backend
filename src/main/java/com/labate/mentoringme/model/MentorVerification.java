package com.labate.mentoringme.model;

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
@Table(name = "mentor_verifications")
@SQLDelete(sql = "update mentor_verifications set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class MentorVerification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long mentorId;
  private Long moderatorId;

  @Enumerated(EnumType.ORDINAL)
  private Status status;

  @Temporal(TemporalType.TIMESTAMP)
  private Date verifiedDate;

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
    IN_PROGRESS,
    ACCEPTED,
    REJECTED;
  }
}
