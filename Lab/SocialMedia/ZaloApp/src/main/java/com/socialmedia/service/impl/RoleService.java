package com.socialmedia.service.impl;

import com.socialmedia.dao.IRoleDAO;
import com.socialmedia.dao.impl.RoleDAO;
import com.socialmedia.model.RoleModel;
import com.socialmedia.service.IRoleService;

public class RoleService implements IRoleService {
	private IRoleDAO roleDAO;
	
	public RoleService() {
		roleDAO = new RoleDAO();
	}
	@Override
	public Long findId(String role) {
		RoleModel roleModel = roleDAO.findByRoleString(role);
		if (roleModel != null)
			return roleModel.getId();
		return -1L;
	}
	@Override
	public RoleModel findById(Long id) {
		return roleDAO.findbyId(id);
	}

}
