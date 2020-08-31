package com.bankapp.owner.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bankapp.owner.dao.AccountDao;
import com.bankapp.owner.dto.SendLogDto;
import com.bankapp.owner.dto.SendmanagementDto;
import com.bankapp.owner.model.Account;
import com.bankapp.owner.model.Mempool;
import com.bankapp.owner.util.Script;
import com.google.gson.Gson;



public class SendLogProcManageAction implements Action{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		//파라메터 받기

		String management = request.getParameter("management");

		
		// db연결
		AccountDao accountDao = AccountDao.getinstance();

			List<SendmanagementDto> sendmanagementDtos = accountDao.sendLogmenegement(management);
			
			request.setAttribute("sendmanagementDtos", sendmanagementDtos);
			
			Gson gson = new Gson();
			String sendLogDtosJson = gson.toJson(sendmanagementDtos);

			
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.println(sendLogDtosJson);
			
		}
	}

