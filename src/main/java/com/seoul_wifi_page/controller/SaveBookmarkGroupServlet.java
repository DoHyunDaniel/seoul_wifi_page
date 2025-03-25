package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.service.BookmarkGroupService;

/**
 * 클라이언트로부터 전달받은 북마크 그룹 정보를 저장하는 서블릿입니다.
 * <p>
 * 요청 URL: /saveBookmarkGroup  
 * 메서드: POST  
 * 요청 파라미터: bookmarkGroupName, bookmarkGroupOrder  
 * 응답: JSON 메시지 (성공 또는 오류)
 */
@WebServlet("/saveBookmarkGroup")
public class SaveBookmarkGroupServlet extends HttpServlet {

	private BookmarkGroupService saveBookmarkGroupService;

	/**
	 * 서블릿 초기화 시 BookmarkGroupService를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		saveBookmarkGroupService = new BookmarkGroupService(context);
	}

	/**
	 * POST 요청으로 전달된 북마크 그룹명과 순서를 DB에 저장합니다.
	 * 성공 시 JSON 메시지를 응답하고, 실패 시 500 에러를 반환합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");

			String bookmarkGroupName = request.getParameter("bookmarkGroupName");
			int bookmarkGroupOrder = Integer.parseInt(request.getParameter("bookmarkGroupOrder"));

			saveBookmarkGroupService.saveBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"message\": \"Bookmark Group saved successfully.\"}");

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"저장 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}
