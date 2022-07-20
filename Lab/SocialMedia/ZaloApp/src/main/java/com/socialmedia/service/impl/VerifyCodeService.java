package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.socialmedia.dao.IVerifyCodeDAO;
import com.socialmedia.dao.impl.VerifyCodeDAO;
import com.socialmedia.model.VerifyCodeModel;
import com.socialmedia.service.IVerifyCodeService;

public class VerifyCodeService implements IVerifyCodeService {
	
	private IVerifyCodeDAO verifyCodeDAO;
	
	public VerifyCodeService() {
		verifyCodeDAO = new VerifyCodeDAO();
	}
	@Override
	public Long insertOne(String phoneNumber, String code) {
		VerifyCodeModel verifyCodeModel = new VerifyCodeModel();
		verifyCodeModel.setCode(code);
		verifyCodeModel.setPhoneNumber(phoneNumber);
		verifyCodeModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		verifyCodeModel.setDeleted(false);
		return verifyCodeDAO.insertOne(verifyCodeModel);
	}

	@Override
	public boolean deleteVerifyCode(Long id) {
		return verifyCodeDAO.deleteVerifyCode(id);
	}
	@Override
	public List<VerifyCodeModel> findByPhoneNumber(String phoneNumber) {
		return verifyCodeDAO.findByPhoneNumber(phoneNumber);
	}
	@Override
	public boolean deleteByPhoneNumber(String phoneNumber) {
		return verifyCodeDAO.deleteByPhoneNumber(phoneNumber);
	}
	
}
