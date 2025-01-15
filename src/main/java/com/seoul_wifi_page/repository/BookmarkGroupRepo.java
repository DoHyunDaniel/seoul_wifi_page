package com.seoul_wifi_page.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.BookmarkGroup;
import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.dto.WifiRow;

public class BookmarkGroupRepo {
	private String dbUrl;

//  db 연결
	public BookmarkGroupRepo(ServletContext servletContext) {
		try {
			String dbPath = servletContext.getRealPath("/wifiinfo.db"); // 루트 디렉터리에 db 파일이 위치
			this.dbUrl = "jdbc:sqlite:" + dbPath;
			Class.forName("org.sqlite.JDBC"); // 드라이버 로드
			System.out.println("SQLite Driver loaded successfully. DB Path: " + dbUrl);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to load SQLite JDBC Driver.", e);
		}
	}

	public Connection connect() throws SQLException {
		return DriverManager.getConnection(dbUrl);
	}

	// 북마크그룹 테이블 생성
	public void createBookmarkGroupTable() {
		String createGroupTableSQL = "CREATE TABLE IF NOT EXISTS BookmarkGroup ("
				+ "groupId INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "groupName TEXT NOT NULL, groupOrder INTEGER NOT NULL DEFAULT 1,"
				+ "createdAt TEXT DEFAULT CURRENT_TIMESTAMP, editedAt TEXT DEFAULT NULL" + ");";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createGroupTableSQL)) {
			stmt.execute();
			System.out.println("BookmarkGroup table created or already exists.");
		} catch (SQLException e) {
			System.err.println("Failed to create BookmarkGroup table.");
			e.printStackTrace();
		}
	}

	// 북마크 그룹 데이터 저장
	public void insertBookmarkGroup(String groupName, int groupOrder) {
		// groupOrder 유효성 검사
		if (groupOrder < 1) {
			System.err.println("groupOrder must be greater than 0.");
			return;
		}

		// BookmarkGroup 테이블에 맞는 INSERT SQL
		String insertSQL = "INSERT INTO BookmarkGroup (groupName, groupOrder) VALUES (?,?);";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			// groupName 값을 설정
			pstmt.setString(1, groupName);
			pstmt.setInt(2, groupOrder);
			// INSERT 실행
			pstmt.executeUpdate();
			System.out.println("Bookmark group inserted successfully.");
		} catch (SQLException e) {
			System.err.println("Failed to insert bookmark group.");
			e.printStackTrace();
		}
	}

	// 테이블 초기화
	public void initializeTables() {
		createBookmarkGroupTable();
	}

	// 모든 북마크 정보 가져오기
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

	// 북마크 그룹 삭제
	// 검색 히스토리 삭제
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

	// 검색 히스토리 업데이트
	public void updateBookmarkGroup(String bookmarkGroupName, int bookmarkGroupOrder, int bookmarkGroupId) {
		String updateSQL = "UPDATE BookmarkGroup " + "SET groupName = ?, groupOrder = ?, editedAt = CURRENT_TIMESTAMP "
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
