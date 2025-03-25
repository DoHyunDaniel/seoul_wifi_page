package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.repository.SearchHistoryRepo;

/**
 * 사용자의 검색 기록을 삭제하는 서블릿입니다.
 * <p>
 * 요청 URL: /deleteSearchHistory  
 * 메서드: POST  
 * 파라미터: id (검색 기록 ID)
 */
@WebServlet("/deleteSearchHistory")
public class DeleteSearchHistoryServlet extends HttpServlet {

	private SearchHistoryRepo repository;

	/**
	 * 서블릿 초기화 시 SearchHistoryRepo 인스턴스를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		repository = new SearchHistoryRepo(context);
	}

	/**
	 * POST 요청을 처리하여 지정한 ID의 검색 기록을 삭제합니다.
	 * <p>
	 * 삭제 완료 후 검색 기록 목록 페이지로 리다이렉트됩니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			repository.deleteSearchHistory(id);

			// 삭제 후 검색 기록 목록 페이지로 이동
			response.sendRedirect("/be1_java_web_study01/searchHistory.jsp");

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"WiFi 데이터를 가져오는 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}
