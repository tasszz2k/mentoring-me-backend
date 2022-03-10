package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.MentorshipRequestDto;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.model.Class;
import com.labate.mentoringme.service.address.AddressService;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipRequestService;
import com.labate.mentoringme.service.mentorshiprequest.ShiftService;
import com.labate.mentoringme.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MentorshipRequestMapper {

  private static MentorshipRequestService mentorshipRequestService;
  private static CategoryService categoryService;
  private static AddressService addressService;
  private static ShiftService shiftService;

  @Autowired
  public MentorshipRequestMapper(
      MentorshipRequestService mentorshipRequestService,
      CategoryService categoryService,
      AddressService addressService,
      ShiftService shiftService) {
    MentorshipRequestMapper.mentorshipRequestService = mentorshipRequestService;
    MentorshipRequestMapper.categoryService = categoryService;
    MentorshipRequestMapper.addressService = addressService;
    MentorshipRequestMapper.shiftService = shiftService;
  }

  public static MentorshipRequestDto toDto(Class entity) {
    if (entity == null) {
      return null;
    }
    var dto = ObjectMapperUtils.map(entity, MentorshipRequestDto.class);

    if (entity.getAddress() != null) {
      var address = AddressMapper.toDto(entity.getAddress());
      dto.setAddress(address);
    }
    return dto;
  }

  public static Class toEntity(CreateMentorshipRequestRq dto) {
    if (dto == null) {
      return null;
    }
    var entity = ObjectMapperUtils.map(dto, Class.class);
    if (dto.getCategoryId() != null) {
      var category = categoryService.findById(dto.getCategoryId());
      entity.setCategory(category);
    }
    if (dto.getAddressId() != null) {
      var address = addressService.findById(dto.getAddressId());
      entity.setAddress(address);
    }
    if (!CollectionUtils.isEmpty(dto.getShifts())) {
      var shifts = ShiftMapper.toEntities(dto.getShifts());
      entity.setShifts(new HashSet<>(shifts));
    }
    return entity;
  }

  public static List<MentorshipRequestDto> toDtos(Collection<Class> entities) {

    List<MentorshipRequestDto> dtos;
    if (entities == null) {
      dtos = null;
    } else {
      dtos = entities.stream().map(MentorshipRequestMapper::toDto).collect(Collectors.toList());
    }
    return dtos;
  }

  // public static List<Class> toEntities(Collection<MentorshipRequestDto> dtos) {
  //   return dtos == null
  //       ? null
  //       : dtos.stream().map(MentorshipRequestMapper::toEntity).collect(Collectors.toList());
  // }
}
