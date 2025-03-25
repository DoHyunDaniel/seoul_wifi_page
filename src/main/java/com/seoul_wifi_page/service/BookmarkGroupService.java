package com.seoul_wifi_page.service;

import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.BookmarkGroup;
import com.seoul_wifi_page.repository.BookmarkGroupRepo;

/**
 * 북마크 그룹 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * <p>
 * 그룹 생성, 조회, 수정 기능을 제공합니다.
 */
public class BookmarkGroupService {

	private final BookmarkGroupRepo repository;

	/**
	 * ServletContext를 이용하여 북마크 그룹 저장소(Repo)를 초기화합니다.
	 *
	 * @param context 서블릿 컨텍스트
	 */
	public BookmarkGroupService(ServletContext context) {
		this.repository = new BookmarkGroupRepo(context);
	}

	/**
	 * 모든 북마크 그룹 정보를 조회합니다.
	 *
	 * @return 북마크 그룹 리스트
	 */
	public List<BookmarkGroup> getAllBookmarkGroupInfo() {
		return repository.getAllBookmarkGroupInfo();
	}

	/**
	 * 새로운 북마크 그룹을 생성하여 저장합니다.
	 * <p>
	 * 테이블이 없을 경우 먼저 생성합니다.
	 *
	 * @param bookmarkGroupName  그룹 이름
	 * @param bookmarkGroupOrder 그룹 정렬 순서
	 */
	public void saveBookmarkGroup(String bookmarkGroupName, int bookmarkGroupOrder) {
		repository.createBookmarkGroupTable();
		repository.insertBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder);
	}

	/**
	 * 기존 북마크 그룹의 정보를 수정합니다.
	 *
	 * @param bookmarkGroupName  새로운 그룹 이름
	 * @param bookmarkGroupOrder 새로운 정렬 순서
	 * @param bookmarkGroupId    수정할 그룹 ID
	 */
	public void updateBookmarkGroup(String bookmarkGroupName, int bookmarkGroupOrder, int bookmarkGroupId) {
		repository.updateBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder, bookmarkGroupId);
	}
}
