package com.labate.mentoringme.service.post;

import com.labate.mentoringme.constant.UserRole;
import com.labate.mentoringme.dto.mapper.PageCriteriaPageableMapper;
import com.labate.mentoringme.dto.mapper.PostMapper;
import com.labate.mentoringme.dto.model.LocalUser;
import com.labate.mentoringme.dto.model.PostDto;
import com.labate.mentoringme.dto.request.CreatePostRequest;
import com.labate.mentoringme.dto.request.GetPostsRequest;
import com.labate.mentoringme.dto.request.PageCriteria;
import com.labate.mentoringme.dto.response.PageResponse;
import com.labate.mentoringme.dto.response.Paging;
import com.labate.mentoringme.exception.CannotLikeOrUnlikeException;
import com.labate.mentoringme.exception.PostNotFoundException;
import com.labate.mentoringme.model.Post;
import com.labate.mentoringme.model.User;
import com.labate.mentoringme.model.UserLikePost;
import com.labate.mentoringme.repository.PostRepository;
import com.labate.mentoringme.repository.UserLikePostRepository;
import com.labate.mentoringme.service.notification.NotificationService;
import com.labate.mentoringme.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
  private final PostRepository postRepository;
  private final UserLikePostRepository userLikePostRepository;
  private final UserService userService;
  private final NotificationService notificationService;

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

  @Transactional
  @Override
  public void likePost(Long postId, Long userId) {
    var post = findPostById(postId);
    if (post == null) {
      throw new PostNotFoundException("id = " + postId);
    }

    var userLikePostKey = new UserLikePost.Key(postId, userId);
    var userLikePost = userLikePostRepository.findByKey(userLikePostKey);
    if (userLikePost == null) {
      var newUserLikePost = new UserLikePost();
      newUserLikePost.setKey(userLikePostKey);
      userLikePostRepository.save(newUserLikePost);
      notificationService.sendLikePostNotification(post, userId);
    } else {
      if (userLikePost.getIsDeleted()) {
        userLikePost.setIsDeleted(false);
        userLikePostRepository.save(userLikePost);
      } else {
        throw new CannotLikeOrUnlikeException("You already liked this post");
      }
    }
    post.increaseLikeCount();
    savePost(post);
  }

  @Transactional
  @Override
  public void unlikePost(Long postId, Long userId) {
    var post = findPostById(postId);
    if (post == null) {
      throw new PostNotFoundException("id = " + postId);
    }

    var userLikePostKey = new UserLikePost.Key(postId, userId);
    var userLikePost = userLikePostRepository.findByKey(userLikePostKey);
    if (userLikePost == null || userLikePost.getIsDeleted()) {
      throw new CannotLikeOrUnlikeException("You haven't liked this post");
    }

    userLikePost.setIsDeleted(true);
    userLikePostRepository.save(userLikePost);

    post.decreaseLikeCount();
    savePost(post);
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

  @Override
  public List<User> getAllUserLikePost(Long id) {
    var userLikePosts = userLikePostRepository.findAllByKeyPostIdAndIsDeletedIsFalse(id);
    var userIds =
        userLikePosts.stream()
            .map(userLikePost -> userLikePost.getKey().getUserId())
            .collect(Collectors.toSet());
    return userService.findAllByIds(userIds);
  }

  @Override
  public void updateCommentCount(Long postId, int number) {
    postRepository.updateCommentCount(postId, number);
  }

  public void checkPermissionToUpdate(Post entity, LocalUser localUser) {
    var userId = localUser.getUserId();
    var role = localUser.getUser().getRole();

    if (!userId.equals(entity.getCreatedBy()) && !UserRole.MANAGER_ROLES.contains(role)) {
      throw new AccessDeniedException("You are not allowed to update this post");
    }
  }

  @Override
  public PostDto findPostDtoById(Long postId, LocalUser localUser) {
    var post = findPostById(postId);
    if (post == null) {
      throw new PostNotFoundException("id = " + postId);
    }
    var dto = PostMapper.toDto(post);
    if (localUser != null) {
      var key = new UserLikePost.Key(postId, localUser.getUserId());
      var isLiked = userLikePostRepository.existsByKeyAndIsDeletedIsFalse(key);
      dto.setIsLiked(isLiked);
    }
    return dto;
  }

  @Override
  public PageResponse findAllPostDtosByConditions(
      PageCriteria pageCriteria, GetPostsRequest request, LocalUser localUser) {

    var page = findAllPosts(pageCriteria, request);
    var posts = page.getContent();

    var paging =
        Paging.builder()
            .limit(pageCriteria.getLimit())
            .page(pageCriteria.getPage())
            .total(page.getTotalElements())
            .build();
    var dtos = PostMapper.toDtos(posts);
    if (localUser != null) {
      updateUserLikePosts(localUser, posts, dtos);
    }

    var response = new PageResponse(dtos, paging);
    return response;
  }

  private void updateUserLikePosts(LocalUser localUser, List<Post> posts, List<PostDto> dtos) {
    var postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
    var userId = localUser.getUserId();
    var keys =
        postIds.stream().map(id -> new UserLikePost.Key(id, userId)).collect(Collectors.toSet());
    var userLikePosts = userLikePostRepository.findAllByKeyIn(keys);
    var map =
        userLikePosts.stream()
            .collect(Collectors.toMap(u -> u.getKey().getPostId(), u -> !u.getIsDeleted()));
    dtos.forEach(
        dto -> {
          var isLiked = map.get(dto.getId());
          isLiked = isLiked != null && isLiked;
          dto.setIsLiked(isLiked);
        });
  }
}
