package com.seoul_wifi_page.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.service.BookmarkGroupService;
import com.seoul_wifi_page.service.SaveSearchHistoryService;

@WebServlet("/updateBookmarkGroup")
public class UpdateBookmarkGroupServlet extends HttpServlet {
	private BookmarkGroupService bookmarkGroupService;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkGroupService = new BookmarkGroupService(context);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");
			String bookmarkGroupName = request.getParameter("bookmarkGroupName");
			int bookmarkGroupOrder = Integer.parseInt(request.getParameter("bookmarkGroupOrder"));
			int bookmarkGroupId = Integer.parseInt(request.getParameter("groupId"));
			bookmarkGroupService.updateBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder, bookmarkGroupId);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"message\": \"Bookmark Group updated successfully.\"}");
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"저장 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}