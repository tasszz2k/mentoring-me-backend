package com.labate.mentoringme.model.quiz;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "questions")
@SQLDelete(sql = "update questions set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // @JsonIgnore
  // @ManyToOne()
  // @JoinColumn(name = "quiz_id", referencedColumnName = "id", insertable = true, updatable = true)
  // private Quiz quiz;
  private Long quizId;

  private String question;

  private String description;

  @Column(name = "is_multiple_choice", columnDefinition = "BIT", length = 1)
  private Boolean isMultipleChoice;

  @Column(name = "is_deleted", columnDefinition = "BIT", length = 1)
  private Boolean isDeleted = false;

  @CreatedDate
  @Column(name = "created", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @LastModifiedDate
  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedDate;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true,
      fetch = FetchType.EAGER)
  private Set<Answer> answers;
}
