package com.seoul_wifi_page.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;

public class SearchHistoryRepo {

	private final String dbUrl;

	public SearchHistoryRepo(ServletContext servletContext) {
		try {
			String dbPath = servletContext.getRealPath("/wifiinfo.db"); // 루트 디렉터리에 db 파일이 위치
			this.dbUrl = "jdbc:sqlite:" + dbPath;
			Class.forName("org.sqlite.JDBC"); // 드라이버 로드
			System.out.println("SQLite Driver loaded successfully. DB Path: " + dbUrl);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to load SQLite JDBC Driver.", e);
		}
	}

	private Connection connect() throws SQLException {
		return DriverManager.getConnection(dbUrl);
	}

	// 검색 히스토리 테이블 생성
	public void createSearchHistoryTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS searchHistory (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "latitude REAL, " + "longitude REAL, " + "searchDate TEXT);";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
			stmt.execute();
			System.out.println("searchHistory table created or already exists.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 검색 히스토리 저장
	public void insertSearchHistory(double latitude, double longitude, String searchDate) {
		String insertSQL = "INSERT INTO searchHistory (latitude, longitude, searchDate) VALUES (?, ?, ?);";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			pstmt.setDouble(1, latitude);
			pstmt.setDouble(2, longitude);
			pstmt.setString(3, searchDate);
			pstmt.executeUpdate();
			System.out.println("Search history inserted successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 검색 히스토리 조회
	public List<SearchHistory> getSearchHistory() {
		String selectSQL = "SELECT * FROM searchHistory ORDER BY id DESC;";
		List<SearchHistory> historyList = new ArrayList<>();

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(selectSQL);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				SearchHistory history = new SearchHistory();
				history.setId(rs.getInt("id"));
				history.setLatitude(rs.getDouble("latitude"));
				history.setLongitude(rs.getDouble("longitude"));
				history.setSearchDate(rs.getString("searchDate"));
				historyList.add(history);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return historyList;
	}

	// 검색 히스토리 테이블 조회
	public List<SearchHistory> getAllSearchHistory() {
		String query = "SELECT * FROM searchHistory ORDER BY id DESC;";
		List<SearchHistory> historyList = new ArrayList<>();

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				SearchHistory history = new SearchHistory();
				history.setId(rs.getInt("id"));
				history.setLongitude(rs.getDouble("longitude"));
				history.setLatitude(rs.getDouble("latitude"));
				history.setSearchDate(rs.getString("searchDate"));
				historyList.add(history);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return historyList;
	}

	// 테이블 전체 개수 가져오기
	public int getTotalCount() {
		String query = "SELECT COUNT(*) AS total FROM searchHistory;";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(query);
				ResultSet rs = pstmt.executeQuery()) {

			if (rs.next()) {
				return rs.getInt("total");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 검색 히스토리 삭제
	public void deleteSearchHistory(int id) {
		String deleteSQL = "DELETE FROM searchHistory WHERE id = ?;";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			System.out.println("Record with ID " + id + " deleted successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
