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

/**
 * 특정 북마크 그룹에 속한 북마크 목록을 조회하여 JSON 형식으로 반환하는 서블릿입니다.
 * <p>
 * 요청 URL: /bookmark-get-results  
 * 메서드: GET  
 * 요청 파라미터: groupId (북마크 그룹 ID)  
 * 응답: bookmarkList (JSON 배열)
 */
@WebServlet("/bookmark-get-results")
public class GetBookmarkServlet extends HttpServlet {

	private BookmarkService bookmarkService;

	/**
	 * 서블릿 초기화 시 BookmarkService를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkService = new BookmarkService(context);
	}

	/**
	 * GET 요청으로 북마크 그룹 ID를 받아 해당 그룹의 북마크 목록을 JSON으로 응답합니다.
	 * <p>
	 * 데이터가 없으면 빈 배열을, 예외 발생 시 오류 메시지를 반환합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int groupIdParam = Integer.parseInt(request.getParameter("groupId"));
			List<Bookmark> bookmarkList = bookmarkService.getAllBookmarkInfo(groupIdParam);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

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

	/**
	 * 북마크 목록을 JSON 구조로 감싸는 응답 객체입니다.
	 */
	private static class ResultsResponse {
		private List<Bookmark> bookmarkList;

		public ResultsResponse(List<Bookmark> bookmarkList) {
			this.bookmarkList = bookmarkList;
		}
	}
}
