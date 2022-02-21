package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

  boolean existsByEmail(String email);
}
