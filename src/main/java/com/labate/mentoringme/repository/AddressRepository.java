package com.labate.mentoringme.repository;

import com.labate.mentoringme.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
  List<Address> findAllByTypeValue(int typeValue);

  List<Address> findAllByTypeValueAndProvinceId(int typeValue, Long provinceId);

  List<Address> findAllByTypeValueAndDistrictId(int typeValue, Long provinceId);
}
