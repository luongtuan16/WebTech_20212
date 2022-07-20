package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.FriendModel;

public interface IFriendService {
	Long insertOne(Long idRequest,Long idRequested);
	List<FriendModel> findListFriendRequestByIdA(Long idA);
	List<FriendModel> findListFriendRequestByIdB(Long idB);
	boolean checkFriendExisted(Long idRequest,Long idRequested,boolean isFriend); //isFriend = true
	boolean checkRequestExisted(Long idRequest,Long idRequested,boolean isFriend);//isFriend = false
	boolean setIsFriend(Long idRequest,Long idRequested);
	boolean deleteRequest(Long idRequest,Long idRequested);
	FriendModel findOne(Long idRequest,Long idRequested);
	List<FriendModel> findListFriendById(Long id);
	int sameFriends(Long idA, Long idB);
	
	boolean deleteUserFriends(Long accountId);
}
