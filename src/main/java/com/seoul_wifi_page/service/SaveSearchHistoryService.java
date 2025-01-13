package com.seoul_wifi_page.service;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.repository.SearchHistoryRepository;

public class SaveSearchHistoryService {

	private final SearchHistoryRepository repository;

	public SaveSearchHistoryService(ServletContext context) {
		this.repository = new SearchHistoryRepository(context);
	}

	public void saveSearchHistory(double userLat, double userLon, String SearchDate) {
		repository.createSearchHistoryTable();
		repository.insertSearchHistory(userLat, userLon, SearchDate);
	}
}
