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
import java.util.HashSet;
import java.util.Set;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "mentorship")
@SQLDelete(sql = "update mentorship set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Mentorship {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long mentorId;

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;

  private Long createdBy;
  private String title;
  private Date startDate;
  private Date endDate;
  private Integer duration;
  private Long type;

  @Enumerated(EnumType.ORDINAL)
  private Status status;

  private Float price;

  private String detailAddress;

  @JsonIgnore
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private Address address;

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

  @OneToMany(mappedBy = "mentorshipId", cascade = CascadeType.REMOVE)
  private Set<Shift> shifts = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "mentorship_requests",
      joinColumns = {@JoinColumn(name = "mentorship_id")},
      inverseJoinColumns = {@JoinColumn(name = "requester_id")})
  private Set<User> users = new HashSet<>();

  public enum Status {
    ON_GOING,
    FOUND,
    COMPLETED,
    CANCELED,
    EXPIRED;
  }
}
