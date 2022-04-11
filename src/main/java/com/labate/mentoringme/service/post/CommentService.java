package com.labate.mentoringme.service.post;

import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreateCommentRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.model.Comment;
import org.springframework.data.domain.Page;

import javax.validation.Valid;

public interface CommentService {
  Comment addNewComment(Long postId, CreateCommentRequest request, LocalUser localUser);

  Page<Comment> getAllCommentsOfPost(Long postId, @Valid PageCriteria pageCriteria);

  void deleteComment(Long commentId, LocalUser localUser);

  Comment updateComment(Long commentId, CreateCommentRequest request, LocalUser localUser);
}
