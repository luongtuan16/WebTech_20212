package com.socialmedia.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.socialmedia.dao.IAccountDAO;
import com.socialmedia.mapping.AccountMapping;
import com.socialmedia.model.AccountModel;
import com.socialmedia.request.account.SignInRequest;

public class AccountDAO extends BaseDAO<AccountModel> implements IAccountDAO {

	@Override
	public boolean signUp(AccountModel accountModel) {
		String sql = "INSERT INTO account(deleted,createddate,createdby,name,password,phonenumber,avatar,uuid,rolekey,active) VALUES (?,?,?,?,?,?,?,?,?,?)";
		Long id = insertOne(sql, 0, accountModel.getCreatedDate(), accountModel.getCreatedBy(),
				accountModel.getName(), accountModel.getPassword(), accountModel.getPhoneNumber(),
				accountModel.getAvatar(), accountModel.getUuid(), 6, accountModel.isActive());
		boolean check = false;
		if (id > 0) {
			check = true;
		}
		return check;
	}

	@Override
	public AccountModel signIn(SignInRequest signInRequest) {
		String sql = "SELECT * FROM account WHERE phonenumber = ? AND password = ? AND deleted = false";
		try {
			return findOne(sql, new AccountMapping(), signInRequest.getPhonenumber(), signInRequest.getPassword());
		} catch (ClassCastException e) {
			return null;
		}

	}

	@Override
	public AccountModel findByPhoneNumber(String phoneNumber) {
		try {
			String sql = "SELECT * FROM account WHERE phonenumber = ? AND deleted = false";
			AccountModel accountModel = findOne(sql, new AccountMapping(), phoneNumber);
			if (accountModel != null) {
				return accountModel;
			}
			return null;
		} catch (ClassCastException e) {
			return null;
		}
	}

	@Override
	public AccountModel findById(Long id) {
		String sql = "SELECT * FROM account WHERE id = ?";
		try {
			AccountModel accountModel = findOne(sql, new AccountMapping(), id);
			if (accountModel != null) {
				return accountModel;
			}
		} catch (ClassCastException e) {
			return null;
		}
		return null;
	}

	@Override
	public List<AccountModel> findListAccountByKeyword(String keyword) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<AccountModel> list = new ArrayList<AccountModel>();
		try {
			String sql = "SELECT * FROM account WHERE deleted = false AND (name LIKE '%" + keyword
					+ "%' OR phonenumber LIKE '%" + keyword + "%')";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				AccountModel model = new AccountModel();
				model.setId(resultSet.getLong("id"));
				model.setDeleted(resultSet.getBoolean("deleted"));
				model.setCreatedDate(resultSet.getTimestamp("createddate"));
				model.setCreatedBy(resultSet.getString("createdby"));
				model.setModifiedDate(resultSet.getTimestamp("modifieddate"));
				model.setModifiedBy(resultSet.getString("modifiedby"));
				model.setName(resultSet.getString("name"));
				model.setPhoneNumber(resultSet.getString("phonenumber"));
				model.setPassword(resultSet.getString("password"));
				model.setAvatar(resultSet.getString("avatar"));
				model.setUuid(resultSet.getString("uuid"));
				list.add(model);
			}
			return list;
		} catch (SQLException e) {
			System.out.println("Failed findListAccountByKeyword AccountDAO 1 : " + e.getMessage());
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
				System.out.println("Failed findListAccountByKeyword AccountDAO 2 : " + e2.getMessage());
				return null;
			}
		}
	}

	@Override
	public boolean changePassword(AccountModel accountModel) {
		String sql = "UPDATE account SET createddate = ?,modifieddate = ?,modifiedby = ?,password = ? WHERE id = ?";
		return update(sql, accountModel.getCreatedDate(), accountModel.getModifiedDate(), accountModel.getModifiedBy(),
				accountModel.getPassword(), accountModel.getId());
	}

	@Override
	public boolean activeAccount(Long id) {
		String sql = "UPDATE account SET active = true WHERE id = ?";
		return update(sql, id);
	}

	@Override
	public boolean updateAccount(AccountModel account) {
		String sql = "UPDATE account SET modifieddate = ?, modifiedby = ?, name = ?, avatar = ?, description = ?,"
				+ "city = ?, address= ?, country = ?, cover_image = ? WHERE id = ?";
		return update(sql, account.getModifiedDate(), account.getModifiedBy(), account.getName(), account.getAvatar(),
				account.getDescription(), account.getCity(), account.getAddress(), account.getCountry(),
				account.getCover_image(), account.getId());
	}

	@Override
	public List<AccountModel> findAll() {
		String sql = "SELECT * FROM account";
		return findAll(sql, new AccountMapping());
	}

	@Override
	public boolean setRole(Long acccountId, Long roleId) {
		String sqlString = "UPDATE account SET rolekey = ? WHERE id = ?";
		return update(sqlString, roleId, acccountId);
	}

	@Override
	public boolean deactiveAccount(Long id) {
		String sql = "UPDATE account SET active = false WHERE id = ?";
		return update(sql, id);
	}

	@Override
	public boolean deleteAccount(Long accountId) {
		String sql = "DELETE FROM account WHERE id = ?";
		return delete(sql, accountId);
	}

	@Override
	public List<AccountModel> searchAccount(String keyword) {
		String temp[] = keyword.split(" ");
		List<AccountModel> accounts = new ArrayList<AccountModel>();
		String str = "";
		for (int i = 0; i < temp.length; i++) {
			str += "%" + temp[i] + "%";
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String sql = "SELECT * FROM account WHERE name LIKE ? OR phonenumber LIKE ?";
			connection = getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, str);
			preparedStatement.setString(2, str);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				AccountModel model = new AccountModel();
				model.setId(resultSet.getLong("id"));
				model.setDeleted(resultSet.getBoolean("deleted"));
				model.setCreatedDate(resultSet.getTimestamp("createddate"));
				model.setCreatedBy(resultSet.getString("createdby"));
				model.setModifiedDate(resultSet.getTimestamp("modifieddate"));
				model.setModifiedBy(resultSet.getString("modifiedby"));
				model.setName(resultSet.getString("name"));
				model.setPhoneNumber(resultSet.getString("phonenumber"));
				model.setPassword(resultSet.getString("password"));
				model.setAvatar(resultSet.getString("avatar"));
				model.setUuid(resultSet.getString("uuid"));

				accounts.add(model);
			}
			return accounts;
		} catch (SQLException e) {
			System.out.println("fail searchAccount AccountDAO 1 : " + e.getMessage());
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
				System.out.println("Fail searchAccount AccountDAO 2 : " + e2.getMessage());
				return null;
			}
		}
	}

}
