package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.seoul_wifi_page.dto.WifiRow;
import com.seoul_wifi_page.service.IndexService;

@WebServlet("/detail")
public class GetDetailServlet extends HttpServlet {

	private IndexService wifiService;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		wifiService = new IndexService(context);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 파라미터로 전달된 managerNo를 가져오기
		String managerNo = request.getParameter("managerNo");
		if (managerNo == null || managerNo.isEmpty()) {
			// managerNo가 없을 경우 에러 응답
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Manager number is required.");
			return;
		}

		// 서비스에서 WiFi 상세 정보 가져오기
		WifiRow detail = wifiService.getWifiDetailByManagerNo(managerNo);

		// 응답 설정
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// JSON 변환 및 응답
		if (detail != null) {
			String jsonResponse = new Gson().toJson(detail); // DTO를 JSON으로 변환
			response.getWriter().write(jsonResponse);
		} else {
			// 해당 managerNo에 대한 데이터가 없는 경우
			response.getWriter().write("{\"error\": \"No details found for the provided manager number.\"}");
		}
	}
}
