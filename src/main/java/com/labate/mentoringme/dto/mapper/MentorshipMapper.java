package com.labate.mentoringme.dto.mapper;

import com.labate.mentoringme.dto.model.BasicMentorshipDto;
import com.labate.mentoringme.dto.model.MentorshipDto;
import com.labate.mentoringme.dto.request.CreateMentorshipRequest;
import com.labate.mentoringme.model.Mentorship;
import com.labate.mentoringme.service.address.AddressService;
import com.labate.mentoringme.service.category.CategoryService;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipService;
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
public class MentorshipMapper {

  private static MentorshipService mentorshipService;
  private static CategoryService categoryService;
  private static AddressService addressService;
  private static UserService userService;

  @Autowired
  public MentorshipMapper(
      MentorshipService mentorshipService,
      CategoryService categoryService,
      AddressService addressService,
      UserService userService) {
    MentorshipMapper.mentorshipService = mentorshipService;
    MentorshipMapper.categoryService = categoryService;
    MentorshipMapper.addressService = addressService;
    MentorshipMapper.userService = userService;
  }

  public static MentorshipDto toDto(Mentorship entity) {
    if (entity == null) {
      return null;
    }
    var dto = ObjectMapperUtils.map(entity, MentorshipDto.class);

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

  public static BasicMentorshipDto toBasicDto(Mentorship entity) {
    if (entity == null) {
      return null;
    }

    return ObjectMapperUtils.map(entity, BasicMentorshipDto.class);
  }

  public static Mentorship toEntity(CreateMentorshipRequest dto) {
    if (dto == null) {
      return null;
    }
    var entity = ObjectMapperUtils.map(dto, Mentorship.class);
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

  public static List<MentorshipDto> toDtos(Collection<Mentorship> entities) {

    List<MentorshipDto> dtos;
    if (entities == null) {
      dtos = null;
    } else {
      dtos = entities.stream().map(MentorshipMapper::toDto).collect(Collectors.toList());
    }
    return dtos;
  }

  public static List<BasicMentorshipDto> toBasicDtos(Collection<Mentorship> entities) {

    List<BasicMentorshipDto> dtos;
    if (entities == null) {
      dtos = null;
    } else {
      dtos = entities.stream().map(MentorshipMapper::toBasicDto).collect(Collectors.toList());
    }
    return dtos;
  }

  // public static List<Class> toEntities(Collection<MentorshipRequestDto> dtos) {
  //   return dtos == null
  //       ? null
  //       : dtos.stream().map(MentorshipRequestMapper::toEntity).collect(Collectors.toList());
  // }
}
