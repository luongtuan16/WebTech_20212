package com.socialmedia.dao.impl;

import com.socialmedia.dao.IReportDAO;
import com.socialmedia.mapping.ReportMapping;
import com.socialmedia.model.ReportModel;

public class ReportDAO extends BaseDAO<ReportModel> implements IReportDAO {

	@Override
	public Long insertOne(ReportModel reportModel) {
		String sql = "INSERT INTO report(deleted,createddate,createdby,details,accountid,postid,typereportid) VALUES(?,?,?,?,?,?,?)";
		Long id = insertOne(sql, reportModel.isDeleted(), reportModel.getCreatedDate(), reportModel.getCreatedBy(),
				reportModel.getDetails(), reportModel.getAccountId(), reportModel.getPostId(),
				reportModel.getTypeReportId());
		if (id > 0) {
			return id;
		}
		return -1L;
	}

	@Override
	public ReportModel findOne(Long accountId, Long postId) {
		try {
			String sql = "SELECT * FROM report WHERE accountid = ? AND postid = ?";
			return findOne(sql, new ReportMapping(), accountId, postId);
		} catch (ClassCastException e) {
			System.out.println("Failed FindOne ReportDAO 1 : " + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean deleteByPostId(Long postId) {
		String sql = "DELETE FROM report WHERE postid = ?";
		return delete(sql, postId);
	}

	@Override
	public boolean deleteByAccountId(Long accountId) {
		String sql = "DELETE FROM report WHERE accountid = ?";
		return delete(sql, accountId);
	}
}
