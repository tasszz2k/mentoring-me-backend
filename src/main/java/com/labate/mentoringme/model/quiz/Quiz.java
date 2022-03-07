package com.labate.mentoringme.model.quiz;

import com.labate.mentoringme.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "quizzes")
@SQLDelete(sql = "update quizzes set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Quiz {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String description;

  private Integer numberOfQuestion;

  private Integer time;

  @Column(name = "visible_status", columnDefinition = "BIT", length = 1)
  private Boolean visibleStatus;

  @Column(name = "editable_status", columnDefinition = "BIT", length = 1)
  private Boolean editableStatus;

  @Column(name = "is_deleted", columnDefinition = "BIT", length = 1)
  private Boolean isDeleted = false;

  @Column(name = "is_draft", columnDefinition = "BIT", length = 1)
  private Boolean isDraft;

  @Column(name = "created_by")
  private Long createdBy;

  private Integer type;

  private Long modifiedBy;

  @CreatedDate
  @Column(name = "created", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @LastModifiedDate
  @Column(name = "modified")
  @Temporal(TemporalType.TIMESTAMP)
  private Date modifiedDate;

  @OneToMany(
      mappedBy = "quiz",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private Set<Question> questions;

  @JsonIgnore
  @ManyToMany(cascade = CascadeType.MERGE)
  @JoinTable(
      name = "quizzes_categories",
      joinColumns = {@JoinColumn(name = "quiz_id")},
      inverseJoinColumns = {@JoinColumn(name = "category_id")})
  private Set<Category> categories;

  private String author;
}
