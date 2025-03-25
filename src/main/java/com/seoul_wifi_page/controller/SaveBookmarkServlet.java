package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.service.BookmarkService;

/**
 * 사용자가 선택한 와이파이 정보를 북마크 그룹에 저장하는 서블릿입니다.
 * <p>
 * 요청 URL: /saveBookmark  
 * 메서드: POST  
 * 요청 파라미터: wifiId, groupId  
 * 응답: JSON 메시지 (성공 또는 실패)
 */
@WebServlet("/saveBookmark")
public class SaveBookmarkServlet extends HttpServlet {

	private BookmarkService bookmarkService;

	/**
	 * 서블릿 초기화 시 BookmarkService 인스턴스를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkService = new BookmarkService(context);
	}

	/**
	 * 사용자가 보낸 와이파이 ID와 북마크 그룹 ID를 기반으로
	 * 북마크 정보를 DB에 저장하고, JSON 응답을 반환합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
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
