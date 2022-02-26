package com.labate.mentoringme.service.address;

import com.labate.mentoringme.model.Address;

import java.util.List;

public interface AddressService {
  Address findById(Long id);

  List<Address> findAllProvinces();

  List<Address> findAllDistrictsByProvinceId(long provinceId);

  List<Address> findAllWardsByDistrictId(long districtId);
}
