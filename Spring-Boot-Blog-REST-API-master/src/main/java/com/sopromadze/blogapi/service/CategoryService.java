package com.sopromadze.blogapi.service;

import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.model.Category;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

	PagedResponse<Category> getAllCategories(int page, int size);

	Category getCategory(Long id);

	Category addCategory(Category category, UserPrincipal currentUser);

	Category updateCategory(Long id, Category newCategory, UserPrincipal currentUser)
			throws UnauthorizedException;

	void deleteCategory(Long id, UserPrincipal currentUser) throws UnauthorizedException;

}
