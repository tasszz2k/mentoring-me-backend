package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  @Query(
      "SELECT a "
          + "FROM Address a "
          + "LEFT JOIN FETCH a.province p "
          + "LEFT JOIN FETCH a.district d "
          + "WHERE a.typeValue = :typeValue ")
  List<Address> findAllByTypeValue(int typeValue);

  @Query(
      "SELECT a "
          + "FROM Address a "
          + "LEFT JOIN FETCH a.province p "
          + "WHERE a.typeValue = :typeValue "
          + "AND a.province.id = :provinceId ")
  List<Address> findAllByTypeValueAndProvinceId(int typeValue, Long provinceId);

  @Query(
      "SELECT a "
          + "FROM Address a "
          + "LEFT JOIN FETCH a.province p "
          + "LEFT JOIN FETCH a.district d "
          + "WHERE a.id = :id ")
  Address findByIdUseFetchJoin(Long id);

  @Query(
      "SELECT a "
          + "FROM Address a "
          + "LEFT JOIN FETCH a.province p "
          + "LEFT JOIN FETCH a.district d "
          + "WHERE a.typeValue = :typeValue "
          + "AND a.district.id = :districtId ")
  List<Address> findAllByTypeValueAndDistrictId(int typeValue, Long districtId);
}
