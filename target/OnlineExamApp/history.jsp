<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    List<Map<String, Object>> userHistory = (List<Map<String, Object>>) session.getAttribute("userHistory");
    if (userHistory == null) {
        response.sendRedirect("history");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Quiz History - Online Exam</title>
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
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .header h1 {
            color: #2c3e50;
            font-size: 28px;
        }
        .header-stats {
            display: flex;
            gap: 30px;
        }
        .stat-box {
            text-align: center;
        }
        .stat-value {
            font-size: 28px;
            font-weight: bold;
            color: #0ea5e9;
        }
        .stat-label {
            color: #7f8c8d;
            font-size: 14px;
            margin-top: 5px;
        }
        .back-btn {
            background: #2c3e50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 14px;
            transition: background 0.3s;
        }
        .back-btn:hover {
            background: #34495e;
        }
        .history-container {
            background: white;
            border-radius: 16px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .history-header {
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            color: white;
            padding: 20px 30px;
            font-size: 16px;
            font-weight: 600;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th {
            background: #ecf0f1;
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: #2c3e50;
            border-bottom: 2px solid #bdc3c7;
        }
        td {
            padding: 15px;
            border-bottom: 1px solid #ecf0f1;
        }
        tr:hover {
            background: #f8f9fa;
        }
        .quiz-name {
            font-weight: 600;
            color: #2c3e50;
        }
        .score-cell {
            font-weight: bold;
            font-size: 16px;
        }
        .score-cell.excellent {
            color: #27ae60;
        }
        .score-cell.good {
            color: #f39c12;
        }
        .score-cell.average {
            color: #e74c3c;
        }
        .percentage {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }
        .percentage.excellent {
            background: #d5f4e6;
            color: #27ae60;
        }
        .percentage.good {
            background: #fdebd0;
            color: #f39c12;
        }
        .percentage.average {
            background: #fadbd8;
            color: #e74c3c;
        }
        .attempt-date {
            color: #7f8c8d;
            font-size: 14px;
        }
        .no-history {
            text-align: center;
            padding: 60px 30px;
            color: #95a5a6;
        }
        .no-history-icon {
            font-size: 48px;
            margin-bottom: 20px;
        }
        .quiz-type-java {
            color: #0d47a1;
            font-weight: 600;
        }
        .quiz-type-cpp {
            color: #d32f2f;
            font-weight: 600;
        }
        .quiz-type-dsa {
            color: #7c3aed;
            font-weight: 600;
        }
        .quiz-type-python {
            color: #f97316;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div>
                <h1>Quiz History</h1>
            </div>
            <button class="back-btn" onclick="location.href='selectLanguage.jsp'">Back to Quizzes</button>
        </div>

        <% if (userHistory != null && !userHistory.isEmpty()) { %>
            <div style="margin-bottom: 20px;">
                <div class="history-container" style="padding: 0;">
                    <div class="history-header">
                        Your Quiz Attempts: <%= userHistory.size() %>
                    </div>
                    <table>
                        <thead>
                            <tr>
                                <th>Quiz Type</th>
                                <th>Score</th>
                                <th>Percentage</th>
                                <th>Date & Time</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% 
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                                int totalAttempts = userHistory.size();
                                int totalScore = 0;
                                
                                for (Map<String, Object> result : userHistory) {
                                    String quizType = (String) result.get("quizType");
                                    int score = (Integer) result.get("score");
                                    int totalQuestions = (Integer) result.get("totalQuestions");
                                    int percentage = (Integer) result.get("percentage");
                                    java.sql.Timestamp attemptDate = (java.sql.Timestamp) result.get("attemptDate");
                                    
                                    totalScore += score;
                                    
                                    String quizClass = "";
                                    if (quizType.equalsIgnoreCase("Java")) {
                                        quizClass = "quiz-type-java";
                                    } else if (quizType.equalsIgnoreCase("C++")) {
                                        quizClass = "quiz-type-cpp";
                                    } else if (quizType.equalsIgnoreCase("DSA")) {
                                        quizClass = "quiz-type-dsa";
                                    } else if (quizType.equalsIgnoreCase("Python")) {
                                        quizClass = "quiz-type-python";
                                    }
                                    
                                    String percentageClass = "";
                                    String scoreClass = "";
                                    if (percentage >= 80) {
                                        percentageClass = "excellent";
                                        scoreClass = "excellent";
                                    } else if (percentage >= 60) {
                                        percentageClass = "good";
                                        scoreClass = "good";
                                    } else {
                                        percentageClass = "average";
                                        scoreClass = "average";
                                    }
                            %>
                            <tr>
                                <td><span class="<%= quizClass %>"><%= quizType %></span></td>
                                <td class="score-cell <%= scoreClass %>"><%= score %> / <%= totalQuestions %></td>
                                <td><span class="percentage <%= percentageClass %>"><%= percentage %>%</span></td>
                                <td class="attempt-date"><%= dateFormat.format(new Date(attemptDate.getTime())) %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <div style="background: white; padding: 30px; border-radius: 16px; box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1); margin-top: 20px;">
                <h3 style="color: #2c3e50; margin-bottom: 20px;">Summary Statistics</h3>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px;">
                    <div class="stat-box">
                        <div class="stat-value"><%= totalAttempts %></div>
                        <div class="stat-label">Total Attempts</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-value"><%= String.format("%.1f", (double) totalScore / totalAttempts) %></div>
                        <div class="stat-label">Average Score</div>
                    </div>
                    <div class="stat-box">
                        <div class="stat-value"><%= String.format("%.1f", ((double) totalScore / (totalAttempts * 10)) * 100) %>%</div>
                        <div class="stat-label">Overall Percentage</div>
                    </div>
                </div>
            </div>
        <% } else { %>
            <div class="history-container">
                <div class="no-history">
                    <div class="no-history-icon">📋</div>
                    <h3>No Quiz History Yet</h3>
                    <p style="margin-top: 10px;">Start taking quizzes to see your history here!</p>
                    <button class="back-btn" onclick="location.href='selectLanguage.jsp'" style="margin-top: 20px;">Take a Quiz</button>
                </div>
            </div>
        <% } %>
    </div>
</body>
</html>
