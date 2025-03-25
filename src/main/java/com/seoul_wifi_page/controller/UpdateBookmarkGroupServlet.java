package com.seoul_wifi_page.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seoul_wifi_page.service.BookmarkGroupService;

/**
 * 기존 북마크 그룹 정보를 수정하는 서블릿입니다.
 * <p>
 * 요청 URL: /updateBookmarkGroup  
 * 메서드: POST
 * 요청 파라미터: bookmarkGroupName, bookmarkGroupOrder, groupId  
 * 응답: JSON 메시지 (성공 또는 실패)
 */
@WebServlet("/updateBookmarkGroup")
public class UpdateBookmarkGroupServlet extends HttpServlet {

	private BookmarkGroupService bookmarkGroupService;

	/**
	 * 서블릿 초기화 시 BookmarkGroupService를 생성합니다.
	 */
	@Override
	public void init() throws ServletException {
		ServletContext context = getServletContext();
		bookmarkGroupService = new BookmarkGroupService(context);
	}

	/**
	 * POST 요청을 통해 전달받은 북마크 그룹 ID에 대해
	 * 이름과 정렬 순서를 수정하고 JSON 응답을 반환합니다.
	 *
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			request.setCharacterEncoding("UTF-8");

			String bookmarkGroupName = request.getParameter("bookmarkGroupName");
			int bookmarkGroupOrder = Integer.parseInt(request.getParameter("bookmarkGroupOrder"));
			int bookmarkGroupId = Integer.parseInt(request.getParameter("groupId"));

			bookmarkGroupService.updateBookmarkGroup(bookmarkGroupName, bookmarkGroupOrder, bookmarkGroupId);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"message\": \"Bookmark Group updated successfully.\"}");

		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"error\": \"저장 중 문제가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}
}
