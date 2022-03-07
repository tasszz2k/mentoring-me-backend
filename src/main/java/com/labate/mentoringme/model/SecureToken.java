package com.labate.mentoringme.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "secure_tokens")
public class SecureToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String token;

  @CreationTimestamp
  @Column(updatable = false)
  private Timestamp timeStamp;

  @Column(updatable = false)
  @Basic(optional = false)
  private LocalDateTime expireAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Transient private boolean isExpired;

  public boolean isExpired() {

    return getExpireAt()
        .isBefore(
            LocalDateTime
                .now()); // this is generic implementation, can always make it timezone specific
  }
}
