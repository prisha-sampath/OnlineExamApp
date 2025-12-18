package com.examapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizResultsDao {
    
    public static void saveQuizResult(String username, String quizType, int score, int totalQuestions) {
        int percentage = (score * 100) / totalQuestions;
        String sql = "INSERT INTO quiz_results (username, quiz_type, score, total_questions, percentage, attempt_date) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, quizType);
            pstmt.setInt(3, score);
            pstmt.setInt(4, totalQuestions);
            pstmt.setInt(5, percentage);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Map<String, Object>> getAllResults() {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT username, quiz_type, score, total_questions, percentage, attempt_date FROM quiz_results ORDER BY attempt_date DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("username", rs.getString("username"));
                row.put("quizType", rs.getString("quiz_type"));
                row.put("score", rs.getInt("score"));
                row.put("totalQuestions", rs.getInt("total_questions"));
                row.put("percentage", rs.getInt("percentage"));
                row.put("attemptDate", rs.getTimestamp("attempt_date"));
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return results;
    }
    
    public static List<Map<String, Object>> getResultsByUsername(String username) {
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT username, quiz_type, score, total_questions, percentage, attempt_date FROM quiz_results WHERE username = ? ORDER BY attempt_date DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("username", rs.getString("username"));
                    row.put("quizType", rs.getString("quiz_type"));
                    row.put("score", rs.getInt("score"));
                    row.put("totalQuestions", rs.getInt("total_questions"));
                    row.put("percentage", rs.getInt("percentage"));
                    row.put("attemptDate", rs.getTimestamp("attempt_date"));
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
