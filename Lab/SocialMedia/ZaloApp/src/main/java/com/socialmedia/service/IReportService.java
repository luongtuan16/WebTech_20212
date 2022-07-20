package com.socialmedia.service;

public interface IReportService {
	Long insertOne(Long accountId,Long postId,Long typeReportId,String details);
	boolean checkReported(Long accountId,Long postId);
	boolean deleteByPostId(Long postId);
	boolean deleteByAccountId(Long accountId);
}
