package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.PostModel;
import com.socialmedia.request.post.AddPostRequest;

public interface IPostService {
	Long insertOne(AddPostRequest addPostRequest);
	PostModel findPostById(Long id);
	PostModel findById(Long id);
	boolean deleteById(Long id);
	List<PostModel> findAll();
	Long findAccountIdByPostId(Long id);
	List<PostModel> findPostByAccountId(Long accountId);
	boolean updateContenById(Long id, String content);
}
