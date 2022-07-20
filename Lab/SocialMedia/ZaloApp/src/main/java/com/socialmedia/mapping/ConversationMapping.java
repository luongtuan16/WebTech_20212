package com.socialmedia.mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.socialmedia.model.ConversationModel;

public class ConversationMapping implements IRowMapping<ConversationModel> {

	@Override
	public ConversationModel mapRow(ResultSet resultSet) {
		try {
			ConversationModel model = new ConversationModel();
			model.setId(resultSet.getLong("id"));
			model.setAccountA(resultSet.getLong("accountA"));
			model.setAccountB(resultSet.getLong("accountB"));
			return model;
		} catch (SQLException e) {
			System.out.println("Failed Row Mapping Conversation Mapping : " + e.getMessage());
			return null;
		}
	}

}
