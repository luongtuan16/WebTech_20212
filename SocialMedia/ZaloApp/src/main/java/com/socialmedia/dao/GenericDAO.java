package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.mapping.IRowMapping;

public interface GenericDAO<T> {
	List<T> findAll(String sql,IRowMapping<T> rowMapping);
	Long insertOne(String sql,Object... parameters );
	boolean update (String sql,Object... parameters);
	T findOne(String sql,IRowMapping<T> rowMapping,Object... parameters);
	boolean delete(String sql,Object... parameters);

}
