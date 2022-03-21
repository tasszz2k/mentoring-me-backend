package com.labate.mentoringme.service.address;

import com.labate.mentoringme.model.Address;
import com.labate.mentoringme.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;

  @Override
  public Address findById(Long id) {
    if (id == null) {
      return null;
    }
    return addressRepository.findByIdUseFetchJoin(id);
  }

  @Override
  public List<Address> findAllProvinces() {
    return addressRepository.findAllByTypeValue(Address.Type.PROVINCE.getValue());
  }

  @Override
  public List<Address> findAllDistrictsByProvinceId(long provinceId) {
    return addressRepository.findAllByTypeValueAndProvinceId(
        Address.Type.DISTRICT.getValue(), provinceId);
  }

  @Override
  public List<Address> findAllWardsByDistrictId(long districtId) {
    return addressRepository.findAllByTypeValueAndDistrictId(
        Address.Type.WARD.getValue(), districtId);
  }
}
