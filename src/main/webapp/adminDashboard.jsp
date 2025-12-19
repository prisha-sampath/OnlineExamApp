<%@ page import="com.examapp.dao.QuizResultsDao" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Timestamp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard - Online Exam</title>
    <meta http-equiv="refresh" content="30">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        .header {
            background: white;
            padding: 30px;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header h1 {
            color: #2c3e50;
            font-size: 32px;
            font-weight: 700;
        }
        .header-info {
            display: flex;
            gap: 20px;
            align-items: center;
        }
        .admin-name {
            color: #666;
            font-size: 14px;
        }
        .logout-btn {
            padding: 10px 20px;
            background: #d32f2f;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .logout-btn:hover {
            background: #b71c1c;
            transform: scale(1.05);
        }
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
            border-left: 5px solid #0ea5e9;
        }
        .stat-label {
            color: #7f8c8d;
            font-size: 14px;
            margin-bottom: 8px;
        }
        .stat-value {
            color: #2c3e50;
            font-size: 36px;
            font-weight: 700;
        }
        .results-container {
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .results-header {
            background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
            color: white;
            padding: 25px;
            font-size: 20px;
            font-weight: 700;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        thead {
            background: #f5f5f5;
            border-bottom: 2px solid #e0e0e0;
        }
        th {
            padding: 16px;
            text-align: left;
            color: #333;
            font-weight: 700;
            font-size: 14px;
        }
        td {
            padding: 16px;
            border-bottom: 1px solid #e0e0e0;
            color: #666;
            font-size: 14px;
        }
        tr:hover {
            background: #f9f9f9;
        }
        .score-cell {
            font-weight: 700;
            color: #2c3e50;
        }
        .score-high {
            background: #f0fdf4;
            padding: 4px 8px;
            border-radius: 4px;
            color: #059669;
        }
        .score-medium {
            background: #fef3c7;
            padding: 4px 8px;
            border-radius: 4px;
            color: #b45309;
        }
        .score-low {
            background: #fee2e2;
            padding: 4px 8px;
            border-radius: 4px;
            color: #dc2626;
        }
        .quiz-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 700;
        }
        .java-badge {
            background: #dbeafe;
            color: #0284c7;
        }
        .cpp-badge {
            background: #fecaca;
            color: #991b1b;
        }
        .dsa-badge {
            background: #e9d5ff;
            color: #6b21a8;
        }
        .python-badge {
            background: #fed7aa;
            color: #92400e;
        }
        .no-results {
            text-align: center;
            padding: 50px;
            color: #999;
        }
        .pagination {
            display: flex;
            justify-content: center;
            gap: 10px;
            padding: 20px;
            background: white;
            border-top: 1px solid #e0e0e0;
        }
    </style>
</head>
<body>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (session.getAttribute("admin") == null) {
            response.sendRedirect("adminLogin.jsp");
            return;
        }
        
        List<Map<String, Object>> results = new java.util.ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            results = QuizResultsDao.getAllResults();
        } catch (Exception e) {
            // Show friendly error instead of letting the page throw a NullPointerException
            out.println("<div style='margin:20px;padding:20px;background:#fee2e2;border-left:5px solid #ef4444;'><strong>Database error:</strong> " + e.getMessage() + "<br>Check that NEON_DB_URL, NEON_DB_USER and NEON_DB_PASSWORD are set and the DB is reachable. <a href=\"diagnostic.jsp\">Run diagnostic</a></div>");
        }
    %>
    
    <div class="container">
        <div class="header">
            <h1>Admin Dashboard</h1>
            <div class="header-info">
                <div class="admin-name">Logged in as: <strong><%= session.getAttribute("adminName") %></strong></div>
                <a href="adminLogout.jsp" class="logout-btn">Logout</a>
            </div>
        </div>
        
        <div class="stats">
            <div class="stat-card">
                <div class="stat-label">Total Quiz Attempts</div>
                <div class="stat-value"><%= results.size() %></div>
            </div>
            <%
                java.util.Set<String> uniqueUsers = new java.util.HashSet<>();
                int totalScore = 0;
                for (Map<String, Object> result : results) {
                    uniqueUsers.add((String) result.get("username"));
                    totalScore += (int) result.get("score");
                }
            %>
            <div class="stat-card">
                <div class="stat-label">Unique Candidates</div>
                <div class="stat-value"><%= uniqueUsers.size() %></div>
            </div>
            <div class="stat-card">
                <div class="stat-label">Average Score</div>
                <div class="stat-value"><%= results.size() > 0 ? (totalScore / results.size()) : 0 %> / 10</div>
            </div>
        </div>
        
        <div class="results-container">
            <div class="results-header">Quiz Results
                <div style="float: right; width: 300px;">
                    <input type="text" id="searchInput" placeholder="Search by username..." style="width: 100%; padding: 8px 12px; border: 2px solid white; border-radius: 6px; font-size: 14px; background: rgba(255,255,255,0.9);">
                </div>
            </div>
            <% if (results.isEmpty()) { %>
            <div class="no-results">
                <p><strong>No quiz results found yet</strong></p>
                <p>To see student marks here:</p>
                <ol style="text-align: left; display: inline-block;">
                    <li>Register a student: <a href="register.jsp">register.jsp</a></li>
                    <li>Login as the student</li>
                    <li>Complete and submit a quiz</li>
                    <li>Results will appear here automatically</li>
                </ol>
                <p style="margin-top: 20px;">
                    <small>📋 <a href="diagnostic.jsp" style="color: #1a237e;">Check database connection</a></small>
                </p>
            </div>
            <% } else { %>
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Quiz Type</th>
                        <th>Marks</th>
                        <th>Percentage</th>
                        <th>Status</th>
                        <th>Attempt Date</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Map<String, Object> result : results) {
                        int score = (int) result.get("score");
                        int total = (int) result.get("totalQuestions");
                        int percentage = (Integer) result.get("percentage");
                        String scoreClass = percentage >= 80 ? "score-high" : percentage >= 60 ? "score-medium" : "score-low";
                        String scoreStatus = percentage >= 80 ? "Excellent" : percentage >= 60 ? "Good" : "Needs Improvement";
                        String quizType = (String) result.get("quizType");
                        String badgeClass = "java-badge";
                        if ("C++".equals(quizType)) badgeClass = "cpp-badge";
                        else if ("DSA".equals(quizType)) badgeClass = "dsa-badge";
                        else if ("Python".equals(quizType)) badgeClass = "python-badge";
                    %>
                    <tr>
                        <td><strong><%= result.get("username") %></strong></td>
                        <td><span class="quiz-badge <%= badgeClass %>"><%= quizType %></span></td>
                        <td class="score-cell"><span class="<%= scoreClass %>"><strong><%= score %> / <%= total %></strong></span></td>
                        <td><strong><%= percentage %>%</strong></td>
                        <td><span class="<%= scoreClass %>"><strong><%= scoreStatus %></strong></span></td>
                        <td><%= sdf.format((Timestamp) result.get("attemptDate")) %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
            <% } %>
        </div>
    </div>
    
    <script>
        document.getElementById('searchInput').addEventListener('keyup', function() {
            const searchValue = this.value.toLowerCase();
            const tableRows = document.querySelectorAll('tbody tr');
            let visibleCount = 0;
            
            tableRows.forEach(row => {
                const username = row.cells[0].textContent.toLowerCase();
                if (username.includes(searchValue)) {
                    row.style.display = '';
                    visibleCount++;
                } else {
                    row.style.display = 'none';
                }
            });
            
            // Show "No results" message if all rows are hidden
            const tbody = document.querySelector('tbody');
            if (visibleCount === 0 && searchValue.length > 0) {
                if (!document.getElementById('noResultsMsg')) {
                    const msg = document.createElement('tr');
                    msg.id = 'noResultsMsg';
                    msg.innerHTML = '<td colspan="6" style="text-align: center; padding: 30px; color: #999;"><strong>No results found for: "' + this.value + '"</strong></td>';
                    tbody.appendChild(msg);
                }
            } else if (document.getElementById('noResultsMsg')) {
                document.getElementById('noResultsMsg').remove();
            }
        });
    </script>
</body>
</html>
