package com.socialmedia.api.post;

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
import com.socialmedia.model.FileModel;
import com.socialmedia.model.PostModel;
import com.socialmedia.response.BaseResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IFileService;
import com.socialmedia.service.IPostService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.FileService;
import com.socialmedia.service.impl.PostService;

@WebServlet("/api/edit_post")
public class EditPostAPI extends HttpServlet {

	/**
	 * 
	 */
	private IPostService postService;
	private IAccountService accountService;
	private GenericService genericService;
	private IFileService fileService;

	public EditPostAPI() {
		postService = new PostService();
		accountService = new AccountService();
		genericService = new BaseService();
		fileService = new FileService();
	}

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		List<Long> DelImages = new ArrayList<Long>();
		List<Long> SortImages = new ArrayList<Long>();
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		List<FileItem> containFileItems = new ArrayList<FileItem>();
		List<String> files = new ArrayList<String>();// luu ten file de save db
		// get path upload folder
		String root = uploadFolder();
		File file = new File(root);
		if (file.exists() == false) {
			file.mkdirs();
		}
		BaseResponse baseResponse = new BaseResponse();
		Long id = 0L;
		String described = "";
		int amoutImages = 0, amountVideo = 0;
		try {
			List<FileItem> list = servletFileUpload.parseRequest(request);
			for (FileItem fileItem : list) {
				if (fileItem.getFieldName().equalsIgnoreCase("id")) {
					id = Long.valueOf(fileItem.getString());
				} else if (fileItem.getFieldName().equalsIgnoreCase("image")) {
					if (fileItem.getName().endsWith(".jpg") || fileItem.getName().endsWith(".svg")
							|| fileItem.getName().endsWith(".JPEG") || fileItem.getName().endsWith(".png")) {
						containFileItems.add(fileItem);
						amoutImages++;
					} else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1003));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1003);
						response.getWriter().print(gson.toJson(baseResponse));
						return;
					}
				} else if (fileItem.getFieldName().equalsIgnoreCase("described")) {
					described = fileItem.getString("utf-8");

				} else if (fileItem.getFieldName().equalsIgnoreCase("image_del")) {
					DelImages = toArrayList(fileItem.getString());
					// System.out.println("Image_del : " + fileItem.getString());

				} else if (fileItem.getFieldName().equalsIgnoreCase("image_sort")) {
					SortImages = toArrayList(fileItem.getString());
					// System.out.println("Image_sort : " + fileItem.getString());

				} else if (fileItem.getFieldName().equalsIgnoreCase("video")) {
					if (fileItem.getName().endsWith(".mp4") || fileItem.getName().endsWith(".MP4")) {
						amountVideo++;
						containFileItems.add(fileItem);
					} else {
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1003));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1003);
						response.getWriter().print(gson.toJson(baseResponse));
						return;
					}
				}
			}
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jwt = (String) request.getAttribute("token");
		String phoneNumber = genericService.getPhoneNumberFromToken(jwt);
		AccountModel accountModel = accountService.findByPhoneNumber(phoneNumber);
		if (id == null || accountModel == null || amoutImages > 4 || amountVideo > 1
				|| amountVideo * amoutImages != 0) {
			parameterInValid(baseResponse);
			response.getWriter().print(gson.toJson(baseResponse));
			return;
		}
		PostModel postModel = postService.findPostById(id);
		if (postModel.getAccountId() != accountModel.getId()) {
			// not permit
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1009));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1009);
			response.getWriter().print(gson.toJson(baseResponse));
			return;
		} else {
			for (FileItem item : containFileItems) {
				String itemName = root + "\\" + item.getName();
				files.add(itemName);
			}
			FileModel fileModel = new FileModel();
			for (Long fileId : DelImages) {
				fileModel = fileService.findOne(fileId);
				if (fileModel != null && fileModel.getPostId() == id) {
					fileService.deleteById(fileId);
				} else {
					// del images invalid
					parameterInValid(baseResponse);
					response.getWriter().print(gson.toJson(baseResponse));
					return;
				}
			
			}
			postService.updateContenById(id, described);
			try {
				fileModel.setPostId(id);
			} catch (NullPointerException e) {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
				response.getWriter().print(gson.toJson(baseResponse));
				return;
			}
			fileModel.setCreatedBy(genericService.getPhoneNumberFromToken(jwt));
			for (String s : files) {
				fileModel.setContent(s);
				fileService.insertOne(fileModel);
			}
			writeFile(containFileItems);
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
			response.getWriter().print(gson.toJson(baseResponse));
		}
	}

	public void parameterInValid(BaseResponse baseResponse) {
		baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
		baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
	}

	public ArrayList<Long> toArrayList(String s) {
		ArrayList<Long> res = new ArrayList<Long>();
		StringBuffer temp = new StringBuffer("");
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
				temp.append(s.charAt(i));
			else if (temp.length() > 0) {
				res.add(Long.valueOf(temp.toString()));
				temp.delete(0, temp.length());
			}
		}
		return res;
	}

	public String uploadFolder() {
		ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getRealPath(File.separator);
		String root = contextPath + "\\images";
		System.out.println("root = " + root);
		return root;

	}

	public void writeFile(List<FileItem> fileItems) {
		for (FileItem item : fileItems) {
			if (item.getName() != null) {
				try {
					item.write(new File(uploadFolder() + "//" + item.getName()));
				} catch (Exception e) {
//					addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1010));
//					addPostResponse.setDataPostResponse(null);
//					addPostResponse.setMessage(BaseHTTP.MESSAGE_1010);
					System.out.println("Error = " + e.getMessage());
				}
			}
		}
	}
}
