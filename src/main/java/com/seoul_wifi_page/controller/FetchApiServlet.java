package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.seoul_wifi_page.service.IndexService;

/**
 * 사용자의 현재 위치를 기반으로 서울시 공공 WiFi OpenAPI 데이터를 호출하고,
 * 해당 데이터를 DB에 저장하는 기능을 수행하는 서블릿입니다.
 * <p>
 * 요청 URL: /fetch-api  
 * 메서드: GET  
 * 요청 파라미터: latitude (위도), longitude (경도)  
 * 응답 형식: JSON
 */
@WebServlet("/fetch-api")
public class FetchApiServlet extends HttpServlet {

	private IndexService wifiService;

	/**
	 * 서블릿 초기화 시 IndexService를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		wifiService = new IndexService(context);
	}

	/**
	 * GET 요청을 처리하여 OpenAPI로부터 WiFi 데이터를 수집하고 DB에 저장한 후,
	 * 성공 메시지 및 전체 데이터 수를 JSON 형태로 반환합니다.
	 *
	 * @param request  HttpServletRequest (latitude, longitude 필요)
	 * @param response HttpServletResponse (JSON 반환)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 사용자 위치 파라미터
			double userLat = Double.parseDouble(request.getParameter("latitude"));
			double userLon = Double.parseDouble(request.getParameter("longitude"));

			// OpenAPI 호출 및 DB 저장
			wifiService.fetchAPI(userLat, userLon);

			// 총 데이터 수 조회
			int totalCount = wifiService.getWifiTotalCount();

			// JSON 응답 준비
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			JsonObject jsonResponse = new JsonObject();
			jsonResponse.addProperty("message", totalCount + "개의 WiFi 데이터가 성공적으로 저장되었습니다.");
			jsonResponse.addProperty("totalCount", totalCount);

			// 응답 전송
			response.getWriter().write(jsonResponse.toString());

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"WiFi 데이터를 가져오는 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}
