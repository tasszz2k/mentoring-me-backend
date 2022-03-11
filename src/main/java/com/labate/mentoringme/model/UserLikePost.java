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
@Table(name = "users_like_posts")
@SQLDelete(sql = "update users_like_posts set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class UserLikePost {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "post_id")
  private Long postId;

  @Column(name = "user_id")
  private Long userId;

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
}
