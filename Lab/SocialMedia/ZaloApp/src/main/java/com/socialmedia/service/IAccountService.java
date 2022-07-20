package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.AccountModel;
import com.socialmedia.request.account.SignInRequest;
import com.socialmedia.request.account.SignUpRequest;

public interface IAccountService {
	AccountModel signUp(SignUpRequest signUpRequest);
	String signIn(SignInRequest signInRequest);
	AccountModel findByPhoneNumber(String phoneNumber);
	AccountModel findById(Long id);
	List<AccountModel> findListAccountByKeyword(String keyword,String token);
	boolean changePassword(String token,String password,String newPassword);
	boolean activeAccount(Long id);
	boolean deactiveAccount(Long id);
	boolean updateAccount(AccountModel account);
	List<AccountModel> listSuggestedAccounts(Long id);
	List<AccountModel> findAll();
	boolean setRole(Long accountId, String role);
	boolean setRole(Long accountId, Long roleId);
	boolean deleteAccount(Long accountId);
	List<AccountModel> searchAccount(String keyword);
}
