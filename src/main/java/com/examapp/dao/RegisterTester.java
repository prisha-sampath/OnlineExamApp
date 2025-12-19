package com.examapp.dao;

public class RegisterTester {
    public static void main(String[] args) {
        if (System.getenv("NEON_DB_URL") == null) {
            System.err.println("Set NEON_DB_URL/NEON_DB_USER/NEON_DB_PASSWORD in the environment first.");
            System.exit(1);
        }

        UserDao dao = new UserDao();
        String username = "testuser";
        String password = "test123";

        boolean ok = dao.register(username, password);
        System.out.println("Register user '" + username + "' => " + ok);

        boolean valid = dao.validate(username, password);
        System.out.println("Validate user '" + username + "' credentials => " + valid);
    }
}