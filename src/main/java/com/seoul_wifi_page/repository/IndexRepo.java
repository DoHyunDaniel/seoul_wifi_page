package com.seoul_wifi_page.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.WifiRow;

/**
 * WifiInfo 테이블에 접근하여 와이파이 정보를 저장하고 조회하는 Repository 클래스입니다.
 * <p>
 * - 테이블 생성, 초기화, 삽입, 전체 조회, 거리순 조회, 개수 조회, 상세 조회 기능 제공
 */
public class IndexRepo {
	private String dbUrl;

	/**
	 * ServletContext를 통해 DB 경로를 설정하고 드라이버를 로드합니다.
	 *
	 * @param servletContext 서블릿 컨텍스트
	 */
	public IndexRepo(ServletContext servletContext) {
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
	 * SQLite DB 연결 객체를 반환합니다.
	 *
	 * @return Connection 객체
	 * @throws SQLException 연결 실패 시 예외
	 */
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(dbUrl);
	}

	/**
	 * WifiInfo 테이블을 생성합니다. (존재하지 않을 경우만)
	 */
	public void createTable() {
		String createTableSQL = "CREATE TABLE IF NOT EXISTS WifiInfo ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "managerNo TEXT, wardOffice TEXT, mainName TEXT, address1 TEXT, address2 TEXT, "
				+ "instlFloor TEXT, instlTy TEXT, instlMby TEXT, svcSe TEXT, cmcwr TEXT, "
				+ "cnstcYear TEXT, inoutDoor TEXT, remars3 TEXT, "
				+ "latitude REAL, longitude REAL, workDttm TEXT, distance REAL);";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
			stmt.execute();
			System.out.println("WifiInfo table created or already exists.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * WifiRow 객체를 WifiInfo 테이블에 삽입합니다.
	 *
	 * @param row 삽입할 와이파이 정보 객체
	 */
	public void insertWifiInfo(WifiRow row) {
		String insertSQL = "INSERT INTO WifiInfo ("
				+ "managerNo, wardOffice, mainName, address1, address2, "
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
			pstmt.setDouble(17, row.getDistance());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 모든 와이파이 데이터를 조회합니다.
	 *
	 * @return WifiRow 리스트
	 */
	public List<WifiRow> getAllWifiInfo() {
		String selectSQL = "SELECT * FROM WifiInfo;";
		List<WifiRow> rows = new ArrayList<>();

		try (Connection conn = connect();
			 PreparedStatement pstmt = conn.prepareStatement(selectSQL);
			 ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				rows.add(mapRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 * 거리 기준으로 가까운 와이파이 데이터를 조회합니다.
	 *
	 * @param limit 조회할 개수 제한
	 * @return WifiRow 리스트
	 */
	public List<WifiRow> getTopWifiInfo(int limit) {
		String selectSQL = "SELECT * FROM WifiInfo ORDER BY distance ASC LIMIT ?;";
		List<WifiRow> rows = new ArrayList<>();

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
			pstmt.setInt(1, limit);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					rows.add(mapRow(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 * WifiInfo 테이블을 삭제합니다. (DROP TABLE)
	 */
	public void resetWifiInfo() {
		String resetSQL = "DROP TABLE WifiInfo";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(resetSQL)) {
			pstmt.executeUpdate();
			System.out.println("WifiInfo table reset successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 저장된 와이파이 총 개수를 반환합니다.
	 *
	 * @return 와이파이 개수
	 */
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
		return 0;
	}

	/**
	 * managerNo를 기준으로 특정 와이파이 상세 정보를 조회합니다.
	 *
	 * @param managerNo 와이파이 ID
	 * @return 해당 와이파이 정보 (없으면 null)
	 */
	public WifiRow getWifiByManagerNo(String managerNo) {
		String query = "SELECT * FROM WifiInfo WHERE managerNo = ?;";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, managerNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapRow(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ResultSet을 WifiRow 객체로 매핑합니다.
	 *
	 * @param rs ResultSet
	 * @return WifiRow 객체
	 * @throws SQLException SQL 오류 발생 시
	 */
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
}