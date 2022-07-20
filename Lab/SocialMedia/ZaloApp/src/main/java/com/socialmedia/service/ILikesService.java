package com.socialmedia.service;

import com.socialmedia.request.like.LikesRequest;

public interface ILikesService {
	Long insertOne(LikesRequest likesRequest);
	int findByPostId(Long postId);
	boolean checkThisUserLiked(Long accountId,Long postId);
	boolean disLike(Long postId,Long accountId);
	boolean deleteByPostId(Long postId);
	boolean deleteByAccountId(Long accountId);
}
