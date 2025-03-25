package com.seoul_wifi_page.service;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.repository.SearchHistoryRepo;

/**
 * 사용자의 검색 기록을 저장하는 서비스 클래스입니다.
 * <p>
 * 위도/경도 및 검색 일자를 기록하며, 필요 시 테이블도 자동 생성합니다.
 */
public class SaveSearchHistoryService {

	private final SearchHistoryRepo repository;

	/**
	 * ServletContext를 기반으로 SearchHistoryRepo를 초기화합니다.
	 *
	 * @param context 서블릿 컨텍스트
	 */
	public SaveSearchHistoryService(ServletContext context) {
		this.repository = new SearchHistoryRepo(context);
	}

	/**
	 * 검색 이력을 DB에 저장합니다.
	 * <p>
	 * 테이블이 없을 경우 먼저 생성하며, 사용자 위치와 날짜 정보를 저장합니다.
	 *
	 * @param userLat    사용자 위도
	 * @param userLon    사용자 경도
	 * @param SearchDate 검색 일자 (예: yyyy-MM-dd HH:mm:ss)
	 */
	public void saveSearchHistory(double userLat, double userLon, String SearchDate) {
		repository.createSearchHistoryTable();
		repository.insertSearchHistory(userLat, userLon, SearchDate);
	}
}
