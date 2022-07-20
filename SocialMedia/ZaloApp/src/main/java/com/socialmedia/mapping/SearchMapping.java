package com.socialmedia.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.socialmedia.model.SearchModel;

public class SearchMapping implements IRowMapping<SearchModel> {

	@Override
	public SearchModel mapRow(ResultSet resultSet) {
		try {
			SearchModel searchModel = new SearchModel();
			searchModel.setId(resultSet.getLong("id"));
			searchModel.setDeleted(resultSet.getBoolean("deleted"));
			searchModel.setCreatedDate(resultSet.getTimestamp("createddate"));
			searchModel.setCreatedBy(resultSet.getString("createdby"));
			searchModel.setAccountId(resultSet.getLong("accountid"));
			searchModel.setKeyword(resultSet.getString("keyword"));
			searchModel.setModifiedBy(resultSet.getString("modifiedby"));
			searchModel.setModifiedDate(resultSet.getTimestamp("modifieddate"));
			return searchModel;
		} catch (SQLException e) {
			System.out.println("Failed Row Mapping Search Mapping : " + e.getMessage());
			return null;
		}
	}

}
