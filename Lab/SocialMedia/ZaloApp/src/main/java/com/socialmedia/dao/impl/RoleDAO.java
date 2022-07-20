package com.socialmedia.dao.impl;

import com.socialmedia.dao.IRoleDAO;
import com.socialmedia.mapping.RoleMapping;
import com.socialmedia.model.RoleModel;

public class RoleDAO extends BaseDAO<RoleModel> implements IRoleDAO{

	@Override
	public RoleModel findByRoleString(String role) {
		String sqlString = "SELECT * FROM role WHERE role = ?";
		return findOne(sqlString, new RoleMapping(), role);
	}

	@Override
	public RoleModel findbyId(Long id) {
		String sql = "SELECT * FROM role WHERE rolekey = ?";
		return findOne(sql, new RoleMapping(), id);
	}
	
}
