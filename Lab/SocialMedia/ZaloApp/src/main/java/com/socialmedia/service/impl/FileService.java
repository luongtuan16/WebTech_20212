package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.socialmedia.dao.IFileDAO;
import com.socialmedia.dao.impl.FileDAO;
import com.socialmedia.model.FileModel;
import com.socialmedia.service.IFileService;

public class FileService implements IFileService {
	private IFileDAO fileDAO;
	public FileService() {
		fileDAO = new FileDAO();
	}

	@Override
	public FileModel insertOne(FileModel fileModel) {
		fileModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		fileModel.setDeleted(false);
		return fileDAO.insertOne(fileModel);
	}

	@Override
	public FileModel findOne(Long id) {
		return fileDAO.findOne(id);
	}

	@Override
	public List<FileModel> findByPostId(Long postId) {
		return fileDAO.findByPostId(postId);
	}

	@Override
	public boolean deleteByPostId(Long postId) {
		return fileDAO.deleteByPostId(postId);
	}

	@Override
	public boolean deleteById(Long id) {
		return fileDAO.deleteById(id);
	}
}
