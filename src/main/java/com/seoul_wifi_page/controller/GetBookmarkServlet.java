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
import com.seoul_wifi_page.dto.Bookmark;
import com.seoul_wifi_page.service.BookmarkService;

@WebServlet("/bookmark-get-results")
public class GetBookmarkServlet extends HttpServlet {

	private BookmarkService bookmarkService;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkService = new BookmarkService(context);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int groupIdParam = Integer.parseInt(request.getParameter("groupId"));
			List<Bookmark> bookmarkList = bookmarkService.getAllBookmarkInfo(groupIdParam);

//			System.out.println(bookmarkList);
//	        int totalCount = searchHistoryService.getTotalCount();

			// JSON 응답 준비
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			// 데이터가 있으면 JSON 변환 후 응답
			if (bookmarkList != null && !bookmarkList.isEmpty()) {
				String jsonResponse = new Gson().toJson(new ResultsResponse(bookmarkList));
				response.getWriter().write(jsonResponse);
			} else {
				response.getWriter().write("{\"bookmarkList\": []}");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"Bookmark 데이터를 가져오는 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}

	// 응답 데이터 구조를 정의하는 내부 클래스
	private static class ResultsResponse {
		private List<Bookmark> bookmarkList;
//		private int totalCount;

		public ResultsResponse(List<Bookmark> bookmarkList) {
			this.bookmarkList = bookmarkList;
//			this.totalCount = totalCount;
		}
	}
}