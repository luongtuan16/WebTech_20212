package com.socialmedia.dao.impl;

import com.socialmedia.dao.IBlockUserDAO;
import com.socialmedia.mapping.BlocksMapping;
import com.socialmedia.model.BlocksModel;

public class BlockUserDAO extends BaseDAO<BlocksModel> implements IBlockUserDAO {

	@Override
	public Long insertOne(BlocksModel blocksModel) {
		String sql = "INSERT INTO block_mess(deleted,createddate,createdby,idBlocks,idBlocked) VALUES(?,?,?,?,?)";
		return insertOne(sql, blocksModel.isDeleted(), blocksModel.getCreatedDate(), blocksModel.getCreatedBy(),
				blocksModel.getIdBlocks(), blocksModel.getIdBlocked());
	}

	@Override
	public BlocksModel findOne(Long idBlocks, Long idBlocked) {
		String sql = "SELECT * FROM block_mess WHERE idblocks = ? AND idblocked = ?";
		try {
			return findOne(sql, new BlocksMapping(), idBlocks, idBlocked);
		} catch (ClassCastException e) {
			System.out.println("Failed Block User DAO : " + e.getMessage());
			return null;
		}
	}

	@Override
	public boolean deleteBlock(Long id) {
		String sql = "DELETE FROM block_mess WHERE id = ?";
		return delete(sql, id);
	}

	@Override
	public boolean deleteUserBlocks(Long accountId) {
		String sql = "DELETE FROM block_mess WHERE idblocks = ? or idblocked = ?";
		return delete(sql, accountId, accountId);
	}

}
