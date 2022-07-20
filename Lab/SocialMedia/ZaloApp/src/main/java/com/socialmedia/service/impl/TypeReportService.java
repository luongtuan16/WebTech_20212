package com.socialmedia.service.impl;

import com.socialmedia.dao.ITypeReportDAO;
import com.socialmedia.dao.impl.TypeReportDAO;
import com.socialmedia.model.TypeReportModel;
import com.socialmedia.service.ITypeReportService;

public class TypeReportService implements ITypeReportService {
	private ITypeReportDAO typeReportDAO;
	public TypeReportService() {
		typeReportDAO = new TypeReportDAO();
	}
	@Override
	public TypeReportModel findOne(Long typeReportId) {
		return typeReportDAO.findOne(typeReportId);
	}

}
