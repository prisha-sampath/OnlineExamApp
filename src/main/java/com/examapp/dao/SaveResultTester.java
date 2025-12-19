package com.examapp.dao;

import java.util.List;
import java.util.Map;

public class SaveResultTester {
    public static void main(String[] args) {
        if (System.getenv("NEON_DB_URL") == null) {
            System.err.println("Set NEON_DB_URL/NEON_DB_USER/NEON_DB_PASSWORD in the environment first.");
            System.exit(1);
        }

        String username = "testuser";
        QuizResultsDao.saveQuizResult(username, "Java", 8, 10);
        List<Map<String, Object>> results = QuizResultsDao.getResultsByUsername(username);
        System.out.println("Results for " + username + ": " + results.size());
        if (!results.isEmpty()) {
            System.out.println(results.get(0));
        }
    }
}