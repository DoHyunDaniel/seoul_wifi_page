package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.repository.BookmarkRepo;

/**
 * 특정 북마크 항목을 삭제하는 서블릿입니다.
 * <p>
 * 요청 URL: /deleteBookmark  
 * 메서드: POST  
 * 파라미터: id (bookmarkId)
 */
@WebServlet("/deleteBookmark")
public class DeleteBookmarkServlet extends HttpServlet {

	private BookmarkRepo repository;

	/**
	 * 서블릿 초기화 시 BookmarkRepo를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		repository = new BookmarkRepo(context);
	}

	/**
	 * POST 요청으로 전달된 북마크 ID를 기반으로 해당 북마크를 삭제합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			repository.deleteBookmark(id);

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"WiFi 데이터를 가져오는 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}
