package com.examapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.examapp.dao.QuizResultsDao;
import com.examapp.model.User;
import com.examapp.util.QuestionShuffler;
import com.examapp.util.QuestionShuffler.Question;

import java.io.IOException;
import java.util.*;

public class JavaQuizServlet extends HttpServlet {

    private static final String[] QUESTION_POOL = {
        "Which of the following is NOT a Java keyword?",
        "What is the default value of an int variable in Java?",
        "How do you compare two strings for content equality in Java?",
        "What is the size of an int array with 5 elements?",
        "Which collection is NOT part of Java Collections Framework?",
        "What exception is thrown when accessing an invalid array index?",
        "Can you override a static method in Java?",
        "What is the output of System.out.println(10 + 20 + \"30\")?",
        "Which keyword is used to implement an interface in Java?",
        "What is the purpose of the final keyword in Java?"
    };

    private static final String[] ANSWER_POOL = {
        "pointer", "0", ".equals() method", "20 bytes", "HashArray",
        "IndexOutOfBoundsException", "No", "3030", "implements", "Prevent modification"
    };

    private static final String[][] OPTIONS = {
        {"extends", "implements", "pointer", "virtual"},
        {"null", "undefined", "0", "-1"},
        {".equals() method", "== operator", ".compare() method", "=== operator"},
        {"5 bytes", "10 bytes", "20 bytes", "25 bytes"},
        {"TreeMap", "HashMap", "HashArray", "LinkedList"},
        {"NullPointerException", "ArrayOutOfBoundsException", "IndexOutOfBoundsException", "BufferException"},
        {"Yes", "No", "Sometimes", "Only in interfaces"},
        {"3050", "3030", "30", "1030"},
        {"extends", "implements", "interface", "inherit"},
        {"Prevent modification", "Allow modification", "Increase speed", "Reduce memory"}
    };

    // ====================== GET ======================
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Question> shuffled = QuestionShuffler.shuffleQuestions(QUESTION_POOL, ANSWER_POOL);

        String[] questions = new String[10];
        String[] correctAnswers = new String[10];
        String[][] options = new String[10][4];

        for (int i = 0; i < 10; i++) {
            questions[i] = shuffled.get(i).text;
            correctAnswers[i] = shuffled.get(i).correctAnswer;

            int idx = Arrays.asList(QUESTION_POOL).indexOf(shuffled.get(i).text);
            options[i] = OPTIONS[idx].clone();
        }

        session.setAttribute("javaQuestions", questions);
        session.setAttribute("javaCorrectAnswers", correctAnswers);
        session.setAttribute("javaOptions", options);

        request.getRequestDispatcher("javaQuiz.jsp").forward(request, response);
    }

    // ====================== POST ======================
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String[] correctAnswers = (String[]) session.getAttribute("javaCorrectAnswers");
        String[] questions = (String[]) session.getAttribute("javaQuestions");

        // ✅ CRITICAL FIX (prevents HTTP 500)
        if (correctAnswers == null || questions == null) {
            response.sendRedirect("javaQuiz");
            return;
        }

        User user = (User) session.getAttribute("user");

        int score = 0;
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("j" + (i + 1));

            boolean isCorrect =
                    userAnswer != null &&
                    correctAnswers[i] != null &&
                    correctAnswers[i].equals(userAnswer);

            if (isCorrect) score++;

            Map<String, Object> q = new HashMap<>();
            q.put("question", (i + 1) + ". " + questions[i]);
            q.put("userAnswer", userAnswer == null ? "Not answered" : userAnswer);
            q.put("correctAnswer", correctAnswers[i]);
            q.put("isCorrect", isCorrect);

            answerKey.put(i + 1, q);
        }

        QuizResultsDao.saveQuizResult(user.getUsername(), "Java", score, 10);

        session.removeAttribute("javaQuestions");
        session.removeAttribute("javaCorrectAnswers");
        session.removeAttribute("javaOptions");

        session.setAttribute("score", score);
        session.setAttribute("quizType", "Java");
        session.setAttribute("answerKey", answerKey);

        response.sendRedirect("result.jsp");
    }
}
