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
import com.seoul_wifi_page.service.WifiService;

@WebServlet("/get-results")
public class GetResultsServlet extends HttpServlet {

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
            // DB에서 상위 20개의 WiFi 데이터 가져오기
            List<WifiRow> wifiData = wifiService.getTopWifiInfo(20);

            // 총 WiFi 데이터 개수 가져오기
            int totalCount = wifiService.getWifiTotalCount();

            // JSON 응답 준비
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 데이터가 있으면 JSON 변환 후 응답
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

    // 응답 데이터 구조를 정의하는 내부 클래스
    private static class ResultsResponse {
        private List<WifiRow> wifiData;
        private int totalCount;

        public ResultsResponse(List<WifiRow> wifiData, int totalCount) {
            this.wifiData = wifiData;
            this.totalCount = totalCount;
        }
    }
}