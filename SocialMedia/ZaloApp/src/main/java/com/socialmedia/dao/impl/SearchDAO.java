package com.socialmedia.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.ISearchDAO;
import com.socialmedia.mapping.SearchMapping;
import com.socialmedia.model.SearchModel;

public class SearchDAO extends BaseDAO<SearchModel> implements ISearchDAO {

	@Override
	public Long insertOne(SearchModel searchModel) {
		String sql = "INSERT INTO search(deleted, createddate,createdby,keyword,accountid) VALUES(?,?,?,?,?)";
		return insertOne(sql,false, searchModel.getCreatedDate(), searchModel.getCreatedBy(), searchModel.getKeyword(),
				searchModel.getAccountId());
	}

	@Override
	public List<SearchModel> findByAccountId(Long accountId) {
		List<SearchModel> searchModels = new ArrayList<SearchModel>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT * FROM search WHERE accountid = ?";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, accountId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SearchModel searchModel = new SearchModel();
				searchModel.setAccountId(resultSet.getLong("accountid"));
				searchModel.setCreatedBy(resultSet.getString("createdby"));
				searchModel.setCreatedDate(resultSet.getTimestamp("createddate"));
				searchModel.setDeleted(resultSet.getBoolean("deleted"));
				searchModel.setId(resultSet.getLong("id"));
				searchModel.setModifiedBy(resultSet.getString("modifiedby"));
				searchModel.setModifiedDate(resultSet.getTimestamp("modifieddate"));
				searchModel.setKeyword(resultSet.getString("keyword"));
				searchModels.add(searchModel);
			}
			return searchModels;
		} catch (SQLException e) {
			System.out.println("findByAccountId SearchDAO 1 : " + e.getMessage());
			return null;
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e2) {
				System.out.println("findByAccountId SearchDAO 2 : " + e2.getMessage());
				return null;
			}
		}
	}

	@Override
	public boolean deleteSearch(Long searchId) {
		String sql = "DELETE FROM search WHERE id = ?";
		return delete(sql, searchId);
	}

	@Override
	public boolean deleteByAccountId(Long accountId) {
		String sql = "DELETE FROM search WHERE accountid = ?";
		return delete(sql, accountId);
	}

	@Override
	public SearchModel findById(Long id) {
		String sql = "SELECT * FROM search WHERE id = ?";
		try {
			return findOne(sql, new SearchMapping(), id);
		} catch (ClassCastException e) {
			System.out.println("Failed findById SearchDAO : " + e.getMessage());
			return null;
		}
	}

}
