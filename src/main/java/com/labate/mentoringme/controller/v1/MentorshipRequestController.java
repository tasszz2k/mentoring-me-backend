package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.MentorshipRequestMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.exception.MentorshipRequestNotFoundException;
import com.labate.mentoringme.service.mentorshiprequest.ClassEnrollmentService;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipRequestService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mentorship-requests")
public class MentorshipRequestController {
  private final MentorshipRequestService mentorshipRequestService;
  private final ClassEnrollmentService classEnrollmentService;

  @GetMapping("/{id}")
  public ResponseEntity<?> findMentorshipRequestById(@PathVariable Long id) {
    var classEntity = mentorshipRequestService.findById(id);
    if (classEntity == null) {
      throw new MentorshipRequestNotFoundException("id = " + id);
    }
    return BaseResponseEntity.ok(MentorshipRequestMapper.toDto(classEntity));
  }

  @GetMapping("")
  public ResponseEntity<?> findAllMentorshipRequests(
      @Valid PageCriteria pageCriteria, @Valid GetMentorshipRequestRq request) {
    var page = mentorshipRequestService.findAllClassesByConditions(pageCriteria, request);
    var classes = page.getContent();

    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();
    var response = new PageResponse(MentorshipRequestMapper.toDtos(classes), paging);
    return BaseResponseEntity.ok(response);
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
  public ResponseEntity<?> addNewMentorshipRequest(
      @Valid @RequestBody CreateMentorshipRequestRq request, @CurrentUser LocalUser localUser) {
    request.setCreatedBy(localUser.getUser().getId());
    var entity = MentorshipRequestMapper.toEntity(request);
    entity.setId(null);
    var savedEntity = mentorshipRequestService.saveMentorshipRequest(entity);

    return BaseResponseEntity.ok(
        MentorshipRequestMapper.toDto(savedEntity), "Mentorship request created successfully");
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
  public ResponseEntity<?> updateMentorshipRequest(
      @PathVariable Long id,
      @Valid @RequestBody CreateMentorshipRequestRq request,
      @CurrentUser LocalUser localUser) {

    request.setId(id);
    mentorshipRequestService.updateMentorshipRequest(request, localUser);
    return BaseResponseEntity.ok(null, "Mentorship request updated successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteMentorshipRequest(
      @PathVariable Long id, @CurrentUser LocalUser localUser) {
    mentorshipRequestService.deleteMentorshipRequest(id, localUser);

    return BaseResponseEntity.ok(null, "MentorshipRequest deleted successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping("/{requestId}/student")
  public ResponseEntity<?> bookMentor(
      @PathVariable Long requestId, @CurrentUser LocalUser localUser) {
    classEnrollmentService.bookMentor(requestId, localUser.getUser().getId());

    return BaseResponseEntity.ok(
        null, "Mentorship request booked successfully. Please wait for mentor approval.");
  }
}
