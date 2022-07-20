package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.socialmedia.dao.IAccountDAO;
import com.socialmedia.dao.ICommentDAO;
import com.socialmedia.dao.IFileDAO;
import com.socialmedia.dao.ILikesDAO;
import com.socialmedia.dao.IPostDAO;
import com.socialmedia.dao.IReportDAO;
import com.socialmedia.dao.impl.AccountDAO;
import com.socialmedia.dao.impl.CommentDAO;
import com.socialmedia.dao.impl.FileDAO;
import com.socialmedia.dao.impl.LikesDAO;
import com.socialmedia.dao.impl.PostDAO;
import com.socialmedia.dao.impl.ReportDAO;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.PostModel;
import com.socialmedia.request.post.AddPostRequest;
import com.socialmedia.service.IPostService;

public class PostService extends BaseService implements IPostService {

	private IPostDAO postDAO;
	private IAccountDAO accountDAO;
	
	public PostService() {
		postDAO = new PostDAO();
		accountDAO = new AccountDAO();
	}

	@SuppressWarnings("unused")
	@Override
	public Long insertOne(AddPostRequest addPostRequest) {
		String described = addPostRequest.getDescribed();
		String phoneNumber = getPhoneNumberFromToken(addPostRequest.getToken());
		System.out.println(phoneNumber);
		AccountModel model = accountDAO.findByPhoneNumber(phoneNumber);
		
		PostModel postModel = new PostModel();
		Long accountId = model.getId();
		postModel.setAccountId(accountId);
		postModel.setContent(described);
		postModel.setCreatedBy(model.getPhoneNumber());
		postModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		postModel.setDeleted(false);
		Long id = postDAO.insertOne(postModel);
		return id;
	}

	@Override
	public PostModel findPostById(Long id) {
		PostModel postModel = postDAO.findPostById(id);
		// System.out.println(postDAO.findPostById(id));
		if (postModel == null || postModel.isDeleted())
			return null;
		else
			return postModel;
		// return postDAO.findPostById(id);
	}

	@Override
	public PostModel findById(Long id) {
		return postDAO.findById(id);
	}

//	@Override
//	public boolean deleteById(Long id) {
//		PostModel model = findById(id);
//		try {
//			if (model.isDeleted() == false) {
//				return postDAO.deleteById(id);
//
//			} else {
//				return false;
//			}
//		} catch (NullPointerException e) {
//			return false;
//		}
//	}
	@Override
	public boolean deleteById(Long id) {
		PostModel model = findById(id);
		if (model != null)
			try {
				IFileDAO fileDAO = new FileDAO();
				ILikesDAO likesDAO= new LikesDAO();
				IReportDAO reportDAO = new ReportDAO();
				ICommentDAO commentDAO = new CommentDAO();
				commentDAO.deleteByPostId(id);
				fileDAO.deleteByPostId(id);
				likesDAO.deleteByPostId(id);
				reportDAO.deleteByPostId(id);
				return postDAO.deleteById(id);
			} catch (NullPointerException e) {
				return false;
			}
		return false;
	}

	@Override
	public List<PostModel> findAll() {
		return postDAO.findAll();
	}

	@Override
	public Long findAccountIdByPostId(Long id) {
		return postDAO.findAccountIdByPostId(id);
	}

	@Override
	public List<PostModel> findPostByAccountId(Long accountId) {
//		List<PostModel> list = postDAO.findPostByAccountId(accountId);
//		List<PostModel> list2 = new ArrayList<PostModel>();
//		for (int i = 0; i < list.size() - 1; i++) {
//			PostModel model = list.get(i);
//			if (model.getId() == list.get(i + 1).getId()) {
//				continue;
//			} else {
//				list2.add(model);
//			}
//
//		}
//		list2.add(list.get(list.size() - 1));
//		return list2;
		return postDAO.findPostByAccountId(accountId);
	}

	@Override
	public boolean updateContenById(Long id, String content) {
		return postDAO.updateContentById(id, content);
	}

}
