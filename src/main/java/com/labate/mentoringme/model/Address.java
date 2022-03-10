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
import java.util.stream.Stream;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "addresses")
@SQLDelete(sql = "update addresses set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(name = "type")
  @Basic
  private Integer typeValue;

  @Transient private Type type;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "province_id")
  private Address province;

  @OneToMany(mappedBy = "province")
  private Set<Address> subProvinces;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "district_id")
  private Address district;

  @OneToMany(mappedBy = "district")
  private Set<Address> subDistricts;

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

  // @JsonIgnore
  // @OneToMany(mappedBy = "address")
  // private Set<UserProfile> userProfiles;

  @PostLoad
  void fillTransient() {
    if (typeValue != null) {
      this.type = Type.of(typeValue);
    }
  }

  @PrePersist
  void fillPersistent() {
    if (type != null) {
      this.typeValue = type.getValue();
    }
  }

  // ################################################################## //
  public enum Type {
    PROVINCE(1),
    DISTRICT(2),
    WARD(3);

    private final Integer value;

    Type(Integer value) {
      this.value = value;
    }

    public static Type of(Integer value) {
      return Stream.of(Type.values())
          .filter(type -> type.getValue().equals(value))
          .findFirst()
          .orElseThrow(
              () -> new IllegalArgumentException("Invalid address type with value: " + value));
    }

    public Integer getValue() {
      return value;
    }
  }
}
