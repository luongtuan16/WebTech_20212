package com.socialmedia.service;

import com.socialmedia.model.RoleModel;

public interface IRoleService {
	Long findId(String role);
	RoleModel findById(Long id);
}
