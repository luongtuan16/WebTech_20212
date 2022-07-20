package com.socialmedia.dao;

import com.socialmedia.model.BlocksModel;

public interface IBlocksDAO {
	Long insertOne(BlocksModel blocksModel);
	BlocksModel findOne(Long idBlocks, Long idBlocked);
	boolean deleteBlock(Long id);
	boolean deleteUserBlocks(Long accountId);
}
