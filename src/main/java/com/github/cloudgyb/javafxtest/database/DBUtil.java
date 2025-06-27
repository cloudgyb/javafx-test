package com.github.cloudgyb.javafxtest.database;

import com.github.cloudgyb.javafxtest.domain.UrlTestHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cloudgyb
 * @since 2025/6/23 22:15
 */
public class DBUtil {
    public static final String DB_URL = "jdbc:sqlite:url_test_history.db";
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";
    public static final String DB_TABLE_NAME = "url_test_history";
    public static final String DB_COLUMN_ID = "id";
    public static final String DB_COLUMN_URL = "url";
    public static final String DB_COLUMN_STATUS_CODE = "status_code";
    public static final String DB_COLUMN_LOAD_TIME = "load_time";
    public static final String DB_COLUMN_TEST_TIME = "test_time";
    public static final String DB_COLUMN_TEST_ERROR_INFO = "test_error_info";

    public static final String DB_CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME + "(" +
            DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DB_COLUMN_URL + " TEXT NOT NULL," +
            DB_COLUMN_STATUS_CODE + " INTEGER NOT NULL," +
            DB_COLUMN_LOAD_TIME + " REAL NOT NULL," +
            DB_COLUMN_TEST_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            DB_COLUMN_TEST_ERROR_INFO + " TEXT DEFAULT ''" +
            ");";
    public static final String DB_INSERT_SQL = "INSERT INTO " + DB_TABLE_NAME + "(" +
            DB_COLUMN_URL + "," +
            DB_COLUMN_STATUS_CODE + "," +
            DB_COLUMN_LOAD_TIME + "," +
            DB_COLUMN_TEST_TIME + "," +
            DB_COLUMN_TEST_ERROR_INFO +
            ") VALUES(?,?,?,?,?)";
    public static final String DB_SELECT_ALL_SQL = "SELECT * FROM " + DB_TABLE_NAME;
    public static final String DB_DELETE_ALL_SQL = "DELETE FROM " + DB_TABLE_NAME;
    public static final String DB_DELETE_SQL = "DELETE FROM " + DB_TABLE_NAME + " WHERE " + DB_COLUMN_ID + "=?";
    public static final String DB_UPDATE_SQL = "UPDATE " + DB_TABLE_NAME + " SET " +
            DB_COLUMN_URL + "=?," +
            DB_COLUMN_STATUS_CODE + "=?," +
            DB_COLUMN_LOAD_TIME + "=? WHERE "
            + DB_COLUMN_ID + "=?";
    public static final String DB_SELECT_SQL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COLUMN_ID + "=?";
    public static final String DB_SELECT_SQL_BY_URL = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COLUMN_URL + "=?";
    public static final String DB_SELECT_SQL_BY_STATUS_CODE = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COLUMN_STATUS_CODE
            + "= ? ORDER BY " + DB_COLUMN_TEST_TIME + " DESC";
    public static final String DB_SELECT_SQL_BY_LOAD_TIME = "SELECT * FROM " + DB_TABLE_NAME + " WHERE " + DB_COLUMN_LOAD_TIME
            + "= ? ORDER BY " + DB_COLUMN_TEST_TIME + " DESC";
    public static final String DB_SELECT_SQL_BY_TEST_TIME = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY " + DB_COLUMN_TEST_TIME + " DESC";
    public static final String DB_SELECT_SQL_BY_TEST_TIME_DESC = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY " + DB_COLUMN_TEST_TIME + " ASC";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(DB_CREATE_TABLE_SQL)) {
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("创建表成功");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<UrlTestHistory> selectAll() throws SQLException {
        List<UrlTestHistory> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement(DB_SELECT_ALL_SQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UrlTestHistory urlTestHistory = new UrlTestHistory();
                urlTestHistory.setId(rs.getInt(DB_COLUMN_ID));
                urlTestHistory.setUrl(rs.getString(DB_COLUMN_URL));

                urlTestHistory.setStatusCode(rs.getInt(DB_COLUMN_STATUS_CODE));
                urlTestHistory.setLoadTime(rs.getLong(DB_COLUMN_LOAD_TIME));
                urlTestHistory.setTestTime(rs.getTimestamp(DB_COLUMN_TEST_TIME));
                urlTestHistory.setTestErrorInfo(rs.getString(DB_COLUMN_TEST_ERROR_INFO));
                list.add(urlTestHistory);
            }
        }
        return list;
    }

    public static void insert(UrlTestHistory urlTestHistory) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DB_INSERT_SQL)) {
            ps.setString(1, urlTestHistory.getUrl());
            ps.setInt(2, urlTestHistory.getStatusCode());
            ps.setLong(3, urlTestHistory.getLoadTime());
            ps.setTimestamp(4, urlTestHistory.getTestTime());
            ps.setString(5, urlTestHistory.getTestErrorInfo());
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("插入成功");
            }
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void delete(Integer id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DB_DELETE_SQL)) {
            ps.setInt(1, id);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("删除成功");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
