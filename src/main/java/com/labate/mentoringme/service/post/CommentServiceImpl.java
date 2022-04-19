package com.labate.mentoringme.service.post;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateCommentRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.exception.CommentNotFoundException;
import com.labate.mentoringme.exception.PostNotFoundException;
import com.labate.mentoringme.model.Comment;
import com.labate.mentoringme.repository.CommentRepository;
import com.labate.mentoringme.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
  public static final int INCREASE_NUMBER = 1;
  public static final int DECREASE_NUMBER = -1;
  private final PostService postService;
  private final CommentRepository commentRepository;
  private final NotificationService notificationService;

  @Transactional
  @Override
  public Comment addNewComment(Long postId, CreateCommentRequest request, LocalUser localUser) {
    var post = postService.findPostById(postId);
    if (post == null) {
      throw new PostNotFoundException("id: " + postId);
    }
    Long commenterId = localUser.getUserId();
    var comment =
        Comment.builder()
            .postId(postId)
            .content(request.getContent())
            .createdBy(commenterId)
            .build();
    Comment newComment = save(comment);
    postService.updateCommentCount(postId, INCREASE_NUMBER);

    if (!post.getCreatedBy().equals(comment.getCreatedBy())) {
      notificationService.sendCommentNotification(newComment, post);
    }
    return newComment;
  }

  @Override
  public Page<Comment> getAllCommentsOfPost(Long postId, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return commentRepository.findAllByPostId(postId, pageable);
  }

  @Transactional
  @Override
  public void deleteComment(Long commentId, LocalUser localUser) {
    var comment = findById(commentId);
    if (comment == null) {
      throw new CommentNotFoundException("id: " + commentId);
    }
    var postId = comment.getPostId();
    checkPermissionToUpdate(comment, localUser);
    commentRepository.delete(comment);
    postService.updateCommentCount(postId, DECREASE_NUMBER);
  }

  @Override
  public Comment updateComment(Long commentId, CreateCommentRequest request, LocalUser localUser) {
    var comment = findById(commentId);
    if (comment == null) {
      throw new CommentNotFoundException("id: " + commentId);
    }
    checkPermissionToUpdate(comment, localUser);
    comment.setContent(request.getContent());
    return save(comment);
  }

  public Comment save(Comment comment) {
    return commentRepository.save(comment);
  }

  private Comment findById(Long commentId) {
    return commentRepository.findById(commentId).orElse(null);
  }

  public void checkPermissionToUpdate(Comment entity, LocalUser localUser) {
    var userId = localUser.getUserId();
    var role = localUser.getUser().getRole();

    if (!userId.equals(entity.getCreatedBy()) && !UserRole.MANAGER_ROLES.contains(role)) {
      throw new AccessDeniedException("You are not allowed to update this post");
    }
  }
}
