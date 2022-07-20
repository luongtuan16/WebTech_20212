package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.SearchModel;

public interface ISearchDAO extends GenericDAO<SearchModel> {
	Long insertOne(SearchModel searchModel);

	List<SearchModel> findByAccountId(Long accountId);

	boolean deleteSearch(Long searchId);

	SearchModel findById(Long id);

	boolean deleteByAccountId(Long accountId);
}
