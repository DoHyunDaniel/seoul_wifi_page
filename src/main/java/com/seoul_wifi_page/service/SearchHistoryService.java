package com.seoul_wifi_page.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.repository.SearchHistoryRepo;

/**
 * 검색 기록을 조회하는 기능을 담당하는 서비스 클래스입니다.
 * <p>
 * 전체 검색 이력 조회 및 검색 기록 수 조회 기능을 제공합니다.
 */
public class SearchHistoryService {

    private final SearchHistoryRepo repository;

    /**
     * ServletContext를 기반으로 SearchHistoryRepo를 초기화합니다.
     *
     * @param context 서블릿 컨텍스트
     */
    public SearchHistoryService(ServletContext context) {
        this.repository = new SearchHistoryRepo(context);
    }

    /**
     * 저장된 전체 검색 이력 목록을 반환합니다.
     *
     * @return 검색 이력 리스트
     */
    public List<SearchHistory> getSearchHistory() {
        return repository.getAllSearchHistory();
    }

    /**
     * 저장된 검색 이력의 총 개수를 반환합니다.
     *
     * @return 검색 이력 수
     */
    public int getTotalCount() {
        return repository.getTotalCount();
    }
}
