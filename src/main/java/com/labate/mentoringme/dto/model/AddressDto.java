package com.labate.mentoringme.dto.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressDto {
  Long id;
  Integer type;
  String name;
  Long provinceId;
  String provinceName;
  Long districtId;
  String districtName;
  Long wardId;
  String wardName;
}
