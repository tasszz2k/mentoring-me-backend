package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.MentorshipMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequest;
import com.labate.mentoringme.dto.request.GetMentorshipRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.exception.MentorshipNotFoundException;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mentorship")
public class MentorshipController {
  private final MentorshipService mentorshipService;

  @GetMapping("/{id}")
  public ResponseEntity<?> findMentorshipById(@PathVariable Long id) {
    var classEntity = mentorshipService.findById(id);
    if (classEntity == null) {
      throw new MentorshipNotFoundException("id = " + id);
    }
    return BaseResponseEntity.ok(MentorshipMapper.toDto(classEntity));
  }

  @GetMapping("")
  public ResponseEntity<?> findAllMentorship(
      @Valid PageCriteria pageCriteria, @Valid GetMentorshipRequest request) {
    var page = mentorshipService.findAllMentorshipByConditions(pageCriteria, request);
    var classes = page.getContent();

    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();
    var response = new PageResponse(MentorshipMapper.toBasicDtos(classes), paging);
    return BaseResponseEntity.ok(response);
  }

  @GetMapping("/top-mentorship")
  public ResponseEntity<?> findTop10Mentorship() {

    var sort = List.of("-createdDate");
    PageCriteria pageCriteria = PageCriteria.builder().limit(10).page(1).sort(sort).build();
    GetMentorshipRequest request = GetMentorshipRequest.builder().build();
    return findAllMentorship(pageCriteria, request);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("")
  public ResponseEntity<?> createNewMentorship(
      @Valid @RequestBody CreateMentorshipRequest request, @CurrentUser LocalUser localUser) {
    request.setCreatedBy(localUser.getUserId());
    var entity = MentorshipMapper.toEntity(request);
    entity.setId(null);
    var savedEntity = mentorshipService.saveMentorship(entity);

    return BaseResponseEntity.ok(
        MentorshipMapper.toDto(savedEntity), "Mentorship request created successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PutMapping("/{id}")
  public ResponseEntity<?> updateMentorship(
      @PathVariable Long id,
      @Valid @RequestBody CreateMentorshipRequest request,
      @CurrentUser LocalUser localUser) {

    request.setId(id);
    mentorshipService.updateMentorship(request, localUser);
    return BaseResponseEntity.ok(null, "Mentorship request updated successfully");
  }
}
