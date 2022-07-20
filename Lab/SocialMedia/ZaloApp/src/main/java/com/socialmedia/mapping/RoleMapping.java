package com.socialmedia.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.socialmedia.model.RoleModel;

public class RoleMapping implements IRowMapping<RoleModel> {

	@Override
	public RoleModel mapRow(ResultSet resultSet) {
		try {

			RoleModel model = new RoleModel();
			model.setId(resultSet.getLong("rolekey"));
			model.setDeleted(resultSet.getBoolean("deleted"));
			model.setRole(resultSet.getString("role"));
			return model;
		} catch (SQLException e) {
			System.out.println("Failed Role Mapping : " + e.getMessage());
		}
		return null;
	}

}
