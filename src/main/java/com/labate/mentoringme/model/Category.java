package com.labate.mentoringme.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
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

  // @ManyToOne
  // @JoinColumn(name = "parent_category_id")
  private Long parentCategoryId;

  // @JsonIgnore
  // @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  // private Set<Category> subCategories;

  @Column(name = "name", length = 50)
  private String name;

  @Column(name = "code", length = 10)
  private String code;

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

  // @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
  // private Set<UserProfile> userProfiles;

  // @ManyToMany(mappedBy = "categories", fetch = FetchType.EAGER)
  // private Set<Quiz> quizzes;
}
