# Deployment Commands - Online Exam App

## 📋 Quick Command Reference

### 1. Build the Project (from project directory)
```bash
cd c:\Users\SreePrishaS\Documents\project\OnlineExamApp
mvn clean package
```

**Expected Output:**
```
BUILD SUCCESS
```

**Output File**: `target/OnlineExamApp.war`

---

### 2. Create Database Table (in MySQL)

#### Option A: Using MySQL Command Line
```bash
mysql -u root -p
```

Then run:
```sql
USE online_exam;

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

#### Option B: Using MySQL Script File
```bash
mysql -u root -p online_exam < sql_setup.sql
```

#### Verify Table Created
```sql
SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'online_exam';
```

---

### 3. Deploy to Apache Tomcat

#### Windows
```batch
REM Copy WAR file
copy target\OnlineExamApp.war "%CATALINA_HOME%\webapps\"

REM Restart Tomcat (from Command Prompt as Administrator)
%CATALINA_HOME%\bin\shutdown.bat
pause
%CATALINA_HOME%\bin\startup.bat
```

#### Linux/Mac
```bash
# Copy WAR file
cp target/OnlineExamApp.war $CATALINA_HOME/webapps/

# Restart Tomcat
$CATALINA_HOME/bin/shutdown.sh
$CATALINA_HOME/bin/startup.sh
```

#### Docker (Optional)
```bash
# If using Tomcat in Docker
docker cp target/OnlineExamApp.war tomcat_container:/usr/local/tomcat/webapps/
docker restart tomcat_container
```

---

### 4. Verify Deployment

#### Check if WAR is Deployed
```bash
# Windows
dir "%CATALINA_HOME%\webapps\OnlineExamApp"

# Linux/Mac
ls $CATALINA_HOME/webapps/OnlineExamApp
```

#### Check Tomcat Logs
```bash
# Windows
type "%CATALINA_HOME%\logs\catalina.out"

# Linux/Mac
tail -f $CATALINA_HOME/logs/catalina.out
```

#### Access Application
```
http://localhost:8080/OnlineExamApp/
```

---

### 5. Test the Application

#### Test as User
```bash
# Open browser and navigate to:
http://localhost:8080/OnlineExamApp/login.jsp

# 1. Click "Register here"
# 2. Enter: username=testuser, password=test123
# 3. Register
# 4. Login with credentials
# 5. Select Java quiz
# 6. Answer 10 questions
# 7. Submit and view results
```

#### Test as Admin
```bash
# Open browser and navigate to:
http://localhost:8080/OnlineExamApp/adminLogin.jsp

# 1. Enter: admin / admin123
# 2. Click Login
# 3. View dashboard
# 4. Verify quiz results appear
# 5. Check color-coded scores
# 6. Click Logout
```

---

### 6. Monitor Database

#### View Quiz Results
```sql
SELECT * FROM quiz_results ORDER BY attempt_date DESC;
```

#### View Results by User
```sql
SELECT * FROM quiz_results WHERE username = 'testuser' ORDER BY attempt_date DESC;
```

#### View Results by Quiz Type
```sql
SELECT * FROM quiz_results WHERE quiz_type = 'Java' ORDER BY attempt_date DESC;
```

#### Get Statistics
```sql
SELECT 
    COUNT(*) as total_attempts,
    COUNT(DISTINCT username) as unique_candidates,
    AVG(percentage) as average_score
FROM quiz_results;
```

#### Check for Errors
```sql
SELECT * FROM quiz_results WHERE score IS NULL;
```

---

### 7. Troubleshooting Commands

#### Check MySQL Connection
```bash
# Windows
mysql -h localhost -u root -p -e "SELECT 1"

# Linux/Mac
mysql -h localhost -u root -p -e "SELECT 1"
```

#### Verify Database Exists
```sql
SHOW DATABASES LIKE 'online_exam';
```

#### Check Table Structure
```sql
DESCRIBE quiz_results;
```

#### Clear Tomcat Cache (if needed)
```bash
# Windows
rmdir /s "%CATALINA_HOME%\work\Catalina\localhost\OnlineExamApp"

# Linux/Mac
rm -rf $CATALINA_HOME/work/Catalina/localhost/OnlineExamApp
```

#### Force Tomcat to Reload WAR
```bash
# Remove WAR first
rm $CATALINA_HOME/webapps/OnlineExamApp.war
rm -rf $CATALINA_HOME/webapps/OnlineExamApp

# Then deploy again
cp target/OnlineExamApp.war $CATALINA_HOME/webapps/
```

---

### 8. Maven Build Options

#### Clean Build
```bash
mvn clean package
```

#### Build Skip Tests
```bash
mvn clean package -DskipTests
```

#### Verify Compilation Only
```bash
mvn clean compile
```

#### Check for Errors
```bash
mvn clean test-compile
```

#### Generate Project Report
```bash
mvn site
```

#### Display Dependency Tree
```bash
mvn dependency:tree
```

---

### 9. Tomcat Startup Commands

#### Start Tomcat
```bash
# Windows
%CATALINA_HOME%\bin\startup.bat

# Linux/Mac
$CATALINA_HOME/bin/startup.sh
```

#### Stop Tomcat
```bash
# Windows
%CATALINA_HOME%\bin\shutdown.bat

