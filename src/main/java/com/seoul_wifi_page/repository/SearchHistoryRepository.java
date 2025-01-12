package com.seoul_wifi_page.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.seoul_wifi_page.dto.SearchHistory;

public class SearchHistoryRepository {

    private final String dbUrl;

    public SearchHistoryRepository(ServletContext context) {
        String dbPath = context.getRealPath("/wifiinfo.db");
        this.dbUrl = "jdbc:sqlite:" + dbPath;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

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
}
