package com.socialmedia.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.IMessageDAO;
import com.socialmedia.mapping.MessageMapping;
import com.socialmedia.model.MessageModel;

public class MessageDAO extends BaseDAO<MessageModel> implements IMessageDAO {

	@Override
	public Long insertOne(MessageModel messageModel) {
		String sqlString = "INSERT INTO message (createddate, content, conversationid, atob, unread) "
				+ "VALUES(?, ?, ?, ?, ?)";
		return insertOne(sqlString, messageModel.getCreatedDate(), messageModel.getContent(),
				messageModel.getConversationId(), messageModel.isaToB(), messageModel.isUnread());
	}

	@Override
	public boolean deleteMessage(Long messageId) {
		String sqlString = "delete from message where id = ?";
		return delete(sqlString, messageId);
	}

	@Override
	public List<MessageModel> findConversation(Long conversationId) {
		List<MessageModel> models = new ArrayList<MessageModel>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT * FROM message WHERE conversationid = ? ORDER BY createddate DESC";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, conversationId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				MessageModel model = new MessageModel();

				model.setConversationId(conversationId);
				model.setCreatedDate(resultSet.getTimestamp("createddate"));
				model.setaToB(resultSet.getBoolean("atob"));
				model.setContent(resultSet.getString("content"));
				model.setId(resultSet.getLong("id"));
				model.setUnread(resultSet.getBoolean("unread"));

				models.add(model);
			}
			return models;
		} catch (SQLException e) {
			System.out.println("findConversation MessageDAO 1 : " + e.getMessage());
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
				System.out.println("findConversation MessageDAO 2 : " + e2.getMessage());
				return null;
			}
		}
	}

	@Override
	public boolean deleteConversation(Long conversationId) {
		String sqlString = "delete from message where conversationid = ?";
		return delete(sqlString, conversationId);
	}

	@Override
	public MessageModel findOne(Long messId) {
		String sqlString = "SELECT * FROM message WHERE id =?";
		return findOne(sqlString, new MessageMapping(), messId);
	}

	@Override
	public List<MessageModel> searchMessage(String keyword) {
		String temp[] = keyword.split(" ");
		List<MessageModel> messages = new ArrayList<MessageModel>();
		String str = "";
		for (int i = 0; i < temp.length; i++) {
			str += "%" + temp[i] + "%";
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT * FROM message WHERE content LIKE ?";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, str);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				MessageModel model = new MessageModel();

				model.setConversationId(resultSet.getLong("conversationid"));
				model.setCreatedDate(resultSet.getTimestamp("createddate"));
				model.setaToB(resultSet.getBoolean("atob"));
				model.setContent(resultSet.getString("content"));
				model.setId(resultSet.getLong("id"));
				model.setUnread(resultSet.getBoolean("unread"));

				messages.add(model);
			}
			return messages;
		} catch (SQLException e) {
			System.out.println("fail searchMessage MessageDAO 1 : " + e.getMessage());
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
				System.out.println("Fail searchMessage MessageDAO 2 : " + e2.getMessage());
				return null;
			}
		}
	}
}
