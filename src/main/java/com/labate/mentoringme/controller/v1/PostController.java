package com.labate.mentoringme.controller.v1;

import com.labate.mentoringme.config.CurrentUser;
import com.labate.mentoringme.dto.mapper.CommentMapper;
import com.labate.mentoringme.dto.mapper.PostMapper;
import com.labate.mentoringme.dto.mapper.UserMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.*;
import com.labate.mentoringme.dto.response.BaseResponseEntity;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.model.Post;
import com.labate.mentoringme.service.post.CommentService;
import com.labate.mentoringme.service.post.PostService;
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
@RequestMapping("/api/v1/posts")
public class PostController {
  private final PostService postService;
  private final CommentService commentService;

  @GetMapping("/{id}")
  public ResponseEntity<?> findPostById(@PathVariable Long id, @CurrentUser LocalUser localUser) {
    var dto = postService.findPostDtoById(id, localUser);
    return BaseResponseEntity.ok(dto);
  }

  @GetMapping("")
  public ResponseEntity<?> findAllPostsByConditions(
      @Valid PageCriteria pageCriteria,
      @Valid GetPostsRequest request,
      @CurrentUser LocalUser localUser) {
    PageResponse response =
        postService.findAllPostDtosByConditions(pageCriteria, request, localUser);
    return BaseResponseEntity.ok(response);
  }

  @GetMapping("/top-posts")
  public ResponseEntity<?> findTop10Posts(@CurrentUser LocalUser localUser) {

    var sort = List.of("-createdDate");
    PageCriteria pageCriteria = PageCriteria.builder().limit(10).page(1).sort(sort).build();
    GetPostsRequest request = GetPostsRequest.builder().status(Post.Status.ON_GOING).build();
    return findAllPostsByConditions(pageCriteria, request, localUser);
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping("")
  public ResponseEntity<?> addNewPost(
      @Valid @RequestBody CreatePostRequest request, @CurrentUser LocalUser localUser) {
    request.setId(null);
    request.setStatus(Post.Status.ON_GOING);
    var entity = PostMapper.toEntity(request, localUser);
    var savedEntity = postService.savePost(entity);

    return BaseResponseEntity.ok(
        PostMapper.toDto(savedEntity), "Post has been created successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('USER')")
  @PutMapping("/{id}")
  public ResponseEntity<?> updatePost(
      @PathVariable Long id,
      @Valid @RequestBody CreatePostRequest request,
      @CurrentUser LocalUser localUser) {
    request.setId(id);
    postService.updatePost(request, localUser);
    return BaseResponseEntity.ok(null, "Post updated successfully");
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
  public ResponseEntity<?> updatePostStatus(
      @PathVariable Long id,
      @Valid @RequestBody UpdatePostStatusRequest request,
      @CurrentUser LocalUser localUser) {
    postService.updateStatus(id, request.getStatus(), localUser);
    return BaseResponseEntity.ok(null, "Post status updated successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("/{id}/like")
  public ResponseEntity<?> likePost(@PathVariable Long id, @CurrentUser LocalUser localUser) {
    postService.likePost(id, localUser.getUserId());
    return BaseResponseEntity.ok(null, "Post liked successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PostMapping("/{id}/unlike")
  public ResponseEntity<?> unlikePost(@PathVariable Long id, @CurrentUser LocalUser localUser) {
    postService.unlikePost(id, localUser.getUserId());
    return BaseResponseEntity.ok(null, "Post unliked successfully");
  }

  @GetMapping("/{id}/like")
  public ResponseEntity<?> getAllUsersLikePost(@PathVariable Long id) {
    var users = postService.getAllUserLikePost(id);
    return BaseResponseEntity.ok(UserMapper.toUserInfos(users));
  }

  // ============================ COMMENTS ===========================//
  @GetMapping("{postId}/comments")
  public ResponseEntity<?> getAllCommentsOfPost(
      @PathVariable Long postId, @Valid PageCriteria pageCriteria) {
    var page = commentService.getAllCommentsOfPost(postId, pageCriteria);
    var comments = page.getContent();

    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();
    var response = new PageResponse(CommentMapper.toDtos(comments), paging);
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
  @PostMapping("{postId}/comments")
  public ResponseEntity<?> addNewComment(
      @PathVariable Long postId,
      @Valid @RequestBody CreateCommentRequest request,
      @CurrentUser LocalUser localUser) {

    var savedEntity = commentService.addNewComment(postId, request, localUser);

    return BaseResponseEntity.ok(
        CommentMapper.toDto(savedEntity), "Comment has been created successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @DeleteMapping("comments/{commentId}")
  public ResponseEntity<?> deleteComment(
      @PathVariable Long commentId, @CurrentUser LocalUser localUser) {

    commentService.deleteComment(commentId, localUser);

    return BaseResponseEntity.ok("Comment has been deleted successfully");
  }

  @ApiImplicitParam(
      name = "Authorization",
      value = "Access Token",
      required = true,
      paramType = "header",
      dataTypeClass = String.class,
      example = "Bearer access_token")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'MENTOR', 'USER')")
  @PatchMapping("comments/{commentId}")
  public ResponseEntity<?> updateComment(
      @PathVariable Long commentId,
      @Valid @RequestBody CreateCommentRequest request,
      @CurrentUser LocalUser localUser) {

    commentService.updateComment(commentId, request, localUser);

    return BaseResponseEntity.ok(null, "Comment has been updated successfully");
  }
}
