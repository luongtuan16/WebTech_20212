package com.socialmedia.service;

import com.socialmedia.model.BlocksModel;

public interface IBlockUserService {
	Long insertOne(Long idBlocks,Long idBlocked);
	BlocksModel findOne(Long idBlocks,Long idBlocked);
	boolean deleteBlock(Long id);
	boolean deleteUserBlocks(Long accountId);
}
