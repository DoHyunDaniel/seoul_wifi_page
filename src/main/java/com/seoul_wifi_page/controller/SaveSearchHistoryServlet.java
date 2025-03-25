package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.service.SaveSearchHistoryService;

/**
 * 사용자의 검색 위치 및 시간을 기반으로 검색 기록을 저장하는 서블릿입니다.
 * <p>
 * 요청 URL: /saveSearchHistory  
 * 메서드: POST  
 * 요청 파라미터: latitude (위도), longitude (경도), searchDate (검색 시각)  
 * 응답: JSON 메시지 (성공 또는 실패)
 */
@WebServlet("/saveSearchHistory")
public class SaveSearchHistoryServlet extends HttpServlet {

	private SaveSearchHistoryService searchHistoryService;

	/**
	 * 서블릿 초기화 시 SaveSearchHistoryService를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		searchHistoryService = new SaveSearchHistoryService(context);
	}

	/**
	 * POST 요청으로 전달된 위도, 경도, 검색 날짜를 기반으로
	 * 검색 기록을 DB에 저장하고 JSON 응답을 반환합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			double userLat = Double.parseDouble(request.getParameter("latitude"));
			double userLon = Double.parseDouble(request.getParameter("longitude"));
			String searchDate = request.getParameter("searchDate");

			searchHistoryService.saveSearchHistory(userLat, userLon, searchDate);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"message\": \"Search history saved successfully.\"}");

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"WiFi 데이터를 가져오는 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}
