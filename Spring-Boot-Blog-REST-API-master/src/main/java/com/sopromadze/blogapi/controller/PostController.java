package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.model.Post;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.payload.PostRequest;
import com.sopromadze.blogapi.payload.PostResponse;
import com.sopromadze.blogapi.security.CurrentUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.PostService;
import com.sopromadze.blogapi.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {


	private final PostService postService;

	@GetMapping
	public PagedResponse<Post> getAllPosts(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {
		PagedResponse<Post> response = postService.getAllPosts(page, size);

		return response;
	}

	@GetMapping("/category/{id}")
	public PagedResponse<Post> getPostsByCategory(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long id) {
		PagedResponse<Post> response = postService.getPostsByCategory(id, page, size);

		return response;
	}

	@GetMapping("/tag/{id}")
	public PagedResponse<Post> getPostsByTag(
			@RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size,
			@PathVariable(name = "id") Long id) {
		PagedResponse<Post> response = postService.getPostsByTag(id, page, size);

		return response;
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public PostResponse addPost(@Valid @RequestBody PostRequest postRequest,
			@CurrentUser UserPrincipal currentUser) {
		PostResponse postResponse = postService.addPost(postRequest, currentUser);

		return postResponse;
	}

	@GetMapping("/{id}")
	public Post getPost(@PathVariable(name = "id") Long id) {
		Post post = postService.getPost(id);

		return post;
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Post updatePost(@PathVariable(name = "id") Long id,
			@Valid @RequestBody PostRequest newPostRequest, @CurrentUser UserPrincipal currentUser) {
		Post post = postService.updatePost(id, newPostRequest, currentUser);

		return post;
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ApiResponse deletePost(@PathVariable(name = "id") Long id, @CurrentUser UserPrincipal currentUser) {
		ApiResponse apiResponse = postService.deletePost(id, currentUser);

		return apiResponse;
	}
}
