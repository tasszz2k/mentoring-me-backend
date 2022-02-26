package com.labate.mentoringme.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
