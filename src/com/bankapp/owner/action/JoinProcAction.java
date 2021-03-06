package com.bankapp.owner.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bankapp.owner.dao.AccountDao;
import com.bankapp.owner.model.Account;
import com.bankapp.owner.util.Script;



public class JoinProcAction implements Action{
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 유효성 검사
		if
		(
				request.getParameter("name").equals("")||
				request.getParameter("name")==null||	
				request.getParameter("pwd").equals("")||
				request.getParameter("pwd")==null||		
				request.getParameter("joinPhone").equals("")||
				request.getParameter("joinPhone")==null	
				) {
			Script.back("누락된 항목이 있습니다.", response);
			return;
		}
		
		//파라메터 받기
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		String menegement = request.getParameter("menegement");
		String phone = request.getParameter("joinPhone");
		
		//오브젝트 변환
		Account account = Account.builder()
				.name(name)
				.pwd(pwd)
				.menegement(menegement)
				.phone(phone)
				.build();
		
		// db연결
		AccountDao accountDao = AccountDao.getinstance();
		int result = accountDao.join(account);
		
		//페이지 이동
		if (result==1) {
			Script.href("계좌개설이 성공했습니다.", "/owner/account?cmd=home", response);
		}else {
			Script.back("계좌개설이 실패했습니다.", response);
		}
	}
}
