package com.seoul_wifi_page.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;
import com.seoul_wifi_page.dto.WifiRow;

public class SQLiteHelper {
	private String dbUrl;

	// 생성자에서 ServletContext를 받아 DB 경로 설정
	public SQLiteHelper(ServletContext servletContext) {
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

	// 테이블 생성 (새로운 필드 포함)
	public void createTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS WifiInfo (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "managerNo TEXT, " + "wardOffice TEXT, " + "mainName TEXT, " + "address1 TEXT, " + "address2 TEXT, "
				+ "instlFloor TEXT, " + "instlTy TEXT, " + "instlMby TEXT, " + "svcSe TEXT, " + "cmcwr TEXT, "
				+ "cnstcYear TEXT, " + "inoutDoor TEXT, " + "remars3 TEXT, " + "latitude REAL, " + "longitude REAL, "
				+ "workDttm TEXT," + "distance REAL" + ");";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
			stmt.execute();
			System.out.println("WifiInfo table created or already exists.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// WiFi 정보 삽입
	public void insertWifiInfo(WifiRow row) {
		String insertSQL = "INSERT INTO WifiInfo (" + "managerNo, wardOffice, mainName, address1, address2, "
				+ "instlFloor, instlTy, instlMby, svcSe, cmcwr, "
				+ "cnstcYear, inoutDoor, remars3, latitude, longitude, workDttm, distance) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			pstmt.setString(1, row.getManagerNo());
			pstmt.setString(2, row.getWardOffice());
			pstmt.setString(3, row.getMainName());
			pstmt.setString(4, row.getAddress1());
			pstmt.setString(5, row.getAddress2());
			pstmt.setString(6, row.getInstlFloor());
			pstmt.setString(7, row.getInstlTy());
			pstmt.setString(8, row.getInstlMby());
			pstmt.setString(9, row.getSvcSe());
			pstmt.setString(10, row.getCmcwr());
			pstmt.setString(11, row.getCnstcYear());
			pstmt.setString(12, row.getInoutDoor());
			pstmt.setString(13, row.getRemars3());
			pstmt.setDouble(14, row.getLatitude());
			pstmt.setDouble(15, row.getLongitude());
			pstmt.setString(16, row.getWorkDttm());
			pstmt.setDouble(17, row.getDistance()); // distance 값 설정
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 모든 WiFi 정보 가져오기
	public List<WifiRow> getAllWifiInfo() {
		String selectSQL = "SELECT * FROM WifiInfo;";
		List<WifiRow> rows = new ArrayList<>();

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(selectSQL);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				WifiRow row = mapRow(rs);
				rows.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	// 제한된 WiFi 정보 가져오기
	public List<WifiRow> getTopWifiInfo(int limit) {
		String selectSQL = "SELECT * FROM WifiInfo ORDER BY distance ASC LIMIT ?;";
		List<WifiRow> rows = new ArrayList<>();

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
			pstmt.setInt(1, limit);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					WifiRow row = mapRow(rs);
					rows.add(row);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	// Helper 메서드: ResultSet을 WifiRow로 매핑
	private WifiRow mapRow(ResultSet rs) throws SQLException {
		WifiRow row = new WifiRow();
		row.setManagerNo(rs.getString("managerNo"));
		row.setWardOffice(rs.getString("wardOffice"));
		row.setMainName(rs.getString("mainName"));
		row.setAddress1(rs.getString("address1"));
		row.setAddress2(rs.getString("address2"));
		row.setInstlFloor(rs.getString("instlFloor"));
		row.setInstlTy(rs.getString("instlTy"));
		row.setInstlMby(rs.getString("instlMby"));
		row.setSvcSe(rs.getString("svcSe"));
		row.setCmcwr(rs.getString("cmcwr"));
		row.setCnstcYear(rs.getString("cnstcYear"));
		row.setInoutDoor(rs.getString("inoutDoor"));
		row.setRemars3(rs.getString("remars3"));
		row.setLatitude(rs.getDouble("latitude"));
		row.setLongitude(rs.getDouble("longitude"));
		row.setWorkDttm(rs.getString("workDttm"));
		row.setDistance(rs.getDouble("distance"));
		return row;
	}

	// 데이터 초기화
	public void resetWifiInfo() {
//		String resetSQL = "DELETE FROM WifiInfo;";
		String resetSQL = "DROP TABLE WifiInfo";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(resetSQL)) {
			pstmt.executeUpdate();
			System.out.println("WifiInfo table reset successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getWifiCount() {
		String countSQL = "SELECT COUNT(*) AS count FROM WifiInfo;";
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(countSQL);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0; // 기본값 반환
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
