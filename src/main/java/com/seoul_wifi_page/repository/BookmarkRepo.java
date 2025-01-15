package com.seoul_wifi_page.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.Bookmark;

public class BookmarkRepo {
	private String dbUrl;

//  db 연결
	public BookmarkRepo(ServletContext servletContext) {
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
	
	// 테이블 삭제
	public void resetBookmark() {
//		String resetSQL = "DELETE FROM WifiInfo;";
		String resetSQL = "DROP TABLE Bookmark";
		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(resetSQL)) {
			pstmt.executeUpdate();
			System.out.println("Bookmark table reset successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// 북마크 테이블 생성
	public void createBookmarkTable() {
		String createBookmarkTableSQL = "CREATE TABLE IF NOT EXISTS Bookmark ("
				+ "bookmarkId INTEGER PRIMARY KEY AUTOINCREMENT, " + "managerNo TEXT NOT NULL, "
				+ "groupId INTEGER NOT NULL, " + "createdAt TEXT DEFAULT (datetime('now')), " + "editedAt TEXT, "
				+ " FOREIGN KEY (managerNo)  REFERENCES WifiInfo (managerNo), "
				+ " FOREIGN KEY (groupId) REFERENCES BookmarkGroup (groupId) " + ");";

		try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(createBookmarkTableSQL)) {
			stmt.execute();
			System.out.println("Bookmark table created or already exists.");
		} catch (SQLException e) {
			System.err.println("Failed to create Bookmark table.");
			e.printStackTrace();
		}
	}

	// 테이블 초기화
	public void initializeTables() {
		resetBookmark();
		createBookmarkTable();
	}

	// 북마크 테이블 생성
	public void insertBookmark(String wifiId, int bookmarkGroupId) {

		// Bookmark 테이블에 맞는 INSERT SQL
		String insertSQL = "INSERT INTO Bookmark (managerNo, groupId) VALUES (?,?);";

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			// groupName 값을 설정
			pstmt.setString(1, wifiId);
			pstmt.setInt(2, bookmarkGroupId);
			// INSERT 실행
			pstmt.executeUpdate();
			System.out.println("Bookmark info inserted successfully.");
		} catch (SQLException e) {
			System.err.println("Failed to insert bookmark.");
			e.printStackTrace();
		}
	}

	// 북마크 모두 불러오기
	public List<Bookmark> getAllBookmarkInfo(int groupId) {
		String selectSQL = "SELECT w.*, " + "b.bookmarkId, b.createdAt AS b_createdAt, " + "       g.groupName "
				+ "FROM WifiInfo w " + "INNER JOIN Bookmark b ON w.managerNo = b.managerNo "
				+ "INNER JOIN BookmarkGroup g ON b.groupId = g.groupId " + "WHERE b.groupId = ?;";

		List<Bookmark> rows = new ArrayList<>();

		try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
			// 여기서 파라미터 바인딩
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
    // 삭제
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
	// Helper 메서드: ResultSet을 BookmarkRow로 매핑
	private Bookmark mapRow(ResultSet rs) throws SQLException {
		Bookmark row = new Bookmark();
		// wifiRow
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
		// bookmarkGroup
//		row.setGroupId(rs.getInt("groupId"));
		row.setGroupName(rs.getString("groupName"));

		// bookmark
		row.setBookmarkId(rs.getInt("bookmarkId"));
		row.setCreatedAt(rs.getString("b_createdAt"));
		return row;
	}
}
