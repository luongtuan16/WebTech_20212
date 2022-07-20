package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.CommentModel;

public interface ICommentDAO extends GenericDAO<CommentModel>{
	Long insertOne(CommentModel commentModel);
	List<CommentModel> findByPostId(Long postId);
	boolean deleteComment(Long postId,Long commentId);
	List<CommentModel> findAll();
	CommentModel findById (Long id);
	boolean update(Long id,String content);
	boolean deleteByPostId(Long postId);
	boolean deleteByAccountId(Long accountId);
}
