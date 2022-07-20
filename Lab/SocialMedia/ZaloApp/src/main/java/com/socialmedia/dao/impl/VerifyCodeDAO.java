package com.socialmedia.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.IVerifyCodeDAO;
import com.socialmedia.model.VerifyCodeModel;

public class VerifyCodeDAO extends BaseDAO<VerifyCodeModel> implements IVerifyCodeDAO {

	@Override
	public Long insertOne(VerifyCodeModel verifyCodeModel) {
		String sql = "INSERT INTO verify_code(deleted,createddate,phonenumber,vcode) VALUES(?,?,?,?)";
		return insertOne(sql, verifyCodeModel.isDeleted(),verifyCodeModel.getCreatedDate(),
				verifyCodeModel.getPhoneNumber(),verifyCodeModel.getCode());
	}

	@Override
	public List<VerifyCodeModel>  findByPhoneNumber(String phoneNumber) {
		List<VerifyCodeModel> listCode = new ArrayList<VerifyCodeModel>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT * FROM verify_code WHERE phonenumber = ?";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, phoneNumber);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				VerifyCodeModel verifyCodeModel = new VerifyCodeModel();
				verifyCodeModel.setCreatedBy(resultSet.getString("createdby"));
				verifyCodeModel.setCreatedDate(resultSet.getTimestamp("createddate"));
				verifyCodeModel.setDeleted(resultSet.getBoolean("deleted"));
				verifyCodeModel.setId(resultSet.getLong("id"));
				verifyCodeModel.setModifiedBy(resultSet.getString("modifiedby"));
				verifyCodeModel.setModifiedDate(resultSet.getTimestamp("modifieddate"));
				verifyCodeModel.setPhoneNumber(resultSet.getString("phonenumber"));
				verifyCodeModel.setCode(resultSet.getString("vcode"));
				listCode.add(verifyCodeModel);
			}
			return listCode;
		} catch (SQLException e) {
			System.out.println("findByPhoneNumber commentDAO 1 : "+e.getMessage());
			return null;
		}finally {
			try {
				if(preparedStatement != null) {
					preparedStatement.close();
				}
				if(resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e2) {
				System.out.println("findByPhoneNumber commentDAO 2 : "+e2.getMessage());
				return null;
			}
		}
	}

	@Override
	public boolean deleteVerifyCode(Long id) {
		String sql = "DELETE FROM verify_code WHERE id = ?";
		return delete(sql, id);
	}

	@Override
	public boolean deleteByPhoneNumber(String phoneNumber) {
		String sql = "DELETE FROM verify_code WHERE phonenumber = ?";
		return delete(sql, phoneNumber);
	}
	
}
