# Online Exam App - Setup & Deployment Guide

## Project Status: ✅ BUILD SUCCESSFUL

The project has been built successfully with all new admin dashboard features integrated.

## Build Information
- **Build Tool**: Maven 3.9.x
- **Java Version**: Java 11
- **Frameworks**: Jakarta Servlet 6.0.0, Jakarta JSP 4.0.0
- **Database**: MySQL 8.0
- **WAR File**: OnlineExamApp.war (located in target/ directory)

## Database Setup (REQUIRED BEFORE DEPLOYMENT)

### 1. Create Online Exam Database
```sql
CREATE DATABASE IF NOT EXISTS online_exam CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE online_exam;
```

### 2. Create Users Table (if not exists)
```sql
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. Create Quiz Results Table (NEW - REQUIRED FOR ADMIN DASHBOARD)
```sql
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
```

### 4. Test Database Connection
```sql
-- Verify tables exist
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'online_exam';

-- Should show: users, quiz_results
```

## Configuration Files

### DBConnection.java
Update the database connection parameters if needed:
```java
Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_exam", "root", "root");
```

Modify the following if your MySQL setup is different:
- **Host**: localhost (change if MySQL is on different server)
- **Port**: 3306 (change if MySQL runs on different port)
- **Username**: root (change to your MySQL username)
- **Password**: root (change to your MySQL password)

## Application Features

### 1. User Registration & Login
- Register new user account
- Login with credentials
- Session-based authentication

### 2. Quiz System (4 Languages)
- **Java Quiz** - Indigo theme (#1e5aa8)
- **C++ Quiz** - Red theme (#d32f2f)
- **DSA Quiz** - Purple theme (#9c27b0)
- **Python Quiz** - Orange theme (#ff6f00)

Each quiz contains:
- 10 multiple-choice questions
- Questions shuffled on each quiz attempt
- Immediate scoring after submission
- Answer key showing correct/incorrect answers
- Results automatically saved to database

### 3. Admin Dashboard (NEW)
- **Admin Login**: Separate authentication for administrators
- **Default Credentials**: 
  - Username: `admin`
  - Password: `admin123`
- **Dashboard Features**:
  - Total quiz attempts count
  - Unique candidates count
  - Average score percentage
  - Comprehensive results table with all candidate attempts
  - Color-coded scores (Green: 80%+, Orange: 60-80%, Red: <60%)
  - Quiz type badges with language-specific colors
  - Sortable attempt timestamps
  - Admin logout functionality

## Application URL Mapping

### User Pages
| Page | URL | Purpose |
|------|-----|---------|
| Registration | `/OnlineExamApp/register.jsp` | Create new account |
| Login | `/OnlineExamApp/login.jsp` | User login |
| Language Selection | `/OnlineExamApp/quiz.jsp` | Choose quiz language |
| Quiz Pages | `/OnlineExamApp/[language]Quiz.jsp` | Take quiz |
| Results | `/OnlineExamApp/result.jsp` | View quiz results and answer key |

### Admin Pages
| Page | URL | Purpose |
|------|-----|---------|
| Admin Login | `/OnlineExamApp/adminLogin.jsp` | Admin authentication |
| Admin Dashboard | `/OnlineExamApp/adminDashboard.jsp` | View all candidate marks |
| Admin Logout | `/OnlineExamApp/adminLogout.jsp` | Logout and clear session |

### Servlet Mappings
| Servlet | Pattern | Purpose |
|---------|---------|---------|
| LoginServlet | `/login` | Process user login |
| RegisterServlet | `/register` | Process user registration |
| JavaQuizServlet | `/javaQuiz` | Process Java quiz, save results |
| CppQuizServlet | `/cppQuiz` | Process C++ quiz, save results |
| DsaQuizServlet | `/dsaQuiz` | Process DSA quiz, save results |
| PythonQuizServlet | `/pythonQuiz` | Process Python quiz, save results |
| AdminLoginServlet | `/adminLogin` | Process admin authentication |
| AdminDashboardServlet | `/adminDashboard` | Route to admin dashboard |

## Deployment Steps

### 1. Deploy to Apache Tomcat
```bash
# Copy WAR file to Tomcat webapps directory
cp target/OnlineExamApp.war $CATALINA_HOME/webapps/

