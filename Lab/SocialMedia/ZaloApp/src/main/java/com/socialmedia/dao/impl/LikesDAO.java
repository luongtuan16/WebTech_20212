package com.socialmedia.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.ILikesDAO;
import com.socialmedia.model.LikesModel;

public class LikesDAO extends BaseDAO<LikesModel> implements ILikesDAO {

	@Override
	public Long insertOne(LikesModel likesModel) {
		String sql = "INSERT INTO likes(deleted,createddate,createdby,postid,accountid) VALUES(?,?,?,?,?)";
		return insertOne(sql, likesModel.isDeleted(), likesModel.getCreatedDate(), likesModel.getCreatedBy(),
				likesModel.getPostId(), likesModel.getAccountId());
	}

	@Override
	public List<LikesModel> findByPostId(Long postId) {
		List<LikesModel> likesModels = new ArrayList<LikesModel>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT * FROM likes WHERE postid = ?";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, postId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				LikesModel likesModel = new LikesModel();
				likesModel.setAccountId(resultSet.getLong("accountid"));
				likesModel.setCreatedBy(resultSet.getString("createdby"));
				likesModel.setCreatedDate(resultSet.getTimestamp("createddate"));
				likesModel.setDeleted(resultSet.getBoolean("deleted"));
				likesModel.setId(resultSet.getLong("id"));
				likesModel.setModifiedBy(resultSet.getString("modifiedby"));
				likesModel.setModifiedDate(resultSet.getTimestamp("modifieddate"));
				likesModel.setPostId(resultSet.getLong("postid"));
				likesModels.add(likesModel);
			}
			return likesModels;
		} catch (SQLException e) {
			System.out.println("findByPostId LikesDAO 1 : " + e.getMessage());
			return null;
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException | NullPointerException e2) {
				System.out.println("findByPostId LikesDAO 2 : " + e2.getMessage());
				return null;
			}
		}
	}

	@Override
	public boolean disLike(Long postId, Long accountId) {
		String sql = "DELETE FROM likes WHERE postid = ? AND accountid = ?";
		return delete(sql, postId, accountId);
	}

	@Override
	public boolean deleteByPostId(Long postId) {
		String sql = "DELETE FROM likes WHERE postid = ?";
		return delete(sql, postId);
	}

	@Override
	public boolean deleteByAccountId(Long accountId) {
		String sql = "DELETE FROM likes WHERE accountid = ?";
		return delete(sql, accountId);
	}
	
}
