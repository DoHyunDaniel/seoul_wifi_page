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
import com.seoul_wifi_page.dto.BookmarkGroup;
import com.seoul_wifi_page.service.BookmarkGroupService;

@WebServlet("/bookmark-group-get-results")
public class GetBookmarkGroupServlet extends HttpServlet {

	private BookmarkGroupService bookmarkGroupService;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkGroupService = new BookmarkGroupService(context);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<BookmarkGroup> bookmarkGroupList = bookmarkGroupService.getAllBookmarkGroupInfo();
//	        int totalCount = searchHistoryService.getTotalCount();

			// JSON 응답 준비
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			// 데이터가 있으면 JSON 변환 후 응답
			if (bookmarkGroupList != null && !bookmarkGroupList.isEmpty()) {
				String jsonResponse = new Gson().toJson(new ResultsResponse(bookmarkGroupList));
				response.getWriter().write(jsonResponse);
			} else {
				response.getWriter().write("{\"bookmarkGroupList\": []}");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"WiFi 데이터를 가져오는 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}

	// 응답 데이터 구조를 정의하는 내부 클래스
	private static class ResultsResponse {
		private List<BookmarkGroup> bookmarkGroupList;
//		private int totalCount;

		public ResultsResponse(List<BookmarkGroup> bookmarkGroupList) {
			this.bookmarkGroupList = bookmarkGroupList;
//			this.totalCount = totalCount;
		}
	}
}