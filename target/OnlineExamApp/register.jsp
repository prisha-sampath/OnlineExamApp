<!DOCTYPE html>
<html>
<head>
    <title>Register - Online Exam</title>
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
        }
        .container {
            background: white;
            padding: 50px;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 420px;
        }
        h2 {
            color: #2c3e50;
            text-align: center;
            margin-bottom: 30px;
            font-size: 32px;
            font-weight: 700;
        }
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 8px;
            font-size: 14px;
        }
        input[type="text"],
        input[type="password"],
        input[type="email"] {
            width: 100%;
            padding: 13px 15px;
            border: 2px solid #ecf0f1;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s ease;
            background-color: #f8f9fa;
        }
        input[type="text"]:focus,
        input[type="password"]:focus,
        input[type="email"]:focus {
            outline: none;
            border-color: #0ea5e9;
            background-color: white;
            box-shadow: 0 0 0 3px rgba(14, 165, 233, 0.1);
        }
        input[type="submit"] {
            width: 100%;
            padding: 13px;
            background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 10px;
        }
        input[type="submit"]:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(14, 165, 233, 0.3);
        }
        .message {
            padding: 12px 16px;
            margin-bottom: 20px;
            border-radius: 8px;
            text-align: center;
            font-weight: 500;
            font-size: 14px;
        }
        .error {
            background-color: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }
        .success {
            background-color: #efe;
            color: #3c3;
            border: 1px solid #cfc;
        }
        .link-section {
            text-align: center;
            margin-top: 20px;
            color: #555;
            font-size: 14px;
        }
        .link-section a {
            color: #1abc9c;
            text-decoration: none;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .link-section a:hover {
            color: #0e6251;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>User Registration</h2>
        <%
            String error = request.getParameter("error");
            String success = request.getParameter("success");
            if ("1".equals(error)) {
        %>
        <div class="message error">ERROR: Username already exists or registration failed</div>
        <%
            } else if ("1".equals(success)) {
        %>
        <div class="message success">SUCCESS: Registration successful! Please login.</div>
        <%
            }
        %>
        <form action="register" method="post">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
            </div>
            <input type="submit" value="Register">
        </form>
        <div class="link-section">
            Already have an account? <a href="login.jsp">Login here</a>
        </div>
    </div>
</body>
</html>
