package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.LikesModel;

public interface ILikesDAO extends GenericDAO<LikesModel> {
	Long insertOne(LikesModel likesModel);
	List<LikesModel> findByPostId(Long postId);
//	List<Long> listAccountIdLiked(Long postId);
	boolean disLike(Long postId,Long accountId);
	boolean deleteByPostId(Long postId);
	boolean deleteByAccountId(Long accountId);
}
