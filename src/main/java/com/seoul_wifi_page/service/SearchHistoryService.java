package com.seoul_wifi_page.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.repository.SearchHistoryRepository;

public class SearchHistoryService {

    private final SearchHistoryRepository repository;

    public SearchHistoryService(ServletContext context) {
        this.repository = new SearchHistoryRepository(context);
    }

    public List<SearchHistory> getSearchHistory() {
        return repository.getAllSearchHistory();
    }

    public int getTotalCount() {
        return repository.getTotalCount();
    }
}
