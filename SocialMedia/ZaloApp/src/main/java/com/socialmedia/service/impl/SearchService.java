package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.socialmedia.dao.ISearchDAO;
import com.socialmedia.dao.impl.SearchDAO;
import com.socialmedia.model.SearchModel;
import com.socialmedia.service.ISearchService;

public class SearchService implements ISearchService {
	private ISearchDAO searchDAO;
	public SearchService() {
		searchDAO=new SearchDAO();
	}
	@Override
	public Long insertOne(SearchModel searchModel) {
		searchModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		return searchDAO.insertOne(searchModel);
	}

	@Override
	public List<SearchModel> findByAccountId(Long accountId) {
		return searchDAO.findByAccountId(accountId);
	}

	@Override
	public boolean deleteByAccountId(Long accountId) {
		return searchDAO.deleteByAccountId(accountId);
	}
	@Override
	public boolean deleteById(Long searchId) {
		return searchDAO.deleteSearch(searchId);
	}
	@Override
	public SearchModel findOne(Long searchId, Long accountId) {
		SearchModel searchModel = searchDAO.findById(searchId);
		if (searchModel!=null &&searchModel.getAccountId() == accountId)
			return searchModel;
		else 
			return null;
	}

}
