package com.socialmedia.dao;

import com.socialmedia.model.ReportModel;

public interface IReportDAO {
	Long insertOne(ReportModel reportModel);
	ReportModel findOne(Long accountId,Long postId);
	boolean deleteByPostId(Long postId);
	boolean deleteByAccountId(Long accountId);
}
