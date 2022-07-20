package com.socialmedia.service;

import java.util.List;

import com.socialmedia.model.FileModel;

public interface IFileService {
	FileModel insertOne(FileModel fileModel);
	FileModel findOne(Long id);
	List<FileModel> findByPostId(Long postId);
	boolean deleteByPostId(Long postId);
	boolean deleteById(Long id);
}
