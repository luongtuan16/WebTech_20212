package com.socialmedia.dao;

import com.socialmedia.model.RoleModel;

public interface IRoleDAO {
	RoleModel findByRoleString(String role);
	RoleModel findbyId(Long id);
}
