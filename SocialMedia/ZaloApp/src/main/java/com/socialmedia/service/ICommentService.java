package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.CommentModel;
import com.socialmedia.request.comment.AddCommentRequest;

public interface ICommentService {
	Long insertOne(AddCommentRequest addCommentRequest);
	int findByPostId(Long postId);
	boolean deleteComment(Long postId,Long commentId);
	List<CommentModel> findAll();
	CommentModel findById (Long id);
	CommentModel update(Long id,String content);
	List<CommentModel> getListCommentByPostId(Long postId);
	boolean deleteByPostId(Long postId);
	boolean deleteByAccountId(Long accountId);
}
