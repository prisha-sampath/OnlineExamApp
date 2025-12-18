<!DOCTYPE html>
<html>
<head>
    <title>Select Language - Online Exam</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        .container {
            text-align: center;
            max-width: 900px;
        }
        h1 {
            color: white;
            font-size: 42px;
            margin-bottom: 15px;
            font-weight: 700;
            letter-spacing: 0.5px;
        }
        .subtitle {
            color: rgba(255, 255, 255, 0.9);
            font-size: 16px;
            margin-bottom: 50px;
        }
        .options {
            display: flex;
            gap: 40px;
            justify-content: center;
            flex-wrap: wrap;
        }
        .language-card {
            background: white;
            padding: 50px 35px;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 320px;
            cursor: pointer;
            transition: all 0.4s ease;
            text-decoration: none;
            color: #333;
            border-top: 5px solid #0ea5e9;
        }
        .language-card:hover {
            transform: translateY(-15px);
            box-shadow: 0 20px 45px rgba(14, 165, 233, 0.2);
            border-top-color: #0284c7;
        }
        .language-icon {
            font-size: 70px;
            margin-bottom: 20px;
            font-weight: 700;
            color: #0ea5e9;
        }
        .language-card h2 {
            font-size: 28px;
            margin-bottom: 12px;
            color: #2c3e50;
            font-weight: 700;
        }
        .language-card p {
            color: #666;
            font-size: 14px;
            margin-bottom: 25px;
            line-height: 1.6;
        }
        .btn {
            display: inline-block;
            padding: 13px 32px;
            background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .btn:hover {
            transform: scale(1.08);
        }
        .logout-link {
            margin-top: 40px;
        }
        .logout-link a {
            color: white;
            text-decoration: none;
            font-size: 14px;
            transition: all 0.3s ease;
            opacity: 0.9;
        }
        .logout-link a:hover {
            text-decoration: underline;
            opacity: 1;
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
        <h1>Programming Exam</h1>
        <p class="subtitle">Select your preferred language and challenge yourself</p>
        <div class="options">
            <a href="javaQuiz.jsp" class="language-card">
                <div class="language-icon">J</div>
                <h2>Java</h2>
                <p>Test your Java programming knowledge with our comprehensive quiz</p>
                <button class="btn">Start Java Quiz</button>
            </a>
            <a href="cppQuiz.jsp" class="language-card">
                <div class="language-icon">C++</div>
                <h2>C++</h2>
                <p>Challenge yourself with C++ programming questions</p>
                <button class="btn">Start C++ Quiz</button>
            </a>
            <a href="dsaQuiz.jsp" class="language-card">
                <div class="language-icon">DSA</div>
                <h2>Data Structures</h2>
                <p>Master data structures and algorithms concepts</p>
                <button class="btn">Start DSA Quiz</button>
            </a>
            <a href="pythonQuiz.jsp" class="language-card">
                <div class="language-icon">Py</div>
                <h2>Python</h2>
                <p>Test your Python programming and scripting skills</p>
                <button class="btn">Start Python Quiz</button>
            </a>
        </div>
        <div class="logout-link">
            <a href="history">View History</a>
            &nbsp;&nbsp;|&nbsp;&nbsp;
            <a href="result">Logout</a>
        </div>
    </div>
</body>
</html>