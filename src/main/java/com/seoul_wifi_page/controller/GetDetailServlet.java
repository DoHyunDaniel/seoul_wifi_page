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

/**
 * managerNo를 기반으로 특정 와이파이의 상세 정보를 조회하여 JSON으로 응답하는 서블릿입니다.
 * <p>
 * 요청 URL: /detail  
 * 메서드: GET  
 * 요청 파라미터: managerNo (와이파이 고유 ID)  
 * 응답: WifiRow 객체(JSON)
 */
@WebServlet("/detail")
public class GetDetailServlet extends HttpServlet {

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
	 * GET 요청으로 전달된 managerNo 값을 바탕으로 와이파이 상세 정보를 조회하고,
	 * 해당 정보를 JSON 형식으로 반환합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String managerNo = request.getParameter("managerNo");

		// 필수 파라미터 누락 시 오류 응답
		if (managerNo == null || managerNo.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Manager number is required.");
			return;
		}

		// 상세 정보 조회
		WifiRow detail = wifiService.getWifiDetailByManagerNo(managerNo);

		// 응답 설정
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// 데이터 응답
		if (detail != null) {
			String jsonResponse = new Gson().toJson(detail);
			response.getWriter().write(jsonResponse);
		} else {
			// 데이터 없을 경우 에러 메시지 반환
			response.getWriter().write("{\"error\": \"No details found for the provided manager number.\"}");
		}
	}
}
