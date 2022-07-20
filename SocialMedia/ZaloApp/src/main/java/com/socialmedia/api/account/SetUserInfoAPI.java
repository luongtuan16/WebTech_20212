package com.socialmedia.api.account;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.gson.Gson;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.model.AccountModel;
import com.socialmedia.request.account.SetUserInfoRequest;
import com.socialmedia.response.account.DataSetUserInfoResponse;
import com.socialmedia.response.account.SetUserInfoResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;

@WebServlet("/api/set_user_info")
public class SetUserInfoAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IAccountService accountService;
	private GenericService genericService;
	
	
	public SetUserInfoAPI() {
		// TODO Auto-generated constructor stub
		accountService = new AccountService();
		genericService = new BaseService();
	
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		SetUserInfoRequest setUserInfoRequest = new SetUserInfoRequest();
		SetUserInfoResponse setUserInfoResponse = new SetUserInfoResponse();
		DataSetUserInfoResponse data = new DataSetUserInfoResponse();
	
		
		String jwt = (String) request.getAttribute("token");
		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		if (accountModel == null || !accountModel.isActive()) {
			setUserInfoResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
			setUserInfoResponse.setMessage(BaseHTTP.MESSAGE_1004);
			response.getWriter().print(gson.toJson(setUserInfoResponse));
			return;
		}

		String root = uploadFolder();
		File file = new File(root);
		if (file.exists() == false) {
			file.mkdirs();
		}
		FileItem avatar = null, coverImage=null;
		
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> list = new ArrayList<FileItem>();

		try {
			list = servletFileUpload.parseRequest(request);
			System.out.println(list.size());
			for (FileItem fileItem : list) {
				if (fileItem.getFieldName().equals("username")) {
					String userName = fileItem.getString("utf-8");
					if (checkUserName(userName)) {
						setUserInfoRequest.setUsername(standardizeUserName(userName));
					} else {
						valueInValid(setUserInfoResponse);
						response.getWriter().print(gson.toJson(setUserInfoResponse));
						return;
					}
				} else if (fileItem.getFieldName().equals("description")) {
					String description = fileItem.getString("utf-8");
					if (description.length() <= 150)
						setUserInfoRequest.setDescription(description);
					else {
						valueInValid(setUserInfoResponse);
						response.getWriter().print(gson.toJson(setUserInfoResponse));
						return;
					}

				} else if (fileItem.getFieldName().equals("country")) {
					String country = fileItem.getString("utf-8");
					if (country.length() > 0)
						setUserInfoRequest.setCountry(country);
					else if (accountModel.getCountry()!=null)
						setUserInfoRequest.setCountry(accountModel.getCountry());
					else 
						setUserInfoRequest.setCountry("");
					
				} else if (fileItem.getFieldName().equals("address")) {
					String address = fileItem.getString("utf-8");
					if (address.length() > 0)
						setUserInfoRequest.setAddress(address);
					else if (accountModel.getAddress()!=null)
						setUserInfoRequest.setAddress(accountModel.getAddress());
					else setUserInfoRequest.setAddress("");
					
				} else if (fileItem.getFieldName().equals("city")) {
					String city = fileItem.getString("utf-8");
					if (city.length() > 0)
						setUserInfoRequest.setCity(city);
					else if (accountModel.getCity()!=null)
						setUserInfoRequest.setCity(accountModel.getCity());
					else setUserInfoRequest.setCity("");
					
				} else if (fileItem.getFieldName().equals("link")) {
				
				} else if (fileItem.getFieldName().equals("avatar")) {
					if (fileItem.getName().endsWith(".jpg") || fileItem.getName().endsWith(".svg")
							|| fileItem.getName().endsWith(".JPEG") 
							|| fileItem.getName().endsWith(".png")) {
						avatar = fileItem;
					}
					else {
						valueInValid(setUserInfoResponse);
						response.getWriter().print(gson.toJson(setUserInfoResponse));
						return;
					}
				} else if (fileItem.getFieldName().equals("cover_image")) {
					if (fileItem.getName().endsWith(".jpg") || fileItem.getName().endsWith(".svg")
							|| fileItem.getName().endsWith(".JPEG") 
							|| fileItem.getName().endsWith(".png")) {
						coverImage = fileItem;
					}
					else {
						valueInValid(setUserInfoResponse);
						response.getWriter().print(gson.toJson(setUserInfoResponse));
						return;
					}
				}

			}
		} catch (FileUploadException e) {
			setUserInfoResponse.setCode(String.valueOf(BaseHTTP.CODE_9994));
			setUserInfoResponse.setMessage(BaseHTTP.MESSAGE_9994);
			response.getWriter().print(gson.toJson(setUserInfoResponse));
			return;
		}
		
		if (avatar != null) {
			writeFile(avatar);
			accountModel.setAvatar(root + "\\" +avatar.getName());			
		}
		if(coverImage !=null) {
			writeFile(coverImage);
			accountModel.setCover_image(root + "\\" +coverImage.getName());
		}
		accountModel.setAddress(setUserInfoRequest.getAddress());
		accountModel.setCity(setUserInfoRequest.getCity());
		accountModel.setDescription(setUserInfoRequest.getDescription());
		accountModel.setCountry(setUserInfoRequest.getCountry());
		accountModel.setName(setUserInfoRequest.getUsername());
		if (accountService.updateAccount(accountModel)) {
			data.setAvatar(accountModel.getAvatar());
			data.setCity(accountModel.getCity());
			data.setCountry(accountModel.getCountry());
			data.setCover_image(accountModel.getCover_image());
			data.setLink(root);
			setUserInfoResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
			setUserInfoResponse.setMessage(BaseHTTP.MESSAGE_1000);
			setUserInfoResponse.setData(data);
		}else {
			setUserInfoResponse.setCode(String.valueOf(BaseHTTP.CODE_1005));
			setUserInfoResponse.setMessage(BaseHTTP.MESSAGE_1005);
		}
		response.getWriter().print(gson.toJson(setUserInfoResponse));
	}

	public String uploadFolder() {
		ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getRealPath(File.separator);
		String root = contextPath + "\\images";
		System.out.println("root = " + root);
		return root;

	}

	public void valueInValid(SetUserInfoResponse setUserInfoResponse) {
		setUserInfoResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
		setUserInfoResponse.setMessage(BaseHTTP.MESSAGE_1004);
		setUserInfoResponse.setData(null);
	}

	private boolean checkUserName(String userName) {
		if (userName.length() == 0 || userName.length() > 50)
			return false;
		return true;
	}

	public String standardizeUserName(String str) {
		str = str.trim();
		while (str.indexOf("  ") != -1)
			str = str.replaceAll("  ", " ");
		String temp[] = str.split(" ");
		str = ""; // ? ^-^
		for (int i = 0; i < temp.length; i++) {
			str += String.valueOf(temp[i].charAt(0)).toUpperCase() + temp[i].substring(1);
			if (i < temp.length - 1)
				str += " ";
		}
		return str;
	}
	
	public void writeFile(FileItem fileItem) {
			if (fileItem == null)
				return;
			if (fileItem.getName() != null) {
				try {
					fileItem.write(new File(uploadFolder() + "//" + fileItem.getName()));
				} catch (Exception e) {
					System.out.println("Error = " + e.getMessage());
				}
			
		}
	}
}
