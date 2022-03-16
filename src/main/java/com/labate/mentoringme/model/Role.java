package com.labate.mentoringme.model;

import com.labate.mentoringme.constant.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/** The persistent class for the role database table. */
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  private String name;

  // bidirectional many-to-many association to User
  // @ManyToMany(mappedBy = "roles")
  // private Set<User> users;

  public Role(String name) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Role role = (Role) obj;
    return role.equals(role.name);
  }

  @Override
  public String toString() {
    return "Role [name=" + name + "]" + "[id=" + id + "]";
  }

  public UserRole getUserRole() {
    return UserRole.of(name);
  }
}
