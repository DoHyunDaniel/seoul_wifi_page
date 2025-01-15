package com.seoul_wifi_page.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.Bookmark;
import com.seoul_wifi_page.repository.BookmarkRepo;

public class BookmarkService {
	private final BookmarkRepo repository;

	public BookmarkService(ServletContext context) {
		this.repository = new BookmarkRepo(context);
	}

	public List<Bookmark> getAllBookmarkInfo(int groupId) {
		return repository.getAllBookmarkInfo(groupId);
	}

	public void saveBookmark(String wifiId, int bookmarkGroupId) {
		repository.createBookmarkTable();
		repository.insertBookmark(wifiId, bookmarkGroupId);
	}

}
