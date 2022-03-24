package com.labate.mentoringme.repository;

import com.labate.mentoringme.dto.projection.BasicUserInfoProjection;
import com.labate.mentoringme.dto.request.FindUsersRequest;
import com.labate.mentoringme.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

  boolean existsByEmail(String email);

  @Query(
      value =
          "SELECT DISTINCT user "
              + "FROM User user "
              + "JOIN user.roles role "
              + "JOIN user.userProfile userProfile "
              + "LEFT JOIN userProfile.categories category "
              + "LEFT JOIN userProfile.address address "
              + "WHERE (:#{#request.getRoleName()} IS NULL OR role.name = :#{#request.getRoleName()}) "
              + "AND (:#{#request.getMentorStatus()} IS NULL OR user.status = :#{#request.getMentorStatus()}) "
              + "AND (:#{#request.getEnabled()} IS NULL OR user.enabled = :#{#request.getEnabled()}) "
              + "AND (COALESCE(:#{#request.categoryIds}, NULL) IS NULL OR category.id IN (:#{#request.categoryIds})) "
              + "AND (COALESCE(:#{#request.addressIds}, NULL) IS NULL OR address.id IN (:#{#request.addressIds})) ",
      countQuery =
          "SELECT COUNT (DISTINCT user) "
              + "FROM User user "
              + "JOIN user.roles role "
              + "JOIN user.userProfile userProfile "
              + "LEFT JOIN userProfile.categories category "
              + "LEFT JOIN userProfile.address address "
              + "WHERE (:#{#request.getRoleName()} IS NULL OR role.name = :#{#request.getRoleName()}) "
              + "AND (:#{#request.getMentorStatus()} IS NULL OR user.status = :#{#request.getMentorStatus()}) "
              + "AND (:#{#request.getEnabled()} IS NULL OR user.enabled = :#{#request.getEnabled()}) "
              + "AND (COALESCE(:#{#request.categoryIds}, NULL) IS NULL OR category.id IN (:#{#request.categoryIds})) "
              + "AND (COALESCE(:#{#request.addressIds}, NULL) IS NULL OR address.id IN (:#{#request.addressIds})) ")
  Page<User> findAllByConditions(FindUsersRequest request, Pageable pageable);

  List<User> findAllByIdInAndEnabledIsTrue(Collection<Long> userIds);

  @Query(
      value =
          "SELECT users.id           AS id,\n"
              + "       users.email        AS email,\n"
              + "       users.full_name    AS fullName,\n"
              + "       users.phone_number AS phoneNumber,\n"
              + "       users.image_url    AS imageUrl,\n"
              + "       roles.name         AS roles\n"
              + "\n"
              + "FROM users\n"
              + "         LEFT JOIN users_roles ON users_roles.user_id = users.id\n"
              + "         LEFT JOIN roles ON roles.id = users_roles.role_id\n"
              + "WHERE users.id = :userId",
      nativeQuery = true)
  BasicUserInfoProjection findBasicUserInfoById(Long userId);

  @Query(
      value =
          "SELECT users.id           AS id,\n"
              + "       users.email        AS email,\n"
              + "       users.full_name    AS fullName,\n"
              + "       users.phone_number AS phoneNumber,\n"
              + "       users.image_url    AS imageUrl,\n"
              + "       roles.name         AS roles\n"
              + "\n"
              + "FROM users\n"
              + "         LEFT JOIN users_roles ON users_roles.user_id = users.id\n"
              + "         LEFT JOIN roles ON roles.id = users_roles.role_id\n"
              + "WHERE users.id IN (:userIds)",
      nativeQuery = true)
  List<BasicUserInfoProjection> findBasicUserInfoByIdIn(List<Long> userIds);
}
