package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.VerifyCodeModel;

public interface IVerifyCodeService {
	Long insertOne(String phoneNumber, String code);
	List<VerifyCodeModel> findByPhoneNumber(String phoneNumber);
	boolean deleteVerifyCode(Long id);
	boolean deleteByPhoneNumber(String phoneNumber);
}
