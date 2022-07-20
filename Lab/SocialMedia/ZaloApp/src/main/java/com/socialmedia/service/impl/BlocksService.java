package com.socialmedia.service.impl;

import java.sql.Timestamp;

import com.socialmedia.dao.IAccountDAO;
import com.socialmedia.dao.IBlocksDAO;
import com.socialmedia.dao.impl.AccountDAO;
import com.socialmedia.dao.impl.BlocksDAO;
import com.socialmedia.model.BlocksModel;
import com.socialmedia.service.IBlocksService;

public class BlocksService implements IBlocksService {
	private IBlocksDAO blocksDAO;
	private IAccountDAO accountDAO;
	public BlocksService() {
		blocksDAO = new  BlocksDAO();
		accountDAO = new AccountDAO();
	}
	@Override
	public Long insertOne(Long idBlocks,Long idBlocked) {
		BlocksModel blocksModel = new BlocksModel();
		String phoneNumber = accountDAO.findById(idBlocks).getPhoneNumber();
		blocksModel.setCreatedBy(phoneNumber);
		blocksModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		blocksModel.setDeleted(false);
		blocksModel.setIdBlocked(idBlocked);
		blocksModel.setIdBlocks(idBlocks);
		return blocksDAO.insertOne(blocksModel);
	}
	@Override
	public BlocksModel findOne(Long idBlocks, Long idBlocked) {
		return blocksDAO.findOne(idBlocks,idBlocked);
	}
	@Override
	public boolean deleteBlock(Long id) {
		return blocksDAO.deleteBlock(id);
	}
	@Override
	public boolean deleteUserBlocks(Long accountId) {
		return blocksDAO.deleteUserBlocks(accountId);
	}

}
