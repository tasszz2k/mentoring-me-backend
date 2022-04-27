package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.AddressDto;
import com.labate.mentoringme.model.Address;
import com.labate.mentoringme.util.ObjectMapperUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AddressMapper {

  public static AddressDto toDto(Address entity) {
    if (entity == null) {
      return null;
    }
    var province = entity.getProvince();
    var district = entity.getDistrict();
    var provinceId = province == null ? null : province.getId();
    var provinceName = province == null ? null : province.getName();
    var districtId = district == null ? null : district.getId();
    var districtName = district == null ? null : district.getName();

    return AddressDto.builder()
        .id(entity.getId())
        .name(entity.getName())
        .type(entity.getTypeValue())
        .provinceId(provinceId)
        .provinceName(provinceName)
        .districtId(districtId)
        .districtName(districtName)
        .build();
  }

  public static Address toEntity(AddressDto dto) {
    return ObjectMapperUtils.map(dto, Address.class);
  }

  public static List<AddressDto> toDtos(Collection<Address> entities) {
    return entities == null
        ? null
        : entities.stream().map(AddressMapper::toDto)
            .sorted(Comparator.comparing(AddressDto::getName))
            .collect(Collectors.toList());
  }

  public static List<Address> toEntities(Collection<AddressDto> dtos) {
    return dtos == null
        ? null
        : dtos.stream().map(AddressMapper::toEntity).collect(Collectors.toList());
  }
}
