<%@ page import="com.examapp.dao.DBConnection" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Database Diagnostic</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            min-height: 100vh;
            padding: 40px 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            border-radius: 16px;
            padding: 40px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 30px;
            border-bottom: 3px solid #0ea5e9;
            padding-bottom: 15px;
        }
        .status {
            margin: 20px 0;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid;
        }
        .success {
            background: #f0fdf4;
            border-left-color: #10b981;
            color: #065f46;
        }
        .error {
            background: #fef2f2;
            border-left-color: #ef4444;
            color: #7f1d1d;
        }
        .warning {
            background: #fffbeb;
            border-left-color: #f59e0b;
            color: #92400e;
        }
        code {
            background: #f8fafc;
            padding: 10px;
            border-radius: 4px;
            display: block;
            margin: 10px 0;
            overflow-x: auto;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        th, td {
            padding: 12px;
            border: 1px solid #e5e7eb;
            text-align: left;
        }
        th {
            background: #2c3e50;
            color: white;
        }
        .sql-command {
            background: #f8fafc;
            padding: 15px;
            border-radius: 8px;
            margin: 15px 0;
            border-left: 4px solid #0ea5e9;
        }
        .button {
            background: #0ea5e9;
            color: white;
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            margin: 10px 5px 10px 0;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
        }
        .button:hover {
            background: #0284c7;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Database Diagnostic Check</h1>
        
        <%
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            
            try {
                conn = DBConnection.getConnection();
                if (conn != null && !conn.isClosed()) {
        %>
        
        <div class="status success">
            <strong>✅ Database Connection: SUCCESS</strong><br>
            Connected to: online_exam
        </div>
        
        <%
                    // Check if quiz_results table exists
                    DatabaseMetaData dbmd = conn.getMetaData();
                    rs = dbmd.getTables(null, null, "quiz_results", null);
                    
                    if (rs.next()) {
        %>
        
        <div class="status success">
            <strong>✅ Table 'quiz_results': EXISTS</strong>
        </div>
        
        <%
                        // Get table structure
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("DESCRIBE quiz_results");
        %>
        
        <h3>Table Structure:</h3>
        <table>
            <thead>
                <tr>
                    <th>Column</th>
                    <th>Type</th>
                    <th>Null</th>
                    <th>Key</th>
                    <th>Default</th>
                </tr>
            </thead>
            <tbody>
        <%
                        while (rs.next()) {
        %>
                <tr>
                    <td><%= rs.getString(1) %></td>
                    <td><%= rs.getString(2) %></td>
                    <td><%= rs.getString(3) %></td>
                    <td><%= rs.getString(4) %></td>
                    <td><%= rs.getString(5) %></td>
                </tr>
        <%
                        }
        %>
            </tbody>
        </table>
        
        <%
                        // Check row count
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("SELECT COUNT(*) as count FROM quiz_results");
                        int rowCount = 0;
                        if (rs.next()) {
                            rowCount = rs.getInt("count");
                        }
        %>
        
        <div class="status <%= rowCount > 0 ? "success" : "warning" %>">
            <strong><%= rowCount > 0 ? "✅" : "⚠️" %> Records in quiz_results: <%= rowCount %></strong><br>
            <%= rowCount == 0 ? "No quiz results yet. Students need to complete quizzes." : "Quiz results found!" %>
        </div>
        
        <%
                        if (rowCount > 0) {
        %>
        
        <h3>Sample Records:</h3>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Quiz Type</th>
                    <th>Score</th>
                    <th>Percentage</th>
                    <th>Date</th>
                </tr>
            </thead>
            <tbody>
        <%
                            stmt = conn.createStatement();
                            rs = stmt.executeQuery("SELECT id, username, quiz_type, score, percentage, attempt_date FROM quiz_results ORDER BY attempt_date DESC LIMIT 10");
                            while (rs.next()) {
        %>
                <tr>
                    <td><%= rs.getInt("id") %></td>
                    <td><%= rs.getString("username") %></td>
                    <td><%= rs.getString("quiz_type") %></td>
                    <td><%= rs.getInt("score") %>/10</td>
                    <td><%= rs.getInt("percentage") %>%</td>
                    <td><%= rs.getTimestamp("attempt_date") %></td>
                </tr>
        <%
                            }
        %>
            </tbody>
        </table>
        
        <%
                        } else {
        %>
        
        <h3>⚠️ No Quiz Results Found</h3>
        <p>To test the dashboard, follow these steps:</p>
        <ol>
            <li>Go to: http://localhost:8080/OnlineExamApp/login.jsp</li>
            <li>Register a new user (e.g., testuser / test123)</li>
            <li>Login with your credentials</li>
            <li>Select a quiz language (Java, C++, DSA, or Python)</li>
            <li>Answer all 10 questions and submit</li>
            <li>Return to admin dashboard to see your score</li>
        </ol>
        
        <%
                        }
        %>
        
        <%
                    } else {
        %>
        
        <div class="status error">
            <strong>Table 'quiz_results': NOT FOUND</strong><br>
            The table needs to be created. Run the SQL command below:
        </div>
        
        <h3>Create Table SQL:</h3>
        <div class="sql-command">
            <code>
CREATE TABLE IF NOT EXISTS quiz_results (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    quiz_type VARCHAR(20) NOT NULL,
    score INT NOT NULL,
    total_questions INT NOT NULL,
    percentage INT NOT NULL,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    INDEX idx_username (username),
    INDEX idx_quiz_type (quiz_type),
    INDEX idx_attempt_date (attempt_date)
);
            </code>
        </div>
        
        <p><strong>Steps to create table:</strong></p>
        <ol>
            <li>Open MySQL Command Prompt or MySQL Workbench</li>
            <li>Connect to online_exam database: <code>USE online_exam;</code></li>
            <li>Copy and paste the SQL command above</li>
            <li>Press Enter to execute</li>
            <li>Refresh this page to verify</li>
        </ol>
        
        <%
                    }
        %>
        
        <%
                } else {
        %>
        
        <div class="status error">
            <strong>Database Connection: FAILED</strong><br>
            Cannot connect to MySQL database.
        </div>
        
        <h3>Troubleshooting:</h3>
        <p><strong>Check the following:</strong></p>
        <ol>
            <li><strong>MySQL is running?</strong> Start MySQL service</li>
            <li><strong>Database exists?</strong> Run: <code>CREATE DATABASE IF NOT EXISTS online_exam;</code></li>
            <li><strong>Connection credentials correct?</strong><br>
                File: DBConnection.java<br>
                Current: host=localhost, port=3306, user=root, password=Prishasampath<br>
                Database: online_exam
            </li>
            <li><strong>MySQL JDBC Driver installed?</strong> Check pom.xml for mysql-connector-java dependency</li>
        </ol>
        
        <h3>DBConnection.java Configuration:</h3>
        <div class="sql-command">
            <code>
URL: jdbc:mysql://localhost:3306/online_exam
USER: root
PASSWORD: Prishasampath
            </code>
        </div>
        
        <%
                }
            } catch (Exception e) {
        %>
        
        <div class="status error">
            <strong>Error: <%= e.getMessage() %></strong><br>
            <strong>Cause:</strong> <%= e.getCause() %>
        </div>
        
        <h3>Error Details:</h3>
        <code style="color: red;">
<%
                e.printStackTrace(new java.io.PrintWriter(out));
%>
        </code>
        
        <%
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        %>
        
        <h3>Quick Links:</h3>
        <a href="adminLogin.jsp" class="button">← Back to Admin Login</a>
        <a href="../../" class="button">← Back to Home</a>
        
        <hr style="margin: 30px 0; border: none; border-top: 2px solid #ddd;">
        
        <h3>📋 Next Steps:</h3>
        <ul>
            <li>✅ Verify database connection above</li>
            <li>✅ Create quiz_results table if needed</li>
            <li>✅ Have a user complete a quiz</li>
            <li>✅ Admin login to see marks in dashboard</li>
        </ul>
    </div>
</body>
</html>
