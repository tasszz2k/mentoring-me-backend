package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.dto.mapper.MentorshipRequestMapper;
import com.labate.mentoringme.dto.request.GetMentorshipRequestRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.exception.MentorshipRequestNotFoundException;
import com.labate.mentoringme.service.mentorshiprequest.MentorshipRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mentorship-requests")
public class MentorshipRequestController {
  private final MentorshipRequestService mentorshipRequestService;

  @GetMapping("/{id}")
  public ResponseEntity<?> findMentorshipRequestById(@PathVariable Long id) {
    var classX = mentorshipRequestService.findById(id);
    if (classX == null) {
      throw new MentorshipRequestNotFoundException("id = " + id);
    }
    return BaseResponseEntity.ok(MentorshipRequestMapper.toDto(classX));
  }

  @GetMapping("")
  public ResponseEntity<?> findAllMentorshipRequests(
      @Valid PageCriteria pageCriteria, @Valid GetMentorshipRequestRequest request) {
    var page = mentorshipRequestService.findAllClasses(pageCriteria, request);
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

  // @ApiImplicitParam(
  //         name = "Authorization",
  //         value = "Access Token",
  //         required = true,
  //         paramType = "header",
  //         dataTypeClass = String.class,
  //         example = "Bearer access_token")
  // @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  // @PostMapping("")
  // public ResponseEntity<?> addNewMentorshipRequest(@Valid @RequestBody MentorshipRequestDto
  // categoryDto) {
  //
  //   var category = MentorshipRequestMapper.toEntity(categoryDto);
  //   category.setId(null);
  //   category = categoryService.saveMentorshipRequest(category);
  //
  //   return BaseResponseEntity.ok(MentorshipRequestMapper.toDto(category));
  // }
  //
  // @ApiImplicitParam(
  //         name = "Authorization",
  //         value = "Access Token",
  //         required = true,
  //         paramType = "header",
  //         dataTypeClass = String.class,
  //         example = "Bearer access_token")
  // @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  // @PutMapping("/{categoryId}")
  // public ResponseEntity<?> updateMentorshipRequest(
  //         @PathVariable Long categoryId, @Valid @RequestBody MentorshipRequestDto categoryDto) {
  //
  //   var oldMentorshipRequest = categoryService.findById(categoryId);
  //   if (oldMentorshipRequest == null) {
  //     throw new MentorshipRequestNotFoundException("id = " + categoryId);
  //   }
  //
  //   categoryDto.setId(categoryId);
  //   var category = MentorshipRequestMapper.toEntity(categoryDto);
  //   category = categoryService.saveMentorshipRequest(category);
  //
  //   return BaseResponseEntity.ok(MentorshipRequestMapper.toDto(category));
  // }
  //
  // @ApiImplicitParam(
  //         name = "Authorization",
  //         value = "Access Token",
  //         required = true,
  //         paramType = "header",
  //         dataTypeClass = String.class,
  //         example = "Bearer access_token")
  // @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  // @DeleteMapping("/{categoryId}")
  // public ResponseEntity<?> deleteMentorshipRequest(@PathVariable Long categoryId) {
  //   var oldMentorshipRequest = categoryService.findById(categoryId);
  //   if (oldMentorshipRequest == null) {
  //     throw new MentorshipRequestNotFoundException("id = " + categoryId);
  //   }
  //
  //   categoryService.deleteMentorshipRequest(categoryId);
  //
  //   return BaseResponseEntity.ok(null, "MentorshipRequest deleted successfully");
  // }
}
