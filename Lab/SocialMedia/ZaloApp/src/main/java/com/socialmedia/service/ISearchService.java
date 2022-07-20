package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.SearchModel;

public interface ISearchService {
	Long insertOne(SearchModel searchModel);
	
	List<SearchModel> findByAccountId(Long accountId);
	
	boolean deleteByAccountId(Long accountId);
	
	boolean deleteById(Long searchId);
	
	SearchModel findOne(Long searchId, Long accountId);
}
