package com.socialmedia.dao.impl;

import com.socialmedia.dao.IBlocksDAO;
import com.socialmedia.mapping.BlocksMapping;
import com.socialmedia.model.BlocksModel;

public class BlocksDAO extends BaseDAO<BlocksModel> implements IBlocksDAO {

	@Override
	public Long insertOne(BlocksModel blocksModel) {
		String sql = "INSERT INTO blocks(deleted,createddate,createdby,idBlocks,idBlocked) VALUES(?,?,?,?,?)";
		return insertOne(sql, blocksModel.isDeleted(),blocksModel.getCreatedDate(),blocksModel.getCreatedBy(),blocksModel.getIdBlocks(),blocksModel.getIdBlocked());
	}

	@Override
	public BlocksModel findOne(Long idBlocks, Long idBlocked) {
		String sql = "SELECT * FROM blocks WHERE idblocks = ? AND idblocked = ?";
		try {
			return findOne(sql, new BlocksMapping(), idBlocks,idBlocked);	
		} catch (ClassCastException e) {
			System.out.println("Failed Blocked DAO : "+e.getMessage());
			return null;
		}
	}

	@Override
	public boolean deleteBlock(Long id) {
		String sql = "DELETE FROM blocks WHERE id = ?";
		return delete(sql, id);
	}

	@Override
	public boolean deleteUserBlocks(Long accountId) {
		String sql = "DELETE FROM blocks WHERE idblocks = ? or idblocked = ?";
		return delete(sql, accountId, accountId);
	}
}