# Start Tomcat
$CATALINA_HOME/bin/startup.sh (Linux/Mac)
# or
$CATALINA_HOME/bin/startup.bat (Windows)
```

### 2. Access Application
- **Home**: http://localhost:8080/OnlineExamApp/
- **User Login**: http://localhost:8080/OnlineExamApp/login.jsp
- **Admin Login**: http://localhost:8080/OnlineExamApp/adminLogin.jsp

## Testing Workflow

### User Testing
1. Register new account (e.g., username: testuser, password: test123)
2. Login with credentials
3. Select language and take quiz
4. View results and answer key
5. Click "Take Another Quiz" to shuffle questions
6. Verify new questions are different from first attempt

### Admin Testing
1. Navigate to Admin Login (link on login.jsp)
2. Login with credentials: admin / admin123
3. View admin dashboard with statistics
4. Verify quiz results from user attempts appear in table
5. Check color-coded scores display correctly
6. Test admin logout functionality

## Database Verification

After a user completes a quiz, verify data is stored:
```sql
SELECT * FROM quiz_results ORDER BY attempt_date DESC;
```

Expected output:
```
| id | username  | quiz_type | score | total_questions | percentage | attempt_date        |
|----|-----------|-----------|-------|-----------------|------------|-------------------|
| 1  | testuser  | Java      | 8     | 10              | 80         | 2024-01-15 10:30:45 |
```

## Troubleshooting

### Issue: "Cannot access database"
**Solution**: 
- Verify MySQL is running
- Check database name is "online_exam"
- Verify username/password in DBConnection.java
- Check MySQL port is 3306

### Issue: "Servlets not found (404)"
**Solution**:
- Verify web.xml has correct servlet mappings
- Ensure WAR file is deployed to Tomcat webapps
- Restart Tomcat server
- Check URL patterns match servlet-mapping entries

### Issue: "Admin login always fails"
**Solution**:
- Verify you're entering: admin / admin123
- Check browser cookies/cache
- Verify AdminLoginServlet is mapped to /adminLogin

### Issue: "Quiz results not appearing in admin dashboard"
**Solution**:
- Verify quiz_results table exists in database
- Check that users have completed a quiz after deployment
- Verify QuizResultsDao.saveQuizResult() is being called (check server logs)
- Ensure database user has INSERT privileges

## Project Structure
```
OnlineExamApp/
├── src/main/java/com/examapp/
│   ├── dao/
│   │   ├── DBConnection.java
│   │   ├── UserDao.java
│   │   └── QuizResultsDao.java (NEW)
│   ├── model/
│   │   └── User.java
│   └── servlet/
│       ├── LoginServlet.java
│       ├── RegisterServlet.java
│       ├── JavaQuizServlet.java (MODIFIED)
│       ├── CppQuizServlet.java (MODIFIED)
│       ├── DsaQuizServlet.java (MODIFIED)
│       ├── PythonQuizServlet.java (MODIFIED)
│       ├── ResultServlet.java
│       ├── AdminLoginServlet.java (NEW)
│       └── AdminDashboardServlet.java (NEW)
├── src/main/webapp/
│   ├── login.jsp (MODIFIED - Added admin link)
│   ├── register.jsp
│   ├── quiz.jsp
│   ├── javaQuiz.jsp
│   ├── cppQuiz.jsp
│   ├── dsaQuiz.jsp
│   ├── pythonQuiz.jsp
│   ├── result.jsp
│   ├── clearQuiz.jsp
│   ├── adminLogin.jsp (NEW)
│   ├── adminDashboard.jsp (NEW)
│   ├── adminLogout.jsp (NEW)
│   ├── WEB-INF/
│   │   └── web.xml (MODIFIED - Added admin servlets)
│   └── index.jsp
├── pom.xml (MODIFIED - Java 11)
├── sql_setup.sql (NEW)
└── target/
    └── OnlineExamApp.war
```

## Build & Rebuild Commands

### Clean Build
```bash
mvn clean package
```

### Quick Build (skip tests)
```bash
mvn package -DskipTests
```

### Check for Compilation Errors
```bash
mvn test-compile
```

## Next Steps (Optional Enhancements)

1. **Move Admin Credentials to Database**
   - Create admin users table
   - Update AdminLoginServlet to query database

2. **Add Admin Dashboard Filters**
   - Filter results by date range
   - Filter by quiz type
   - Filter by username

3. **User Performance Analytics**
   - Show user's own quiz history
   - Display personal best scores
   - Track improvement over attempts

4. **Export Functionality**
   - Export results to PDF/CSV
   - Generate performance reports

5. **Question Bank Management**
   - Admin interface to add/edit questions
   - Dynamic question loading from database

## Support

For issues or questions:
1. Check logs in Tomcat: `$CATALINA_HOME/logs/`
2. Verify database connectivity
3. Ensure all tables are created in online_exam database
4. Check that web.xml servlet mappings are correct
