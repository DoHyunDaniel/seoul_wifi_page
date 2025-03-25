package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.repository.BookmarkGroupRepo;

/**
 * 북마크 그룹을 삭제하는 기능을 담당하는 서블릿입니다.
 * <p>
 * 요청 URL: /deleteBookmarkGroup  
 * 메서드: POST  
 * 파라미터: groupId  
 * 처리 후: 북마크 그룹 목록 페이지로 리다이렉트
 */
@WebServlet("/deleteBookmarkGroup")
public class DeleteBookmarkGroupServlet extends HttpServlet {

	private BookmarkGroupRepo repository;

	/**
	 * 서블릿 초기화 시 DB Repository를 준비합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		repository = new BookmarkGroupRepo(context);
	}

	/**
	 * POST 요청을 처리하여 지정한 groupId의 북마크 그룹을 삭제합니다.
	 * 삭제 후에는 목록 페이지로 리다이렉트하며, 오류 발생 시 500 에러와 메시지를 반환합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("groupId"));
			repository.deleteBookmarkGroup(id);

			// 삭제 후 북마크 그룹 목록 페이지로 이동
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
