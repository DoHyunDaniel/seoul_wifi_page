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

/**
 * 사용자 검색 히스토리 전체 목록과 총 개수를 조회하여 JSON으로 반환하는 서블릿입니다.
 * <p>
 * 요청 URL: /searchHistory  
 * 메서드: GET  
 * 응답: historyList (검색 이력 목록), totalCount (총 개수)
 */
@WebServlet("/searchHistory")
public class SearchHistoryServlet extends HttpServlet {

    private SearchHistoryService searchHistoryService;

    /**
     * 서블릿 초기화 시 SearchHistoryService 인스턴스를 생성합니다.
     */
    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        searchHistoryService = new SearchHistoryService(context);
    }

    /**
     * GET 요청을 처리하여 검색 히스토리 목록과 총 개수를 JSON 형식으로 반환합니다.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<SearchHistory> historyList = searchHistoryService.getSearchHistory();
        int totalCount = searchHistoryService.getTotalCount();

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(new SearchHistoryResponse(historyList, totalCount));
        response.getWriter().write(jsonResponse);
    }

    /**
     * 검색 히스토리 목록과 총 개수를 포함하는 JSON 응답 구조를 정의하는 내부 클래스입니다.
     */
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
