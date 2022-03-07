package com.labate.mentoringme.model;

import com.labate.mentoringme.model.quiz.Quiz;
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
@Getter
@Setter
@Table(name = "categories")
@SQLDelete(sql = "update categories set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category_id")
  private Category parentCategory;

  @JsonIgnore
  @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
  private Set<Category> subCategories;

  @Column(name = "name", length = 50)
  private String name;

  @Column(name = "code", length = 10)
  private String code;

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

  @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
  private Set<UserProfile> userProfiles;

  @JsonIgnore
  @ManyToMany(mappedBy = "categories")
  private Set<Quiz> quizzes;
}
