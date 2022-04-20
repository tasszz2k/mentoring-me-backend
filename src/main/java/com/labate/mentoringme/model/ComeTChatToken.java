package com.labate.mentoringme.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cometchat_tokens")
@SQLDelete(sql = "update cometchat_tokens set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
@Builder
@AllArgsConstructor
public class ComeTChatToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;

  private String token;

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
}
