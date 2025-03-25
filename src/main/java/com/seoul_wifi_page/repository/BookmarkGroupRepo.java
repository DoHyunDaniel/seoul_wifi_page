package com.seoul_wifi_page.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.BookmarkGroup;

/**
 * 북마크 그룹(BookmarkGroup) 테이블에 접근하는 Repository 클래스입니다.
 * <p>
 * 테이블 생성, 삽입, 조회, 수정, 삭제 기능을 제공합니다.
 */
public class BookmarkGroupRepo {
	private String dbUrl;

	/**
	 * ServletContext를 통해 DB 경로를 초기화하고 SQLite 드라이버를 로드합니다.
	 *
	 * @param servletContext 서블릿 컨텍스트
	 */
	public BookmarkGroupRepo(ServletContext servletContext) {
		try {
			String dbPath = servletContext.getRealPath("/wifiinfo.db");
			this.dbUrl = "jdbc:sqlite:" + dbPath;
			Class.forName("org.sqlite.JDBC");
			System.out.println("SQLite Driver loaded successfully. DB Path: " + dbUrl);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to load SQLite JDBC Driver.", e);
		}
	}

	/**
	 * SQLite 데이터베이스 연결을 반환합니다.
	 *
	 * @return DB 커넥션
	 * @throws SQLException 연결 실패 시 예외 발생
	 */
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(dbUrl);
	}

	/**
	 * BookmarkGroup 테이블을 생성합니다. (이미 존재하면 무시)
	 */
	public void createBookmarkGroupTable() {
		String createGroupTableSQL = "CREATE TABLE IF NOT EXISTS BookmarkGroup ("
				+ "groupId INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "groupName TEXT NOT NULL, "
				+ "groupOrder INTEGER NOT NULL DEFAULT 1, "
				+ "createdAt TEXT DEFAULT CURRENT_TIMESTAMP, "
				+ "editedAt TEXT DEFAULT NULL);";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createGroupTableSQL)) {
			stmt.execute();
			System.out.println("BookmarkGroup table created or already exists.");
		} catch (SQLException e) {
			System.err.println("Failed to create BookmarkGroup table.");
			e.printStackTrace();
		}
	}

	/**
	 * 북마크 그룹 데이터를 삽입합니다.
	 *
	 * @param groupName  그룹 이름
	 * @param groupOrder 정렬 순서 (1 이상)
	 */
	public void insertBookmarkGroup(String groupName, int groupOrder) {
		if (groupOrder < 1) {
			System.err.println("groupOrder must be greater than 0.");
			return;
		}

		String insertSQL = "INSERT INTO BookmarkGroup (groupName, groupOrder) VALUES (?, ?);";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			pstmt.setString(1, groupName);
			pstmt.setInt(2, groupOrder);
			pstmt.executeUpdate();
			System.out.println("Bookmark group inserted successfully.");
		} catch (SQLException e) {
			System.err.println("Failed to insert bookmark group.");
			e.printStackTrace();
		}
	}

	/**
	 * BookmarkGroup 테이블을 초기화합니다. (create 호출)
	 */
	public void initializeTables() {
		createBookmarkGroupTable();
	}

	/**
	 * 모든 북마크 그룹 정보를 조회합니다.
	 *
	 * @return 북마크 그룹 리스트
	 */
	public List<BookmarkGroup> getAllBookmarkGroupInfo() {
		String selectSQL = "SELECT * FROM BookmarkGroup ORDER BY groupOrder ASC;";
		List<BookmarkGroup> rows = new ArrayList<>();

		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(selectSQL);
			 ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				BookmarkGroup bookmarkGroup = new BookmarkGroup();
				bookmarkGroup.setGroupId(rs.getInt("groupId"));
				bookmarkGroup.setGroupName(rs.getString("groupName"));
				bookmarkGroup.setGroupOrder(rs.getInt("groupOrder"));
				bookmarkGroup.setCreatedAt(rs.getString("createdAt"));
				bookmarkGroup.setEditedAt(rs.getString("editedAt"));
				rows.add(bookmarkGroup);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 * 특정 ID의 북마크 그룹을 삭제합니다.
	 *
	 * @param id 삭제할 그룹 ID
	 */
	public void deleteBookmarkGroup(int id) {
		String deleteSQL = "DELETE FROM BookmarkGroup WHERE groupId = ?;";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			System.out.println("Record with ID " + id + " deleted successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 북마크 그룹 정보를 수정합니다.
	 *
	 * @param bookmarkGroupName  변경할 그룹 이름
	 * @param bookmarkGroupOrder 변경할 정렬 순서
	 * @param bookmarkGroupId    대상 그룹 ID
	 */
	public void updateBookmarkGroup(String bookmarkGroupName, int bookmarkGroupOrder, int bookmarkGroupId) {
		String updateSQL = "UPDATE BookmarkGroup "
				+ "SET groupName = ?, groupOrder = ?, editedAt = CURRENT_TIMESTAMP "
				+ "WHERE groupId = ?;";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
			pstmt.setString(1, bookmarkGroupName);
			pstmt.setInt(2, bookmarkGroupOrder);
			pstmt.setInt(3, bookmarkGroupId);
			pstmt.executeUpdate();
			System.out.println("Record updated.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
