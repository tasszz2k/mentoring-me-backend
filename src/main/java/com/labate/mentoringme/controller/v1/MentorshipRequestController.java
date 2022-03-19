package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.MentorshipRequestMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateMentorshipRequestRq;
import com.labate.mentoringme.dto.request.GetMentorshipRequestRq;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.request.UpdateMentorshipRequestStatusRequest;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.exception.MentorshipRequestNotFoundException;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipRequestService;
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
@RequestMapping("/api/v1/mentorship-requests")
public class MentorshipRequestController {
  private final MentorshipRequestService mentorshipRequestService;

  @GetMapping("/{id}")
  public ResponseEntity<?> findMentorshipRequestById(@PathVariable Long id) {
    var mentorshipRequest = mentorshipRequestService.findById(id);
    if (mentorshipRequest == null) {
      throw new MentorshipRequestNotFoundException("id = " + id);
    }
    return BaseResponseEntity.ok(MentorshipRequestMapper.toDto(mentorshipRequest));
  }

  @GetMapping("")
  public ResponseEntity<?> findAllMentorship(
      @Valid PageCriteria pageCriteria, @Valid GetMentorshipRequestRq request) {
    var page = mentorshipRequestService.findAllMentorshipRequestByConditions(pageCriteria, request);
    var mentorshipRequests = page.getContent();

    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();
    var response = new PageResponse(MentorshipRequestMapper.toDtos(mentorshipRequests), paging);
    return BaseResponseEntity.ok(response);
  }

  @GetMapping("/top-mentorship-requests")
  public ResponseEntity<?> findTop10MentorshipRequests() {

    var sort = List.of("-createdDate");
    PageCriteria pageCriteria = PageCriteria.builder().limit(10).page(1).sort(sort).build();
    GetMentorshipRequestRq request = GetMentorshipRequestRq.builder().build();
    return findAllMentorship(pageCriteria, request);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
  @PostMapping("")
  public ResponseEntity<?> createNewMentorshipRequest(
      @Valid @RequestBody CreateMentorshipRequestRq request, @CurrentUser LocalUser localUser) {
    request.getMentorship().setCreatedBy(localUser.getUser().getId());
    var savedEntity = mentorshipRequestService.createNewMentorshipRequest(request);

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
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
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
  @PatchMapping("/{id}/status")
  public ResponseEntity<?> updateMentorshipRequestStatus(
      @PathVariable Long id,
      @Valid @RequestBody UpdateMentorshipRequestStatusRequest request,
      @CurrentUser LocalUser localUser) {
    mentorshipRequestService.updateStatus(id, request, localUser);
    return BaseResponseEntity.ok(null, "Mentorship request status updated successfully");
  }
}
