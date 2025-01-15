package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.service.BookmarkService;

@WebServlet("/saveBookmark")
public class SaveBookmarkServlet extends HttpServlet {
	private BookmarkService bookmarkService;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkService = new BookmarkService(context);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			String wifiId = request.getParameter("wifiId");
			int bookmarkGroupId = Integer.parseInt(request.getParameter("groupId"));

			bookmarkService.saveBookmark(wifiId, bookmarkGroupId);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"message\": \"Bookmark saved successfully.\"}");
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"저장 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}