package com.socialmedia.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.IConversationDAO;
import com.socialmedia.mapping.ConversationMapping;
import com.socialmedia.model.ConversationModel;

public class ConversationDAO extends BaseDAO<ConversationModel> implements IConversationDAO {

	@Override
	public Long insertOne(ConversationModel conversationModel) {
		String sqlString = "insert into conversation (accountA, accountB) values (?, ?)";
		return insertOne(sqlString, conversationModel.getAccountA(), conversationModel.getAccountB());
	}

	@Override
	public boolean deleteConversation(Long conversationId) {
		String sqlString = "delete from conversation where id = ?";
		return delete(sqlString, conversationId);
	}

	@Override
	public ConversationModel findOne(Long conversationId) {
		String sqlString = "select * from conversation where id = ?";
		return findOne(sqlString, new ConversationMapping(), conversationId);
	}

	@Override
	public ConversationModel findOne(Long idA, Long idB) {
		String sqlString = "select * from conversation where accountA = ? and accountB = ?";
		return findOne(sqlString, new ConversationMapping(), idA, idB);
	}

	@Override
	public List<ConversationModel> findListConversation(Long accountId) {
		List<ConversationModel> models = new ArrayList<ConversationModel>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT * FROM conversation WHERE accountA = ? or accountB = ?";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, accountId);
			preparedStatement.setLong(2, accountId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ConversationModel model = new ConversationModel();
				model.setAccountA(resultSet.getLong("accountA"));
				model.setAccountB(resultSet.getLong("accountB"));
				model.setId(resultSet.getLong("id"));

				models.add(model);
			}
			return models;
		} catch (SQLException e) {
			System.out.println("findListConversation ConversationDAO 1 : " + e.getMessage());
			return null;
		} finally {
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (resultSet != null) {
					resultSet.close();
				}
			} catch (SQLException e2) {
				System.out.println("findListConversation ConversationDAO 2 : " + e2.getMessage());
				return null;
			}
		}
	}

}
