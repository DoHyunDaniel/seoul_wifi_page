package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.seoul_wifi_page.service.WifiService;

@WebServlet("/fetch-api")
public class FetchApiServlet extends HttpServlet {

	private WifiService wifiService;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		wifiService = new WifiService(context);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			double userLat = Double.parseDouble(request.getParameter("latitude"));
			double userLon = Double.parseDouble(request.getParameter("longitude"));

			// OpenAPI 데이터를 가져와 DB에 저장
			wifiService.fetchAPI(userLat, userLon);

			// 최신 WiFi 데이터 총 개수 가져오기
			int totalCount = wifiService.getWifiTotalCount();

			// 응답 처리
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			// JSON 응답 데이터 생성
			JsonObject jsonResponse = new JsonObject();
			jsonResponse.addProperty("message", totalCount + "개의 WiFi 데이터가 성공적으로 저장되었습니다.");
			jsonResponse.addProperty("totalCount", totalCount);

			// JSON 응답 보내기
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