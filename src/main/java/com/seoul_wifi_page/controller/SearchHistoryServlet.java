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
import com.seoul_wifi_page.service.SearchHistoryService;

@WebServlet("/searchHistoryData")
public class SearchHistoryServlet extends HttpServlet {

    private SearchHistoryService searchHistoryService;

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        searchHistoryService = new SearchHistoryService(context);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<SearchHistory> historyList = searchHistoryService.getSearchHistory();
        int totalCount = searchHistoryService.getTotalCount();

        // JSON 형태로 반환
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new SearchHistoryResponse(historyList, totalCount));
        response.getWriter().write(jsonResponse);
    }

    // 내부 클래스: 응답 객체 정의
    private static class SearchHistoryResponse {
        private List<SearchHistory> historyList;
        private int totalCount;

        public SearchHistoryResponse(List<SearchHistory> historyList, int totalCount) {
            this.historyList = historyList;
            this.totalCount = totalCount;
        }

        public List<SearchHistory> getHistoryList() {
            return historyList;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }
}
