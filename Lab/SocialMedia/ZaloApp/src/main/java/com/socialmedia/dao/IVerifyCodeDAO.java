package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.VerifyCodeModel;

public interface IVerifyCodeDAO {
	Long insertOne(VerifyCodeModel verifyCodeModel);
	List<VerifyCodeModel> findByPhoneNumber(String phoneNumber);
	boolean deleteVerifyCode(Long id);
	boolean deleteByPhoneNumber(String phoneNumber);
}
