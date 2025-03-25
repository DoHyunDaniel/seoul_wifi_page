package com.seoul_wifi_page.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.Bookmark;
import com.seoul_wifi_page.repository.BookmarkRepo;

/**
 * 북마크 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * <p>
 * 북마크 생성, 조회 기능을 제공합니다.
 */
public class BookmarkService {

	private final BookmarkRepo repository;

	/**
	 * ServletContext를 기반으로 BookmarkRepo를 초기화합니다.
	 *
	 * @param context 서블릿 컨텍스트
	 */
	public BookmarkService(ServletContext context) {
		this.repository = new BookmarkRepo(context);
	}

	/**
	 * 특정 북마크 그룹에 속한 모든 북마크 정보를 조회합니다.
	 *
	 * @param groupId 조회할 북마크 그룹 ID
	 * @return 해당 그룹에 속한 북마크 리스트
	 */
	public List<Bookmark> getAllBookmarkInfo(int groupId) {
		return repository.getAllBookmarkInfo(groupId);
	}

	/**
	 * 새로운 북마크를 생성하여 저장합니다.
	 * <p>
	 * 내부적으로 북마크 테이블이 없다면 먼저 생성합니다.
	 *
	 * @param wifiId          북마크할 와이파이 ID
	 * @param bookmarkGroupId 북마크가 속할 그룹 ID
	 */
	public void saveBookmark(String wifiId, int bookmarkGroupId) {
		repository.createBookmarkTable();
		repository.insertBookmark(wifiId, bookmarkGroupId);
	}
}
