<!DOCTYPE html>
<html>
<head>
    <title>Admin Login - Online Exam</title>
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
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .container {
            background: white;
            padding: 60px 40px;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 420px;
        }
        .header {
            text-align: center;
            margin-bottom: 40px;
        }
        .admin-icon {
            font-size: 60px;
            color: #0ea5e9;
            margin-bottom: 15px;
        }
        h1 {
            color: #2c3e50;
            font-size: 32px;
            margin-bottom: 10px;
            font-weight: 700;
        }
        .subtitle {
            color: #7f8c8d;
            font-size: 14px;
        }
        .form-group {
            margin-bottom: 25px;
        }
        label {
            display: block;
            color: #2c3e50;
            font-weight: 600;
            margin-bottom: 8px;
            font-size: 14px;
        }
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 14px 16px;
            border: 2px solid #ecf0f1;
            border-radius: 8px;
            font-size: 15px;
            transition: all 0.3s ease;
        }
        input[type="text"]:focus,
        input[type="password"]:focus {
            outline: none;
            border-color: #0ea5e9;
            box-shadow: 0 0 0 3px rgba(14, 165, 233, 0.1);
        }
        .btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(26, 35, 126, 0.3);
        }
        .message.error {
            background: #ffebee;
            color: #d32f2f;
            padding: 12px 16px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #d32f2f;
            font-size: 14px;
        }
        .back-link {
            text-align: center;
            margin-top: 20px;
        }
        .back-link a {
            color: #1a237e;
            text-decoration: none;
            font-size: 14px;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <%
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        String error = request.getParameter("error");
    %>
    <div class="container">
        <div class="header">
            <div class="admin-icon">ADMIN</div>
            <h1>Admin Panel</h1>
            <p class="subtitle">View candidate marks and quiz results</p>
        </div>
        
        <% if (error != null) { %>
        <div class="message error">ERROR: <%= error %></div>
        <% } %>
        
        <form method="post" action="adminLogin">
            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required placeholder="Enter admin username">
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required placeholder="Enter admin password">
            </div>
            
            <button type="submit" class="btn">Login as Admin</button>
        </form>
        
        <div class="back-link">
            <a href="login.jsp">Back to User Login</a>
        </div>
    </div>
</body>
</html>