# Linux/Mac
$CATALINA_HOME/bin/shutdown.sh
```

#### Start Tomcat with Console Output
```bash
# Windows
%CATALINA_HOME%\bin\catalina.bat run

# Linux/Mac
$CATALINA_HOME/bin/catalina.sh run
```

---

### 10. File Management Commands

#### Copy WAR File (Windows)
```bash
copy target\OnlineExamApp.war "%CATALINA_HOME%\webapps\"
```

#### Copy WAR File (Linux/Mac)
```bash
cp target/OnlineExamApp.war $CATALINA_HOME/webapps/
```

#### Check WAR File Size
```bash
# Windows
dir target\OnlineExamApp.war

# Linux/Mac
ls -lh target/OnlineExamApp.war
```

#### Extract WAR for Inspection
```bash
# Windows
cd target
jar xf OnlineExamApp.war

# Linux/Mac
cd target
unzip OnlineExamApp.war
```

---

## 🚀 Complete Deployment Sequence

### One-Time Setup
```bash
# 1. Build project
cd c:\Users\SreePrishaS\Documents\project\OnlineExamApp
mvn clean package

# 2. Create database table (in MySQL)
mysql -u root -p online_exam < sql_setup.sql

# 3. Copy WAR to Tomcat
copy target\OnlineExamApp.war "%CATALINA_HOME%\webapps\"

# 4. Restart Tomcat
%CATALINA_HOME%\bin\shutdown.bat
pause
%CATALINA_HOME%\bin\startup.bat

# 5. Wait for deployment (30-60 seconds)

# 6. Test application
# Open browser: http://localhost:8080/OnlineExamApp/
```

### For Updates/Redeployment
```bash
# 1. Make code changes
# 2. Rebuild
mvn clean package

# 3. Stop Tomcat
%CATALINA_HOME%\bin\shutdown.bat

# 4. Replace WAR
copy target\OnlineExamApp.war "%CATALINA_HOME%\webapps\"

# 5. Start Tomcat
%CATALINA_HOME%\bin\startup.bat

# 6. Verify: http://localhost:8080/OnlineExamApp/
```

---

## 📊 Monitoring Commands

#### View Tomcat Logs (Real-time)
```bash
# Linux/Mac
tail -f $CATALINA_HOME/logs/catalina.out

# Windows (alternative)
Get-Content $env:CATALINA_HOME\logs\catalina.out -Wait -Tail 20
```

#### Database Connection Test
```bash
# Test from command line
mysql -h localhost -u root -p -e "USE online_exam; SELECT COUNT(*) FROM quiz_results;"
```

#### Port Check (verify Tomcat running)
```bash
# Windows
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :8080
```

---

## ✅ Checklist for Successful Deployment

```
[] 1. Ran: mvn clean package
[] 2. Build Status: SUCCESS
[] 3. WAR file created: target/OnlineExamApp.war
[] 4. Created database table: quiz_results
[] 5. Copied WAR to Tomcat webapps/
[] 6. Stopped Tomcat
[] 7. Started Tomcat
[] 8. Waited 30-60 seconds for deployment
[] 9. Opened http://localhost:8080/OnlineExamApp/
[] 10. Registered test user
[] 11. Logged in and took quiz
[] 12. Verified result saved in database
[] 13. Logged in as admin (admin/admin123)
[] 14. Verified results appear in admin dashboard
[] 15. Checked color-coded scores
[] 16. Tested admin logout
```

---

## 🔗 Quick Links

**Application URLs:**
- Home: http://localhost:8080/OnlineExamApp/
- User Login: http://localhost:8080/OnlineExamApp/login.jsp
- User Registration: http://localhost:8080/OnlineExamApp/register.jsp
- Admin Login: http://localhost:8080/OnlineExamApp/adminLogin.jsp

**Documentation:**
- QUICK_START.md - Fast 5-minute setup
- SETUP_GUIDE.md - Comprehensive guide
- ADMIN_IMPLEMENTATION_SUMMARY.md - Technical details
- FINAL_BUILD_REPORT.md - Build verification

**Database:**
- Database: online_exam
- Tables: users, quiz_results
- User Table: username (PK), password, email
- Results Table: id (PK), username (FK), quiz_type, score, percentage, attempt_date

---

## 💡 Common Issues & Solutions

**Problem**: Port 8080 already in use
```bash
# Change Tomcat port in $CATALINA_HOME/conf/server.xml
# Find <Connector port="8080" and change to 8081
```

**Problem**: Cannot connect to database
```sql
-- Verify connection in DBConnection.java
-- Test MySQL: mysql -u root -p -h localhost
-- Check online_exam database exists: SHOW DATABASES;
```

**Problem**: WAR not deploying
```bash
-- Delete failed deployment
rm -rf $CATALINA_HOME/webapps/OnlineExamApp
-- Verify WAR file: unzip -t target/OnlineExamApp.war
-- Redeploy
```

**Problem**: Results not saving
```sql
-- Verify quiz_results table exists
DESCRIBE quiz_results;
-- Check for errors: SELECT * FROM quiz_results;
```

---

**Status**: ✅ Ready for Deployment
**Commands**: All provided above
**Time to Deploy**: ~10 minutes
