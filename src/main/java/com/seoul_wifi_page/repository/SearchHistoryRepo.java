package com.seoul_wifi_page.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;

/**
 * searchHistory 테이블에 접근하여 사용자 검색 이력을 처리하는 Repository 클래스입니다.
 * <p>
 * 검색 기록 저장, 조회, 삭제 및 테이블 관리 기능을 제공합니다.
 */
public class SearchHistoryRepo {

	private final String dbUrl;

	/**
	 * ServletContext를 이용하여 DB 경로를 설정하고 드라이버를 로드합니다.
	 *
	 * @param servletContext 서블릿 컨텍스트
	 */
	public SearchHistoryRepo(ServletContext servletContext) {
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
	 * DB 연결 객체를 반환합니다.
	 *
	 * @return Connection 객체
	 * @throws SQLException 연결 실패 시 예외 발생
	 */
	private Connection connect() throws SQLException {
		return DriverManager.getConnection(dbUrl);
	}

	/**
	 * 검색 기록 테이블을 생성합니다.
	 */
	public void createSearchHistoryTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS searchHistory ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "latitude REAL, "
				+ "longitude REAL, "
				+ "searchDate TEXT);";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
			stmt.execute();
			System.out.println("searchHistory table created or already exists.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 검색 기록을 DB에 저장합니다.
	 *
	 * @param latitude    사용자 위도
	 * @param longitude   사용자 경도
	 * @param searchDate  검색 시각 (예: yyyy-MM-dd HH:mm:ss)
	 */
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

	/**
	 * 검색 기록을 최신순으로 모두 조회합니다.
	 *
	 * @return 검색 기록 리스트
	 */
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

	/**
	 * 전체 검색 기록을 조회합니다. (중복 메서드로 보이지만 유지)
	 *
	 * @return 검색 기록 리스트
	 */
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

	/**
	 * 저장된 검색 기록의 총 개수를 반환합니다.
	 *
	 * @return 검색 기록 개수
	 */
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

	/**
	 * 특정 ID의 검색 기록을 삭제합니다.
	 *
	 * @param id 삭제할 검색 기록 ID
	 */
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
