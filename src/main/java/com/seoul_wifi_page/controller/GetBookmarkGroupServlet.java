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

/**
 * 북마크 그룹 목록을 JSON 형식으로 반환하는 서블릿입니다.
 * <p>
 * 요청 URL: /bookmark-group-get-results  
 * 메서드: GET  
 * 응답: 북마크 그룹 리스트(JSON)
 */
@WebServlet("/bookmark-group-get-results")
public class GetBookmarkGroupServlet extends HttpServlet {

	private BookmarkGroupService bookmarkGroupService;

	/**
	 * 서블릿 초기화 시 BookmarkGroupService를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkGroupService = new BookmarkGroupService(context);
	}

	/**
	 * 북마크 그룹 데이터를 조회하여 JSON 형식으로 반환합니다.
	 * <p>
	 * 북마크 그룹이 없을 경우 빈 배열 반환, 실패 시 에러 메시지 반환
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			List<BookmarkGroup> bookmarkGroupList = bookmarkGroupService.getAllBookmarkGroupInfo();

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

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

	/**
	 * 북마크 그룹 리스트를 JSON 구조로 감싸는 응답 객체입니다.
	 */
	private static class ResultsResponse {
		private List<BookmarkGroup> bookmarkGroupList;

		public ResultsResponse(List<BookmarkGroup> bookmarkGroupList) {
			this.bookmarkGroupList = bookmarkGroupList;
		}
	}
}
