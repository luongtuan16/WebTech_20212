package com.socialmedia.dao;

import java.util.List;

import com.socialmedia.model.FileModel;

public interface IFileDAO extends GenericDAO<FileModel> {
	FileModel insertOne(FileModel fileModel);
	FileModel findOne(Long id);
	List<FileModel> findByPostId(Long postId);
	boolean deleteByPostId(Long postId);
	boolean deleteById(Long id);
}
