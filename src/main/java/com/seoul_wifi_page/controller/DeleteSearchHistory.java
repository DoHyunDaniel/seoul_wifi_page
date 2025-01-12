package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.repository.SQLiteHelper;

@WebServlet("/deleteSearchHistory")
public class DeleteSearchHistory extends HttpServlet {
	private SQLiteHelper dbHelper;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		dbHelper = new SQLiteHelper(context);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		dbHelper.deleteSearchHistory(id); // 삭제 메서드 호출

		// 리다이렉트로 목록 페이지로 이동
		response.sendRedirect("/be1_java_web_study01/searchHistory.jsp");
	}
}
