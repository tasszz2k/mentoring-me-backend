package com.labate.mentoringme.service.post;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.mapper.PostMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.request.CreatePostRequest;
import com.labate.mentoringme.dto.request.GetPostsRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.exception.CannotLikeOrUnlikeException;
import com.labate.mentoringme.exception.PostNotFoundException;
import com.labate.mentoringme.model.Post;
import com.labate.mentoringme.model.UserLikePost;
import com.labate.mentoringme.repository.PostRepository;
import com.labate.mentoringme.repository.UserLikePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
  private final PostRepository postRepository;
  private final UserLikePostRepository userLikePostRepository;

  @Override
  public Post savePost(Post post) {
    return postRepository.save(post);
  }

  @Override
  public Post findPostById(Long id) {
    return postRepository.findById(id).orElse(null);
  }

  @Override
  public Page<Post> findAllPosts(PageCriteria pageCriteria, GetPostsRequest request) {
    var pageable = PageCriteriaPageableMapper.toPageable(pageCriteria);
    return postRepository.findAllByConditions(request, pageable);
  }

  @Override
  public Post updatePost(CreatePostRequest request, LocalUser localUser) {

    var id = request.getId();
    var oldPost = findPostById(id);
    if (oldPost == null) {
      throw new PostNotFoundException("id = " + id);
    }

    checkPermissionToUpdate(oldPost, localUser);
    var post = PostMapper.toEntity(request, localUser);
    // TODO: change to fields can update instead of all fields except (id, createdBy,...)
    post.setCreatedBy(oldPost.getCreatedBy());

    return savePost(post);
  }

  @Override
  public void deletePost(Long id) {
    postRepository.deleteById(id);
  }

  @Override
  public void likePost(Long postId, Long userId) {
    var userLikePostKey = new UserLikePost.Key(postId, userId);
    var userLikePost = userLikePostRepository.findByKey(userLikePostKey);
    if (userLikePost == null) {
      var newUserLikePost = new UserLikePost();
      newUserLikePost.setKey(userLikePostKey);
      userLikePostRepository.save(newUserLikePost);
      return;
    }

    if (userLikePost.getIsDeleted()) {
      userLikePost.setIsDeleted(false);
      userLikePostRepository.save(userLikePost);
    } else {
      throw new CannotLikeOrUnlikeException("You already liked this post");
    }
  }

  @Override
  public void unlikePost(Long postId, Long userId) {
    var userLikePostKey = new UserLikePost.Key(postId, userId);
    var userLikePost = userLikePostRepository.findByKey(userLikePostKey);
    if (userLikePost == null || userLikePost.getIsDeleted()) {
      throw new CannotLikeOrUnlikeException("You haven't liked this post");
    }

    userLikePost.setIsDeleted(true);
    userLikePostRepository.save(userLikePost);
  }

  @Override
  public Post updateStatus(Long postId, Post.Status status, LocalUser localUser) {
    var post = findPostById(postId);
    if (post == null) {
      throw new PostNotFoundException("id = " + postId);
    }

    checkPermissionToUpdate(post, localUser);
    post.setStatus(status);
    return savePost(post);
  }

  public void checkPermissionToUpdate(Post entity, LocalUser localUser) {
    var userId = localUser.getUser().getId();
    var role = localUser.getUser().getRole();

    if (!userId.equals(entity.getCreatedBy()) && !UserRole.MANAGER_ROLES.contains(role)) {
      throw new AccessDeniedException("You are not allowed to update this post");
    }
  }
}
