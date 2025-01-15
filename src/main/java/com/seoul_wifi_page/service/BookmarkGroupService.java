package com.seoul_wifi_page.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.BookmarkGroup;
import com.seoul_wifi_page.repository.BookmarkGroupRepo;

public class BookmarkGroupService {
	private final BookmarkGroupRepo repository;

	public BookmarkGroupService(ServletContext context) {
		this.repository = new BookmarkGroupRepo(context);
	}

	public List<BookmarkGroup> getAllBookmarkGroupInfo() {
		return repository.getAllBookmarkGroupInfo();
	}

	public void saveBookmarkGroup(String bookmarkGroupName, int bookmarkGroupOrder) {
		repository.createBookmarkGroupTable();
		repository.insertBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder);
	}

	public void updateBookmarkGroup(String bookmarkGroupName, int bookmarkGroupOrder, int bookmarkGroupId) {
		repository.updateBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder, bookmarkGroupId);
	}
}
