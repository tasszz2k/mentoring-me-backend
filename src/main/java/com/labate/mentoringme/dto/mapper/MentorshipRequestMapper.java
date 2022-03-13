package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.BasicMentorshipRequestDto;
import com.labate.mentoringme.dto.model.MentorshipRequestDto;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.model.Class;
import com.labate.mentoringme.service.address.AddressService;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipRequestService;
import com.labate.mentoringme.service.user.UserService;
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
  private static UserService userService;

  @Autowired
  public MentorshipRequestMapper(
      MentorshipRequestService mentorshipRequestService,
      CategoryService categoryService,
      AddressService addressService,
      UserService userService) {
    MentorshipRequestMapper.mentorshipRequestService = mentorshipRequestService;
    MentorshipRequestMapper.categoryService = categoryService;
    MentorshipRequestMapper.addressService = addressService;
    MentorshipRequestMapper.userService = userService;
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
    if (entity.getMentorId() != null) {
      var mentor = userService.findUserById(entity.getMentorId()).orElseGet(null);
      if (mentor != null) {
        dto.setMentor(UserMapper.buildUserInfo(mentor));
      }
    }
    return dto;
  }

  public static BasicMentorshipRequestDto toBasicDto(Class entity) {
    if (entity == null) {
      return null;
    }

    return ObjectMapperUtils.map(entity, BasicMentorshipRequestDto.class);
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

  public static List<BasicMentorshipRequestDto> toBasicDtos(Collection<Class> entities) {

    List<BasicMentorshipRequestDto> dtos;
    if (entities == null) {
      dtos = null;
    } else {
      dtos =
          entities.stream().map(MentorshipRequestMapper::toBasicDto).collect(Collectors.toList());
    }
    return dtos;
  }

  // public static List<Class> toEntities(Collection<MentorshipRequestDto> dtos) {
  //   return dtos == null
  //       ? null
  //       : dtos.stream().map(MentorshipRequestMapper::toEntity).collect(Collectors.toList());
  // }
}
