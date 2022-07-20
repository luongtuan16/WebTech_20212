package com.socialmedia.dao.impl;

import com.socialmedia.dao.ITypeReportDAO;
import com.socialmedia.mapping.TypeReportMapping;
import com.socialmedia.model.TypeReportModel;

public class TypeReportDAO extends BaseDAO<TypeReportModel> implements ITypeReportDAO {

	@Override
	public TypeReportModel findOne(Long typeReportId) {
		String sql = "SELECT * FROM typereport WHERE id = ?";
		try {
			return findOne(sql, new TypeReportMapping(), typeReportId);
		} catch (ClassCastException e) {
			return null;

		}

	}

}
