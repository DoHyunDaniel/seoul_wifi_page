package com.seoul_wifi_page.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.Bookmark;

/**
 * Bookmark 테이블에 접근하여 북마크 데이터를 처리하는 Repository 클래스입니다.
 * <p>
 * 테이블 생성, 초기화, 삽입, 조회, 삭제 기능을 제공합니다.
 */
public class BookmarkRepo {
	private String dbUrl;

	/**
	 * ServletContext를 기반으로 SQLite DB 연결을 설정합니다.
	 *
	 * @param servletContext 서블릿 컨텍스트
	 */
	public BookmarkRepo(ServletContext servletContext) {
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
	 * DB 연결을 반환합니다.
	 *
	 * @return Connection 객체
	 * @throws SQLException 연결 실패 시 발생
	 */
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(dbUrl);
	}

	/**
	 * Bookmark 테이블을 삭제합니다. (DROP TABLE)
	 */
	public void resetBookmark() {
		String resetSQL = "DROP TABLE Bookmark";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(resetSQL)) {
			pstmt.executeUpdate();
			System.out.println("Bookmark table reset successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Bookmark 테이블을 생성합니다.
	 */
	public void createBookmarkTable() {
		String createBookmarkTableSQL = "CREATE TABLE IF NOT EXISTS Bookmark ("
				+ "bookmarkId INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "managerNo TEXT NOT NULL, "
				+ "groupId INTEGER NOT NULL, "
				+ "createdAt TEXT DEFAULT (datetime('now')), "
				+ "editedAt TEXT, "
				+ "FOREIGN KEY (managerNo) REFERENCES WifiInfo (managerNo), "
				+ "FOREIGN KEY (groupId) REFERENCES BookmarkGroup (groupId) );";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createBookmarkTableSQL)) {
			stmt.execute();
			System.out.println("Bookmark table created or already exists.");
		} catch (SQLException e) {
			System.err.println("Failed to create Bookmark table.");
			e.printStackTrace();
		}
	}

	/**
	 * 테이블을 재설정하고 다시 생성합니다.
	 */
	public void initializeTables() {
		resetBookmark();
		createBookmarkTable();
	}

	/**
	 * Bookmark 테이블에 북마크 데이터를 삽입합니다.
	 *
	 * @param wifiId         와이파이 고유 ID (managerNo)
	 * @param bookmarkGroupId 북마크 그룹 ID
	 */
	public void insertBookmark(String wifiId, int bookmarkGroupId) {
		String insertSQL = "INSERT INTO Bookmark (managerNo, groupId) VALUES (?,?);";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			pstmt.setString(1, wifiId);
			pstmt.setInt(2, bookmarkGroupId);
			pstmt.executeUpdate();
			System.out.println("Bookmark info inserted successfully.");
		} catch (SQLException e) {
			System.err.println("Failed to insert bookmark.");
			e.printStackTrace();
		}
	}

	/**
	 * 특정 그룹 ID에 속한 모든 북마크 정보를 조회합니다.
	 *
	 * @param groupId 북마크 그룹 ID
	 * @return Bookmark 객체 리스트
	 */
	public List<Bookmark> getAllBookmarkInfo(int groupId) {
		String selectSQL = "SELECT w.*, "
				+ "b.bookmarkId, b.createdAt AS b_createdAt, "
				+ "g.groupName "
				+ "FROM WifiInfo w "
				+ "INNER JOIN Bookmark b ON w.managerNo = b.managerNo "
				+ "INNER JOIN BookmarkGroup g ON b.groupId = g.groupId "
				+ "WHERE b.groupId = ?;";

		List<Bookmark> rows = new ArrayList<>();

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
			pstmt.setInt(1, groupId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Bookmark row = mapRow(rs);
					rows.add(row);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	/**
	 * 특정 북마크 ID의 레코드를 삭제합니다.
	 *
	 * @param id 삭제할 북마크 ID
	 */
	public void deleteBookmark(int id) {
		String deleteSQL = "DELETE FROM Bookmark WHERE bookmarkId = ?;";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			System.out.println("Record with ID " + id + " deleted successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ResultSet으로부터 Bookmark 객체를 생성합니다.
	 *
	 * @param rs ResultSet
	 * @return 매핑된 Bookmark 객체
	 * @throws SQLException SQL 오류 발생 시
	 */
	private Bookmark mapRow(ResultSet rs) throws SQLException {
		Bookmark row = new Bookmark();

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

		row.setGroupName(rs.getString("groupName"));
		row.setBookmarkId(rs.getInt("bookmarkId"));
		row.setCreatedAt(rs.getString("b_createdAt"));

		return row;
	}
}
