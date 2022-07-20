package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.socialmedia.dao.IAccountDAO;
import com.socialmedia.dao.ICommentDAO;
import com.socialmedia.dao.impl.AccountDAO;
import com.socialmedia.dao.impl.CommentDAO;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.CommentModel;
import com.socialmedia.request.comment.AddCommentRequest;
import com.socialmedia.service.ICommentService;

public class CommentService extends BaseService implements ICommentService {

	private ICommentDAO commentDAO;
	private IAccountDAO accountDAO;

	public CommentService() {
		commentDAO = new CommentDAO();
		accountDAO = new AccountDAO();
	}

	@Override
	public Long insertOne(AddCommentRequest addCommentRequest) {
		CommentModel commentModel = new CommentModel();
		commentModel.setAccountId(addCommentRequest.getAccountId());
		commentModel.setContent(addCommentRequest.getComment());
		commentModel.setPostId(addCommentRequest.getId());
		commentModel.setDeleted(false);
		commentModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		AccountModel accountModel = accountDAO.findById(commentModel.getAccountId());
		commentModel.setCreatedBy(accountModel.getPhoneNumber());
		return commentDAO.insertOne(commentModel);
	}

	@Override
	public int findByPostId(Long postId) {
		List<CommentModel> commentModels = commentDAO.findByPostId(postId);
		if(commentModels != null) {
			return commentModels.size();	
		}else {
			return 0;
		}
	}

	@Override
	public boolean deleteComment(Long postId, Long commentId) {
		return commentDAO.deleteComment(postId, commentId);
	}

	@Override
	public List<CommentModel> findAll() {
		return commentDAO.findAll();
	}

	@Override
	public CommentModel findById(Long id) {
		return commentDAO.findById(id);
	}

	@Override
	public CommentModel update(Long id,String content) {
		boolean b = commentDAO.update(id, content);
		if(b ==true) {
			return findById(id);
		}else {
			return null;	
		}
	}

	@Override
	public List<CommentModel> getListCommentByPostId(Long postId) {
		return commentDAO.findByPostId(postId);
	}

	@Override
	public boolean deleteByPostId(Long postId) {
		return commentDAO.deleteByPostId(postId);
	}

	@Override
	public boolean deleteByAccountId(Long accountId) {
		return commentDAO.deleteByAccountId(accountId);
	}

}
