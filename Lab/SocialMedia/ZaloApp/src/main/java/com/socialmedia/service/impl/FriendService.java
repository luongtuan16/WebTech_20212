package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.IFriendDAO;
import com.socialmedia.dao.impl.FriendDAO;
import com.socialmedia.model.FriendModel;
import com.socialmedia.service.IFriendService;

public class FriendService implements IFriendService {
	private IFriendDAO friendDAO;

	public FriendService() {
		friendDAO = new FriendDAO();
	}

	@Override
	public Long insertOne(Long idRequest, Long idRequested) {
		FriendModel friendModel = new FriendModel();
		friendModel.setCreatedBy(idRequest.toString());
		friendModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		friendModel.setDeleted(false);
		friendModel.setFriend(false);
		friendModel.setIdA(idRequest);
		friendModel.setIdB(idRequested);
		return friendDAO.insertOne(friendModel);
	}

	@Override
	public List<FriendModel> findListFriendRequestByIdA(Long idA) {
		return friendDAO.findListFriendRequestByIdA(idA);
	}

	@Override
	public boolean checkFriendExisted(Long idRequest, Long idRequested, boolean isFriend) {
		FriendModel friendModel = friendDAO.findOne(idRequest, idRequested, isFriend);
		FriendModel friendModel1 = friendDAO.findOne(idRequested, idRequest, isFriend);
		if (friendModel != null || friendModel1 != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkRequestExisted(Long idRequest, Long idRequested, boolean isFriend) {
		FriendModel friendModel = friendDAO.findOne(idRequest, idRequested, isFriend);
		if (friendModel != null) {
			return true;
		}
		return false;

	}

	@Override
	public boolean setIsFriend(Long idRequest, Long idRequested) {
		return friendDAO.setIsFriend(idRequest, idRequested);
	}

	@Override
	public boolean deleteRequest(Long idRequest, Long idRequested) {
		return friendDAO.deleteRequest(idRequest, idRequested);
	}

	@Override
	public FriendModel findOne(Long idRequest, Long idRequested) {
		return friendDAO.findOne(idRequest, idRequested);
	}

	@Override
	public List<FriendModel> findListFriendById(Long id) {
		return friendDAO.findListFriendById(id);
	}

	@Override
	public List<FriendModel> findListFriendRequestByIdB(Long idB) {
		return friendDAO.findListFriendRequestByIdB(idB);
	}

	@Override
	public int sameFriends(Long idA, Long idB) {
		List<FriendModel> listfrsA = findListFriendById(idA);
		List<FriendModel> listfrsB = findListFriendById(idB);
		List<Long> listId1 = new ArrayList<Long>();
		List<Long> listId2 = new ArrayList<Long>();
		int count = 0;

		for (FriendModel friendModel : listfrsA) {
			if (friendModel.getIdA() != idA && friendModel.getIdA() != idB)
				listId1.add(friendModel.getIdA());
			else if (friendModel.getIdB() != idA && friendModel.getIdB() != idB)
				listId1.add(friendModel.getIdB());
		}
		for (FriendModel friendModel : listfrsB) {
			if (friendModel.getIdA() != idA && friendModel.getIdA() != idB)
				listId2.add(friendModel.getIdA());
			else if (friendModel.getIdB() != idA && friendModel.getIdB() != idB)
				listId2.add(friendModel.getIdB());
		}
		for (Long i : listId1)
			for (Long j : listId2)
				if (i == j)
					count++;
		return count;
	}

	@Override
	public boolean deleteUserFriends(Long accountId) {
		return friendDAO.deleteUserFriends(accountId);
	}
}
