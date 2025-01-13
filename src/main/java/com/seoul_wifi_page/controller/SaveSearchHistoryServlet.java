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
import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.service.SaveSearchHistoryService;

@WebServlet("/saveSearchHistory")
public class SaveSearchHistoryServlet extends HttpServlet {
	private SaveSearchHistoryService searchHistoryService;

	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		searchHistoryService = new SaveSearchHistoryService(context);
	}

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