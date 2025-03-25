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
import com.seoul_wifi_page.dto.WifiRow;
import com.seoul_wifi_page.service.IndexService;

/**
 * 저장된 와이파이 정보 중 거리 기준 상위 20개를 조회하여 JSON으로 응답하는 서블릿입니다.
 * <p>
 * 요청 URL: /get-results  
 * 메서드: GET  
 * 응답: wifiData (WiFi 목록), totalCount (총 저장된 수)
 */
@WebServlet("/get-results")
public class GetResultsServlet extends HttpServlet {

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
	 * GET 요청 시 DB에서 상위 20개의 와이파이 데이터를 조회하고, 총 개수와 함께 JSON으로 응답합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// 거리 기준 상위 20개 WiFi 정보 조회
			List<WifiRow> wifiData = wifiService.getTopWifiInfo(20);
			int totalCount = wifiService.getWifiTotalCount();

			// JSON 응답 설정
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			if (wifiData != null && !wifiData.isEmpty()) {
				String jsonResponse = new Gson().toJson(new ResultsResponse(wifiData, totalCount));
				response.getWriter().write(jsonResponse);
			} else {
				response.getWriter().write("{\"wifiData\": [], \"totalCount\": 0}");
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
	 * 와이파이 목록과 총 개수를 포함하는 응답 JSON 구조를 정의하는 내부 클래스입니다.
	 */
	private static class ResultsResponse {
		private List<WifiRow> wifiData;
		private int totalCount;

		public ResultsResponse(List<WifiRow> wifiData, int totalCount) {
			this.wifiData = wifiData;
			this.totalCount = totalCount;
		}
	}
}
