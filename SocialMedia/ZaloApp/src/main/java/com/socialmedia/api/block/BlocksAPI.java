package com.socialmedia.api.block;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.socialmedia.api.BaseHTTP;
import com.socialmedia.model.AccountModel;
import com.socialmedia.model.BlocksModel;
import com.socialmedia.request.blocks.AddBlocksRequest;
import com.socialmedia.response.BaseResponse;
import com.socialmedia.service.GenericService;
import com.socialmedia.service.IAccountService;
import com.socialmedia.service.IBlocksService;
import com.socialmedia.service.impl.AccountService;
import com.socialmedia.service.impl.BaseService;
import com.socialmedia.service.impl.BlocksService;

@WebServlet("/api/blocks")
public class BlocksAPI extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IBlocksService blocksService;
	private IAccountService accountService;
	private GenericService genericService;

	public BlocksAPI() {
		blocksService = new BlocksService();
		accountService = new AccountService();
		genericService = new BaseService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		String jwt = request.getHeader(BaseHTTP.Authorization);
		BaseResponse baseResponse = new BaseResponse();
		String idBlockedQuery = request.getParameter("idBlocked");
		String phoneNumber = genericService.getPhoneNumberFromToken(jwt);
		AccountModel accountModel = accountService.findByPhoneNumber(phoneNumber);
		Long idBlocks = accountModel.getId();
		try {
//			AddBlocksRequest addBlocksRequest = gson.fromJson(request.getReader(), AddBlocksRequest.class);
			
				
				if (idBlockedQuery != null) {
					@SuppressWarnings("unused")
					AddBlocksRequest addBlocksRequest = new AddBlocksRequest();
					addBlocksRequest.setIdBlocked(Long.valueOf(idBlockedQuery));
					addBlocksRequest.setIdBlocks(Long.valueOf(idBlocks));
					
						Long idBlocked = addBlocksRequest.getIdBlocked();
					Long id = 0L;
					if (idBlocks == idBlocked) {
						id = -1L;
						System.out.println("1");
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
						baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
					} else if (accountService.findById(idBlocked) == null) {
						id = -1L;
						System.out.println("2");
						baseResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
						baseResponse.setMessage(BaseHTTP.MESSAGE_9995);

					} else {
						// Check Block
						BlocksModel blocksModel = blocksService.findOne(idBlocks, addBlocksRequest.getIdBlocked());
						if (blocksModel == null) {
							id = blocksService.insertOne(idBlocks, addBlocksRequest.getIdBlocked());
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
							baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
						} else {
							baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1010));
							baseResponse.setMessage(BaseHTTP.MESSAGE_1010);
						}

					}
				} else {
					// {
					// ....
					// }
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
				}
				response.getWriter().print(gson.toJson(baseResponse));
			
		} catch (NumberFormatException | JsonSyntaxException e) {
			// sai kieu
			System.out.println(e.getMessage());
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1003));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1003);
			response.getWriter().print(gson.toJson(baseResponse));
		}
	}
}
