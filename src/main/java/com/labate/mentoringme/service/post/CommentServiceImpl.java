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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
  private final PostService postService;
  private final CommentRepository commentRepository;

  @Override
  public Comment addNewComment(Long postId, CreateCommentRequest request, LocalUser localUser) {
    var post = postService.findPostById(postId);
    if (post == null) {
      throw new PostNotFoundException("id: " + postId);
    }
    var comment =
        Comment.builder()
            .postId(postId)
            .content(request.getContent())
            .createdBy(localUser.getUserId())
            .build();
    return save(comment);
  }

  @Override
  public Page<Comment> getAllCommentsOfPost(Long postId, PageCriteria pageCriteria) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return commentRepository.findAllByPostId(postId, pageable);
  }

  @Override
  public void deleteComment(Long commentId, LocalUser localUser) {
    var comment = findById(commentId);
    if (comment == null) {
      throw new CommentNotFoundException("id: " + commentId);
    }
    checkPermissionToUpdate(comment, localUser);
    commentRepository.delete(comment);
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
