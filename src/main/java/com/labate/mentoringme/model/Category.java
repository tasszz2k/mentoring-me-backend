package com.labate.mentoringme.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.labate.mentoringme.model.quiz.Quiz;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

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

  @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
  private Set<Quiz> quizzes;
}
