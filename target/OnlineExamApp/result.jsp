<!DOCTYPE html>
<html>
<head>
    <title>Result - Online Exam</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #418193 0%, #418193 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .main-container {
            max-width: 1000px;
            margin: 0 auto;
        }
        .score-container {
            background: white;
            padding: 50px;
            border-radius: 16px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
            text-align: center;
            margin-bottom: 30px;
        }
        h2 {
            color: #418193;
            font-size: 32px;
            margin-bottom: 30px;
            font-weight: 700;
        }
        .score-box {
            background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
            color: white;
            padding: 35px;
            border-radius: 10px;
            margin-bottom: 30px;
            font-size: 24px;
            font-weight: 600;
        }
        .score-value {
            font-size: 48px;
            font-weight: bold;
            margin: 10px 0;
        }
        .buttons {
            text-align: center;
            margin-top: 30px;
        }
        a {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: all 0.3s ease;
            margin: 0 10px;
        }
        a:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(14, 165, 233, 0.3);
        }
        .another-quiz {
            background: linear-gradient(135deg, #10b981 0%, #059669 100%);
        }
        .another-quiz:hover {  
            box-shadow: 0 10px 20px rgba(16, 185, 129, 0.3);
        }
        
        .answer-key-container {
            background: white;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
        }
        
        .answer-key-title {
            color: #2c3e50;
            font-size: 28px;
            margin-bottom: 30px;
            font-weight: 700;
            border-bottom: 3px solid #0ea5e9;
            padding-bottom: 15px;
        }
        
        .question-review {
            background: #f8fafc;
            padding: 20px;
            margin-bottom: 20px;
            border-left: 5px solid #ccc;
            border-radius: 8px;
        }
        
        .question-review.correct {
            border-left-color: #10b981;
            background: #f0fdf4;
        }
        
        .question-review.incorrect {
            border-left-color: #ef4444;
            background: #fef2f2;
        }
        
        .question-number {
            font-weight: 700;
            color: #333;
            margin-bottom: 12px;
            font-size: 16px;
        }
        
        .answer-row {
            margin: 10px 0;
            padding: 10px;
            background: white;
            border-radius: 5px;
        }
        
        .answer-row label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            font-size: 14px;
        }
        
        .user-answer {
            color: #ef4444;
        }
        
        .correct-answer {
            color: #10b981;
        }
        
        .status-icon {
            display: inline-block;
            width: 25px;
            height: 25px;
            border-radius: 50%;
            margin-right: 10px;
            color: white;
            text-align: center;
            line-height: 25px;
            font-weight: bold;
        }
        
        .status-correct {
            background: #10b981;
        }
        
        .status-incorrect {
            background: #ef4444;
        }
    </style>
</head>
<body>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        Integer score = (Integer) session.getAttribute("score");
        String quizType = (String) session.getAttribute("quizType");
        java.util.Map<Integer, java.util.Map<String, Object>> answerKey = (java.util.Map<Integer, java.util.Map<String, Object>>) session.getAttribute("answerKey");
        
        if (score == null) {
            response.sendRedirect("selectLanguage.jsp");
            return;
        }
    %>
    
    <div class="main-container">
        <div class="score-container">
            <h2>Quiz Completed!</h2>
            <div class="score-box">
                <p><%= quizType %> Quiz Result</p>
                <div class="score-value"><%= score %> / 10</div>
                <p><%= score >= 8 ? "Excellent! Score: " + score + "/10" : score >= 6 ? "Good! Score: " + score + "/10" : score >= 4 ? "Fair! Score: " + score + "/10" : "Try Again! Score: " + score + "/10" %></p>
            </div>
            <div class="buttons">
                <a href="clearQuiz.jsp" class="another-quiz">Take Another Quiz</a>
                <a href="result">Logout</a>
            </div>
        </div>
        
        <% if (answerKey != null && !answerKey.isEmpty()) { %>
        <div class="answer-key-container">
            <div class="answer-key-title">Answer Key - Review Your Answers</div>
            
            <% 
                for (int i = 1; i <= 10; i++) {
                    java.util.Map<String, Object> data = answerKey.get(i);
                    if (data != null) {
                        String question = (String) data.get("question");
                        String userAnswer = (String) data.get("userAnswer");
                        String correctAnswer = (String) data.get("correctAnswer");
                        Boolean isCorrect = (Boolean) data.get("isCorrect");
                        String reviewClass = isCorrect ? "question-review correct" : "question-review incorrect";
                        String statusClass = isCorrect ? "status-icon status-correct" : "status-icon status-incorrect";
                        String statusText = isCorrect ? "CORRECT" : "WRONG";
            %>
            <div class="<%= reviewClass %>">
                <div class="question-number">
                    <span class="<%= statusClass %>"><%= statusText %></span>
                    <%= question %>
                </div>
                <div class="answer-row">
                    <label class="user-answer">Your Answer: <strong><%= userAnswer %></strong></label>
                </div>
                <div class="answer-row">
                    <label class="correct-answer">Correct Answer: <strong><%= correctAnswer %></strong></label>
                </div>
            </div>
            <% 
                    }
                }
            %>
        </div>
        <% } %>
    </div>
</body>
</html>
