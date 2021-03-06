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
import com.socialmedia.model.FileModel;
import com.socialmedia.request.post.AddPostRequest;
import com.socialmedia.response.post.AddPostResponse;
import com.socialmedia.response.post.DataAddPostResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IFileService;
import com.socialmedia.service.IPostService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.FileService;
import com.socialmedia.service.impl.PostService;

@WebServlet("/api/add_post")
//@MultipartConfig
public class AddPostAPI extends HttpServlet {

	private IPostService postService;
	private IFileService fileService;
	private GenericService genericService;

	public AddPostAPI() {
		postService = new PostService();
		fileService = new FileService();
		genericService = new BaseService();
	}

	public static final int MAX_REQUEST_FILE = 1024 * 1024 * 50; // 50MB
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		AddPostResponse addPostResponse = new AddPostResponse();
		//String token = request.getHeader(BaseHTTP.Authorization);
		String token = (String) request.getAttribute("token");
		String describedRequest = null;
		List<String> files = new ArrayList<String>();// luu ten file de save db
		Gson gson = new Gson();
		// get path upload folder
		String root = uploadFolder();
		File file = new File(root);
		if (file.exists() == false) {
			file.mkdirs();
		}
		List<FileItem> containFileItems = new ArrayList<FileItem>(); // chua cac file != null,de ghi ra folder
		ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
		boolean video = false, image = false;// xac dinh file dang nao.
		int countImage = 0, countVideo = 0; // get so luong image,video
		int filesSize = 0; // tong size cua cac file upload
		List<FileItem> list = new ArrayList<FileItem>();

		try {

			list = servletFileUpload.parseRequest(request);
			System.out.println(list.size());
			for (FileItem fileItem : list) {
				System.out.println("FileITem == " + fileItem.getFieldName());
				if (fileItem.getFieldName().equalsIgnoreCase("video")) {
					if (fileItem.getName().endsWith(".mp4")||fileItem.getName().endsWith(".MP4")) {
						if (countVideo < 1) {
							if (image == true) {
								parameterInValid(addPostResponse);
								response.getWriter().print(gson.toJson(addPostResponse));
								return;
							} else {
								countVideo++;
								video = true;
								filesSize += fileItem.getSize();
							}
							containFileItems.add(fileItem);
						} else {
							uploadFileFailed(addPostResponse);
							response.getWriter().print(gson.toJson(addPostResponse));
							return;
						}

					} else {
						parameterInValid(addPostResponse);
						response.getWriter().print(gson.toJson(addPostResponse));
						return;
					}
				} else if (fileItem.getFieldName().equalsIgnoreCase("image")) {
					if (fileItem.getName().endsWith(".jpg") || fileItem.getName().endsWith(".svg")
							|| fileItem.getName().endsWith(".JPEG") 
							|| fileItem.getName().endsWith(".png")) {
						if (countImage < 4) {
							if (video == true) {
								parameterInValid(addPostResponse);
								response.getWriter().print(gson.toJson(addPostResponse));
								return;
							} else {
								countImage++;
								image = true;
								filesSize += fileItem.getSize();
							}
							containFileItems.add(fileItem);
						} else {
							maximumNumberOfImages(addPostResponse);
							response.getWriter().print(gson.toJson(addPostResponse));
							return;
						}
					} else {
						parameterInValid(addPostResponse);
						response.getWriter().print(gson.toJson(addPostResponse));
						return;
					}
				} else if (fileItem.getFieldName().equalsIgnoreCase("described")) {
					String described = fileItem.getString("utf-8");
					if (described.length() > 0) {
						describedRequest = described;
					} else {
						// decribed vuot qua so tu cho phep
						parameterInValid(addPostResponse);
						response.getWriter().print(gson.toJson(addPostResponse));
						return;
					}
				}

			}
		} catch (FileUploadException e) {
			noData(addPostResponse);
			response.getWriter().print(gson.toJson(addPostResponse));
			return;
		}
		// check max size
		if (filesSize > MAX_REQUEST_FILE) {
			containFileItems.clear();
			response.getWriter().print(gson.toJson(addPostResponse));
			return;
		} else {
			// <= MAX = > write File
			for (FileItem item : containFileItems) {
				String itemName = root + "\\" +item.getName();
				files.add(itemName);
			}
		}
//		 add post
		try {
			if (describedRequest.length() > 0) {
				AddPostRequest addPostRequest = new AddPostRequest();
				addPostRequest.setDescribed(describedRequest);
				addPostRequest.setToken(token);
				addPostRequest.setFiles(files);
				Long id = postService.insertOne(addPostRequest);
				FileModel fileModel = new FileModel();
				try {
					fileModel.setPostId(id);
				} catch (NullPointerException e) {
					parameterNotEnough(addPostResponse);
					response.getWriter().print(gson.toJson(addPostResponse));
					return;
				}
				fileModel.setCreatedBy(genericService.getPhoneNumberFromToken(token));
				for (String s : files) {
					fileModel.setContent(s);
					fileService.insertOne(fileModel);
				}
				DataAddPostResponse dataPostResponse = new DataAddPostResponse();
				dataPostResponse.setId(id);
				dataPostResponse.setUrl("/CZone/api/get_post?id=" + id);
				addPostResponse.setCode(String.valueOf(1000));
				addPostResponse.setDataPostResponse(dataPostResponse);
				addPostResponse.setMessage("OK");
				writeFile(containFileItems);
				response.getWriter().print(gson.toJson(addPostResponse));
			}
		} catch (NullPointerException e) {
			parameterNotEnough(addPostResponse);
			response.getWriter().print(gson.toJson(addPostResponse));
		}
	}

	public String uploadFolder() {
		ServletContext servletContext = getServletContext();
		String contextPath = servletContext.getRealPath(File.separator);
		String root = contextPath + "\\images";
		System.out.println("root = " + root);
		return root;

	}

	public void parameterInValid(AddPostResponse addPostResponse) {
		addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
		addPostResponse.setDataPostResponse(null);
		addPostResponse.setMessage(BaseHTTP.MESSAGE_1004);
	}

	public void parameterTypeInValid(AddPostResponse addPostResponse) {
		addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1003));
		addPostResponse.setDataPostResponse(null);
		addPostResponse.setMessage(BaseHTTP.MESSAGE_1003);
	}

	public void parameterNotEnough(AddPostResponse addPostResponse) {
		addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
		addPostResponse.setDataPostResponse(null);
		addPostResponse.setMessage(BaseHTTP.MESSAGE_1002);
	}

	public void uploadFileFailed(AddPostResponse addPostResponse) {
		addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1007));
		addPostResponse.setDataPostResponse(null);
		addPostResponse.setMessage(BaseHTTP.MESSAGE_1007);
	}

	public void maximumNumberOfImages(AddPostResponse addPostResponse) {
		addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1008));
		addPostResponse.setDataPostResponse(null);
		addPostResponse.setMessage(BaseHTTP.MESSAGE_1008);

	}

	public void noData(AddPostResponse addPostResponse) {
		addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_9994));
		addPostResponse.setDataPostResponse(null);
		addPostResponse.setMessage(BaseHTTP.MESSAGE_9994);
	}

	public void fileSizeIsTooBig(AddPostResponse addPostResponse) {
		addPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1006));
		addPostResponse.setDataPostResponse(null);
		addPostResponse.setMessage(BaseHTTP.MESSAGE_1006);
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