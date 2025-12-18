<!DOCTYPE html>
<html>
<head>
    <title>Quiz - Online Exam</title>
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
            max-width: 600px;
            margin: 0 auto;
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
        }
        h2 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 30px;
            font-size: 32px;
        }
        .question-block {
            margin-bottom: 30px;
            padding: 20px;
            background: #f8fafc;
            border-left: 4px solid #0ea5e9;
            border-radius: 8px;
        }
        .question-block p {
            font-size: 16px;
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 15px;
        }
        .radio-group {
            margin-bottom: 10px;
        }
        input[type="radio"] {
            margin-right: 8px;
            cursor: pointer;
        }
        label {
            color: #64748b;
            cursor: pointer;
            font-size: 15px;
        }
        input[type="submit"] {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s;
            margin-top: 20px;
        }
        input[type="submit"]:hover {
            transform: translateY(-2px);
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
    %>
    <div class="container">
        <h2>Online Quiz</h2>
        <form action="quiz" method="post">
            <div class="question-block">
                <p>1. What is the capital of France?</p>
                <div class="radio-group">
                    <input type="radio" id="q1a" name="q1" value="Paris">
                    <label for="q1a">Paris</label>
                </div>
                <div class="radio-group">
                    <input type="radio" id="q1b" name="q1" value="London">
                    <label for="q1b">London</label>
                </div>
                <div class="radio-group">
                    <input type="radio" id="q1c" name="q1" value="Berlin">
                    <label for="q1c">Berlin</label>
                </div>
            </div>

            <div class="question-block">
                <p>2. What is 2 + 2?</p>
                <div class="radio-group">
                    <input type="radio" id="q2a" name="q2" value="3">
                    <label for="q2a">3</label>
                </div>
                <div class="radio-group">
                    <input type="radio" id="q2b" name="q2" value="4">
                    <label for="q2b">4</label>
                </div>
                <div class="radio-group">
                    <input type="radio" id="q2c" name="q2" value="5">
                    <label for="q2c">5</label>
                </div>
            </div>

            <input type="submit" value="Submit Quiz">
        </form>
    </div>
</body>
</html>
