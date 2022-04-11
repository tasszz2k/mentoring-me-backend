package com.labate.mentoringme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "posts")
@SQLDelete(sql = "update posts set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private Long createdBy;
  private String content;

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;

  private Date startDate;
  private Date endDate;
  private Integer type;

  @Enumerated(EnumType.ORDINAL)
  private Status status;

  private Float price;

  private String detailAddress;

  // @JsonIgnore
  // @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  // @JoinColumn(name = "address_id", referencedColumnName = "id")
  // private Address address;

  private int likeCount;
  private int commentCount;

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

  // @ManyToMany
  // @JoinTable(
  //     name = "users_like_posts",
  //     joinColumns = {@JoinColumn(name = "post_id")},
  //     inverseJoinColumns = {@JoinColumn(name = "user_id")})
  // private Set<User> users = new HashSet<>();

  public void increaseLikeCount() {
    this.likeCount++;
  }

  public void decreaseLikeCount() {
    this.likeCount--;
  }

  public enum Status {
    ON_GOING,
    COMPLETED,
    CANCELED;

    public static final Set<Status> COMPLETED_STATUSES = Set.of(COMPLETED, CANCELED);
  }
}
