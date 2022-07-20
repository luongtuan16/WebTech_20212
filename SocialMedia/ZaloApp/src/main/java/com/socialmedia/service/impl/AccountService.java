package com.socialmedia.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.socialmedia.dao.IAccountDAO;
import com.socialmedia.dao.IBlocksDAO;
import com.socialmedia.dao.IFriendDAO;
import com.socialmedia.dao.IRoleDAO;
import com.socialmedia.dao.impl.AccountDAO;
import com.socialmedia.dao.impl.BlocksDAO;
import com.socialmedia.dao.impl.FriendDAO;
import com.socialmedia.dao.impl.RoleDAO;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.BlocksModel;
import com.socialmedia.model.FriendModel;
import com.socialmedia.model.RoleModel;
import com.socialmedia.request.account.SignInRequest;
import com.socialmedia.request.account.SignUpRequest;
import com.socialmedia.service.IAccountService;

public class AccountService extends BaseService implements IAccountService {

	private IAccountDAO accountDAO;
	private IBlocksDAO blocksDAO;
	private IFriendDAO friendDAO;
	private IRoleDAO roleDAO;
	public AccountService() {
		// TODO Auto-generated constructor stub
		accountDAO = new AccountDAO();
		blocksDAO = new BlocksDAO();
		friendDAO = new FriendDAO();
		roleDAO = new RoleDAO();
	}

	@SuppressWarnings("null")
	@Override
	public AccountModel signUp(SignUpRequest signUpRequest) {
		String phoneNumber = signUpRequest.getPhonenumber();
		AccountModel model = accountDAO.findByPhoneNumber(phoneNumber);
		if (model != null) {
			return null;
		} else {
			AccountModel accountModel = new AccountModel();
			accountModel.setPhoneNumber(signUpRequest.getPhonenumber());
			accountModel.setName(signUpRequest.getPhonenumber());
			accountModel.setDeleted(false);
			accountModel.setAvatar(" ");
			accountModel.setActive(true);
			accountModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			accountModel.setCreatedBy(signUpRequest.getPhonenumber());
			accountModel.setCreatedDateLong(convertTimestampToSeconds(accountModel.getCreatedDate()));
//			accountModel.setPassword(getMD5(signUpRequest.getPassword()));
			accountModel.setPassword(signUpRequest.getPassword());
			accountModel.setUuid(signUpRequest.getUuid());
			boolean b = accountDAO.signUp(accountModel);
			if (b) {
				return accountModel;
			}
			return null;
		}

	}

	@Override
	public String signIn(SignInRequest signInRequest) {
		String jwt = createJWT(signInRequest.getPhonenumber());
//		signInRequest.setPassword(getMD5(signInRequest.getPassword()));
		if (accountDAO.signIn(signInRequest) != null) {
			return jwt;
		}
		return null;
	}

	@Override
	public AccountModel findByPhoneNumber(String phoneNumber) {
		return accountDAO.findByPhoneNumber(phoneNumber);
	}

	@Override
	public AccountModel findById(Long id) {
		return accountDAO.findById(id);
	}

	@Override
	public List<AccountModel> findListAccountByKeyword(String keyword, String token) {
		List<AccountModel> list = accountDAO.findListAccountByKeyword(keyword);
		// get accountId From token
		Long accountId = findByPhoneNumber(getPhoneNumberFromToken(token)).getId();
		for (AccountModel accountModel : list) {
			// Check block
			BlocksModel b1 = blocksDAO.findOne(accountId, accountModel.getId());
			BlocksModel b2 = blocksDAO.findOne(accountModel.getId(), accountId);
			if (b1 != null || b2 != null) {
				list.remove(accountModel);
			}
		}
		return list;
	}

	@Override
	public boolean changePassword(String token, String password, String newPassword) {
		String phoneNumber = getPhoneNumberFromToken(token);
		AccountModel accountModel = findByPhoneNumber(phoneNumber);
		String pw = accountModel.getPassword();
		if (pw.equals(password)) {
			accountModel.setModifiedDate(new Timestamp(System.currentTimeMillis()));
			accountModel.setPassword(newPassword);
			accountModel.setModifiedBy(phoneNumber);
			return accountDAO.changePassword(accountModel);
		} else {
			return false;
		}

	}

	@Override
	public boolean activeAccount(Long id) {
		return accountDAO.activeAccount(id);
	}

	@Override
	public boolean updateAccount(AccountModel account) {
		account.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		account.setModifiedBy(account.getPhoneNumber());
		
		AccountModel oldAcc = findById(account.getId());
		if (account.getName()==null || account.getName().length() == 0)
			account.setName(oldAcc.getName());
		if (account.getAddress()==null || account.getAddress().length() == 0)
			account.setAddress("");
		if (account.getCity()==null || account.getCity().length() == 0)
			account.setCity("");
		if (account.getAvatar()==null || account.getAvatar().length() == 0)
			account.setAvatar(oldAcc.getAvatar());
		if (account.getCover_image()==null || account.getCover_image().length() == 0)
			account.setCover_image(oldAcc.getCover_image());
		if (account.getCountry() == null || account.getCountry().length() == 0)
			account.setCountry("");
		if (account.getDescription() == null || account.getDescription().length() == 0)
			account.setDescription("");
		
		return accountDAO.updateAccount(account);
	}

	@Override
	public List<AccountModel> listSuggestedAccounts(Long id) {
		List<AccountModel> listacc = accountDAO.findAll();
		int i=0;
		while (i<listacc.size()) {
			if (listacc.get(i).getId() == id)
				listacc.remove(i);
			else {
				FriendModel friendModel1 = friendDAO.findOne(id, listacc.get(i).getId(), true);
				FriendModel friendModel2 =friendDAO.findOne(listacc.get(i).getId(),id, true);
				if (friendModel1 !=null || friendModel2 !=null)
					listacc.remove(i);
				else i++;
			}
		}
		return listacc;
	}

	@Override
	public boolean setRole(Long accountId, String role) {
		RoleModel roleModel = roleDAO.findByRoleString(role);
		if (roleModel!=null)
			return accountDAO.setRole(accountId, roleModel.getId());
		return false;
	}

	@Override
	public boolean setRole(Long accountId, Long roleId) {
		return accountDAO.setRole(accountId, roleId);
	}

	@Override
	public boolean deactiveAccount(Long id) {
		return accountDAO.deactiveAccount(id);
	}

	@Override
	public List<AccountModel> findAll() {
		return accountDAO.findAll();
	}

	@Override
	public boolean deleteAccount(Long accountId) {
		return accountDAO.deleteAccount(accountId);
	}

	@Override
	public List<AccountModel> searchAccount(String keyword) {
		return accountDAO.searchAccount(keyword);
	}
}
