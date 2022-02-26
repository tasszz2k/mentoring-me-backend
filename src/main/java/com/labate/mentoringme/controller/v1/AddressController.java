package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.dto.mapper.AddressMapper;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {
  private final AddressService addressService;

  @GetMapping("/{addressId}")
  public ResponseEntity<?> findAddressById(@PathVariable Long addressId) {
    return BaseResponseEntity.ok(AddressMapper.toDto(addressService.findById(addressId)));
  }

  @GetMapping("/province")
  public ResponseEntity<?> findAllProvinces() {
    return BaseResponseEntity.ok(AddressMapper.toDtos(addressService.findAllProvinces()));
  }

  @GetMapping("/district")
  public ResponseEntity<?> findAllDistrictsByProvinceId(@RequestParam Long provinceId) {
    return BaseResponseEntity.ok(
        AddressMapper.toDtos(addressService.findAllDistrictsByProvinceId(provinceId)));
  }

  @GetMapping("/ward")
  public ResponseEntity<?> findAllWardsByDistrictId(@RequestParam Long districtId) {
    return BaseResponseEntity.ok(
        AddressMapper.toDtos(addressService.findAllWardsByDistrictId(districtId)));
  }
}
