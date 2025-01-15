package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.repository.BookmarkGroupRepo;

@WebServlet("/deleteBookmarkGroup")
public class DeleteBookmarkGroupServlet extends HttpServlet {
	private BookmarkGroupRepo repository;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		repository = new BookmarkGroupRepo(context);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("groupId"));
			repository.deleteBookmarkGroup(id); // 삭제 메서드 호출

			// 리다이렉트로 목록 페이지로 이동
			response.sendRedirect("/be1_java_web_study01/bookmark-group.jsp");
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"WiFi 데이터를 가져오는 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}
