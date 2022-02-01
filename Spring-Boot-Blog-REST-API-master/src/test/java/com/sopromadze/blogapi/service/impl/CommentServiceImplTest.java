package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.Comment;
import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.User;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.CommentRequest;
import com.sopromadze.blogapi.repository.CommentRepository;
import com.sopromadze.blogapi.repository.PostRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.PostService;
import com.sopromadze.blogapi.utils.AppUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @InjectMocks
    private PostServiceImpl postService;



    private User user;
    private Comment comment;
    private Post post;
    private Pageable pageable;
    private CommentRequest commentRequest;
    private UserPrincipal userPrincipal;
    @BeforeEach
    void init() {
        List<Role> rolesUser = new ArrayList<Role>();
        rolesUser.add(new Role(RoleName.ROLE_USER));

        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("user");
        user.setRoles(rolesUser);

        post = new Post();
        post.setId(1L);
        post.setUser(user);
        post.setTitle("Nuevo post");

        comment = new Comment();
        comment.setName("Nuevo comentario");
        comment.setId(1L);
        comment.setUser(user);
        comment.setPost(post);
        comment.setEmail("email@gmail.com");

        pageable = PageRequest.of(1, 1, Sort.Direction.DESC, "createdAt");

        commentRequest = new CommentRequest();
        commentRequest.setBody("Lleva la tarar un vestido blanco lleno de cascabeles");

        userPrincipal = new UserPrincipal(1L,"Alfonso", "Gallardo", "Alfonsogr", "email@gmail.com", "1234", null);
    }

    @Test
    void test_getAllCommentService () {

        AppUtils.validatePageNumberAndSize(1, 1);

        List<Comment> commentList = new ArrayList<Comment>();
        commentList.add(comment);

        Page<Comment> commentPage = new PageImpl(commentList, pageable, commentList.size());

        when(commentRepository.findByPostId(any(Long.class), any(Pageable.class))).thenReturn(commentPage);
        assertTrue(commentService.getAllComments(1L,1,10).getContent().contains(comment));
    }

    @Test
    void test_getAllCommentService_throwNullPointerExceptions () {
        assertThrows(NullPointerException.class ,
                ()->commentService.getAllComments(3L, 1, 1));
    }

    @Test
    void test_getCommentService () {
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        assertEquals(commentService.getComment(post.getId(),1L), comment);
    }

    @Test
    void deleteComment_success(){
        when(postRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(comment));

        doNothing().when(commentRepository).deleteById(isA(Long.class));

        ApiResponse apiResponse = new ApiResponse(Boolean.TRUE,"You successfully deleted comment");

        assertEquals(apiResponse,commentService.deleteComment(post.getId(), 1L,userPrincipal));
    }

    @Test
    void test_addCommentService () {
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(userRepository.getUser(any(UserPrincipal.class))).thenReturn(user);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertAll(
                ()->assertTrue(commentService.addComment(commentRequest,any(Long.class),userPrincipal).getName().equals(comment.getName())),
                ()->assertTrue(commentService.addComment(commentRequest,any(Long.class),userPrincipal).getEmail().equals(comment.getEmail())),
                ()->assertTrue(commentService.addComment(commentRequest,any(Long.class),userPrincipal).getUser().getUsername().equals(userPrincipal.getUsername())),
                ()->assertTrue(commentService.addComment(commentRequest,any(Long.class),userPrincipal).getPost().getTitle().equals(post.getTitle()))
        );
    }



}