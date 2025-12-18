<!DOCTYPE html>
<html>
<head>
    <title>DSA Quiz - Online Exam</title>
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
            background: white;
            max-width: 800px;
            margin: 0 auto;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            border-bottom: 3px solid #7c3aed;
            padding-bottom: 20px;
        }
        h2 {
            color: #7c3aed;
            font-size: 32px;
            font-weight: 700;
        }
        .back-link {
            color: #7c3aed;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
            padding: 8px 16px;
            border-radius: 5px;
        }
        .back-link:hover {
            background: #f0f0f0;
            color: #6d28d9;
        }
        .question-block {
            margin-bottom: 30px;
            padding: 25px;
            background: #f8fafc;
            border-left: 5px solid #a687dd;
            border-radius: 8px;
        }
        .question-block p {
            font-size: 16px;
            color: #2c3e50;
            font-weight: 700;
            margin-bottom: 18px;
        }
        .radio-group {
            margin-bottom: 14px;
        }
        input[type="radio"] {
            margin-right: 10px;
            cursor: pointer;
            width: 18px;
            height: 18px;
            accent-color: #7c3aed;
        }
        label {
            color: #333;
            cursor: pointer;
            font-size: 15px;
        }
        .submit-btn {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #7c3aed 0%, #6d28d9 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 20px;
        }
        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(124, 58, 237, 0.3);
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
        
        String[] questions = (String[]) session.getAttribute("dsaQuestions");
        String[][] options = (String[][]) session.getAttribute("dsaOptions");
        
        if (questions == null || options == null) {
            response.sendRedirect("dsaQuiz");
            return;
        }
    %>
    <div class="container">
        <div class="header">
            <h2>DSA Quiz</h2>
            <a href="selectLanguage.jsp" class="back-link">Back</a>
        </div>
        <form action="dsaQuiz" method="post">
            <%
                for (int i = 0; i < questions.length; i++) {
            %>
            <div class="question-block">
                <p><%= (i + 1) %>. <%= questions[i] %></p>
                <%
                    for (int j = 0; j < options[i].length; j++) {
                        String optionText = options[i][j];
                        String radioId = "d" + (i + 1) + String.valueOf((char)('a' + j));
                %>
                <div class="radio-group">
                    <input type="radio" id="<%= radioId %>" name="d<%= (i + 1) %>" value="<%= optionText %>">
                    <label for="<%= radioId %>"><%= optionText %></label>
                </div>
                <%
                    }
                %>
            </div>
            <%
                }
            %>
            <button type="submit" class="submit-btn">Submit Quiz</button>
        </form>
    </div>
    <script>
        let timeRemaining = 900; // 15 minutes in seconds
        
        function updateTimer() {
            const minutes = Math.floor(timeRemaining / 60);
            const seconds = timeRemaining % 60;
            const timeString = String(minutes).padStart(2, '0') + ':' + String(seconds).padStart(2, '0');
            document.getElementById('timer').textContent = timeString;
            
            const timerContainer = document.querySelector('.timer-container');
            if (timeRemaining <= 300) { // Last 5 minutes
                timerContainer.classList.add('timer-warning');
            } else {
                timerContainer.classList.remove('timer-warning');
            }
            
            if (timeRemaining > 0) {
                timeRemaining--;
                setTimeout(updateTimer, 1000);
            } else {
                alert('Time is up! Submitting your quiz...');
                document.getElementById('quizForm').submit();
            }
        }
        
        window.addEventListener('load', updateTimer);
    </script>
</body>
</html>
