# 🚀 COMPLETE DATABASE SETUP - STEP BY STEP

## ⚠️ Current Status
Dashboard shows: **"No quiz results found yet"**

This means the `quiz_results` table either **doesn't exist** or is **empty**.

---

## ✅ STEP 1: Create the Database Table

### **Option A: Using MySQL Command Prompt (Easiest)**

1. **Open Command Prompt**:
   - Press `Windows Key + R`
   - Type: `cmd`
   - Press Enter

2. **Login to MySQL**:
   ```
   mysql -u root -p
   ```
   - **When asked for password, type**: `Prishasampath`
   - Press Enter

3. **Select the online_exam database**:
   ```sql
   USE online_exam;
   ```

4. **Create the table - Copy and paste this ENTIRE command**:
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

5. **Verify the table was created**:
   ```sql
   SHOW TABLES;
   ```
   **You should see**:
   ```
   +--------------------+
   | Tables_in_online_exam |
   +--------------------+
   | quiz_results       |
   | users              |
   +--------------------+
   ```

6. **Type to exit MySQL**:
   ```
   EXIT;
   ```

---

### **Option B: Using MySQL Workbench**

1. **Open MySQL Workbench**
2. **Click** on your connection to open it
3. **Go to**: File → Open SQL Script
4. **Navigate to**: `C:\Users\SreePrishaS\Documents\project\OnlineExamApp\sql_setup.sql`
5. **Click Open**
6. **Click the Lightning Bolt ⚡ icon** at the top to execute
7. **Check the output** - should show `0 rows affected`

---

## ✅ STEP 2: Verify Table Exists

Run this in MySQL to verify:
```sql
USE online_exam;
DESCRIBE quiz_results;
```

You should see:
```
+-----------------+--------------+------+-----+-------------------+-------------------+
| Field           | Type         | Null | Key | Default           | Extra             |
+-----------------+--------------+------+-----+-------------------+-------------------+
| id              | int          | NO   | PRI | NULL              | auto_increment    |
| username        | varchar(50)  | NO   | MUL | NULL              |                   |
| quiz_type       | varchar(20)  | NO   |     | NULL              |                   |
| score           | int          | NO   |     | NULL              |                   |
| total_questions | int          | NO   |     | NULL              |                   |
| percentage      | int          | NO   |     | NULL              |                   |
| attempt_date    | timestamp    | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
+-----------------+--------------+------+-----+-------------------+-------------------+
```

If you see this table structure, ✅ **you're good!**

---

## ✅ STEP 3: Test the Application

### **A. Register a Student**

1. Go to: `http://localhost:8080/OnlineExamApp/register.jsp`
2. Fill in:
   - **Username**: `student1`
   - **Password**: `password123`
   - **Email**: `student1@test.com`
3. Click **Register**
4. You should see: "Registration successful!" ✅

### **B. Login as Student**

1. Go to: `http://localhost:8080/OnlineExamApp/login.jsp`
2. Enter:
   - **Username**: `student1`
   - **Password**: `password123`
3. Click **Login** ✅

### **C. Take a Quiz**

1. You'll see language options: Java, C++, DSA, Python
2. **Click "Java Quiz"**
3. Answer all 10 questions
4. **Click "Submit Quiz"** at the bottom
5. You'll see your score and answer key ✅

**⚠️ IMPORTANT**: The quiz result is now saved to the database!

### **D. View Results in Admin Dashboard**

1. Go to: `http://localhost:8080/OnlineExamApp/adminLogin.jsp`
2. Enter:
   - **Username**: `admin`
   - **Password**: `admin123`
3. Click **Login**
4. **NOW YOU SHOULD SEE** your quiz result in the table! 📊

---

## 🔍 STEP 4: Verify Data in Database

Open MySQL and run:
```sql
USE online_exam;
SELECT * FROM quiz_results;
```

You should see:
```
| id | username | quiz_type | score | total_questions | percentage | attempt_date            |
|----|----------|-----------|-------|-----------------|------------|-------------------------|
| 1  | student1 | Java      | 7     | 10              | 70         | 2025-12-17 10:30:45     |
```

---

## ❌ TROUBLESHOOTING

### **Problem: "No quiz results found yet"**

**Check 1: Does quiz_results table exist?**
```sql
USE online_exam;
SHOW TABLES LIKE 'quiz_results';
```
- **If empty**: Run the CREATE TABLE command above
- **If shows quiz_results**: Table exists ✅

**Check 2: Are there any records?**
```sql
SELECT * FROM quiz_results;
```
- **If empty**: A student needs to complete a quiz first
- **If has data**: Check admin dashboard

**Check 3: Can you login as admin?**
- Go to: http://localhost:8080/OnlineExamApp/adminLogin.jsp
- Username: `admin`
- Password: `admin123`
- If you see dashboard but no results, do **Check 2**

---

## 📋 COMPLETE WORKFLOW

```
1. CREATE TABLE quiz_results
   ↓
2. Deploy OnlineExamApp.war to Tomcat
   ↓
3. Register student: student1/password123
   ↓
4. Login as student1
   ↓
5. Select and take Java quiz (10 questions)
   ↓
6. Submit quiz → Result saved to database automatically
   ↓
7. Login as admin: admin/admin123
   ↓
8. See student1's marks in admin dashboard ✅
```

---

## 🆘 STILL NOT WORKING?

### **Use Diagnostic Page**

1. Go to: `http://localhost:8080/OnlineExamApp/diagnostic.jsp`
2. It will show:
   - ✅ Database connection status
   - ✅ If quiz_results table exists
   - ✅ How many records are in the table
   - 📋 Exact SQL commands to run

This page will tell you exactly what's wrong!

---

## ✅ QUICK CHECKLIST

- [ ] MySQL is running
- [ ] Created `quiz_results` table with CREATE TABLE command
- [ ] Can login as admin (admin/admin123)
- [ ] Registered a student (student1/password123)
- [ ] Student logged in and took a quiz
- [ ] Submitted quiz answers
- [ ] Checked admin dashboard
- [ ] Saw student's marks in the results table

---

## 📞 IF ALL ELSE FAILS

1. **Open Command Prompt**
2. **Completely reset and recreate**:
   ```bash
   mysql -u root -p online_exam < "C:\Users\SreePrishaS\Documents\project\OnlineExamApp\sql_setup.sql"
   ```
   - Enter password: `Prishasampath`

3. **Then verify**:
   ```bash
   mysql -u root -p online_exam -e "SHOW TABLES; DESCRIBE quiz_results;"
   ```

---

**Status**: 🔴 No quiz results → 🟡 Table needs to be created → 🟢 Ready!

**Next Action**: Follow STEP 1 to create the table, then STEP 3 to test!
