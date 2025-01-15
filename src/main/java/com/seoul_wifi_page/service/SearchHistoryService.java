package com.seoul_wifi_page.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.repository.SearchHistoryRepo;

public class SearchHistoryService {

    private final SearchHistoryRepo repository;

    public SearchHistoryService(ServletContext context) {
        this.repository = new SearchHistoryRepo(context);
    }

    public List<SearchHistory> getSearchHistory() {
        return repository.getAllSearchHistory();
    }

    public int getTotalCount() {
        return repository.getTotalCount();
    }
}
