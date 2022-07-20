package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.AccountModel;
import com.socialmedia.request.account.SignInRequest;

public interface IAccountDAO extends GenericDAO<AccountModel> {
	boolean signUp(AccountModel accountModel);

	AccountModel signIn(SignInRequest signInRequest);

	AccountModel findByPhoneNumber(String phoneNumber);

	AccountModel findById(Long id);

	List<AccountModel> findListAccountByKeyword(String keyword);

	boolean changePassword(AccountModel accountModel);
	
	boolean activeAccount(Long id);
	
	boolean deactiveAccount(Long id);
	
	boolean updateAccount(AccountModel account);

	List<AccountModel> findAll();
	
	boolean setRole(Long acccountId, Long roleId);
	
	boolean deleteAccount(Long accountId);
	
	List<AccountModel> searchAccount(String keyword);
}
