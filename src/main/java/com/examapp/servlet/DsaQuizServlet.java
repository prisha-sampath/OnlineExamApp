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

public class DsaQuizServlet extends HttpServlet {

    private static final String[] QUESTION_POOL = {
        "What is the time complexity of binary search?",
        "Which data structure allows insertion at both ends?",
        "What is the best-case time complexity of quicksort?",
        "Which data structure provides O(1) average lookup time?",
        "What is a self-balancing binary search tree?",
        "Which graph traversal technique uses a queue?",
        "Which algorithm solves problems with overlapping subproblems?",
        "What is the time complexity of merge sort?",
        "What is an AVL tree?",
        "What is the topological sort used for?"
    };

    private static final String[] ANSWER_POOL = {
        "O(log n)",
        "Deque (Double-ended Queue)",
        "O(n log n)",
        "Hash Table",
        "Red-Black tree",
        "BFS (Breadth-First Search)",
        "Dynamic Programming",
        "O(n log n)",
        "AVL tree is self-balancing",
        "Sorting in topological order"
    };

    private static final String[][] OPTIONS = {
        {"O(n)", "O(log n)", "O(n log n)", "O(1)"},
        {"Stack", "Deque (Double-ended Queue)", "Queue", "Tree"},
        {"O(n)", "O(n log n)", "O(n log n)", "O(1)"},
        {"Stack", "Hash Table", "Queue", "Linked List"},
        {"Hash tree", "Red-Black tree", "Simple tree", "Linked list"},
        {"DFS", "BFS (Breadth-First Search)", "Both", "Neither"},
        {"Greedy algorithm", "Dynamic Programming", "Recursion only", "Iteration only"},
        {"O(n)", "O(n log n)", "O(n^2)", "O(1)"},
        {"Normal binary tree", "AVL tree is self-balancing", "Simple tree", "Hash tree"},
        {"Sorting elements", "Sorting in topological order", "Creating loops", "Deleting nodes"}
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

        session.setAttribute("dsaQuestions", questions);
        session.setAttribute("dsaCorrectAnswers", correctAnswers);
        session.setAttribute("dsaOptions", options);

        request.getRequestDispatcher("dsaQuiz.jsp").forward(request, response);
    }

    // ====================== POST ======================
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String[] correctAnswers = (String[]) session.getAttribute("dsaCorrectAnswers");
        String[] questions = (String[]) session.getAttribute("dsaQuestions");

        // ✅ CRITICAL SAFETY CHECK
        if (correctAnswers == null || questions == null) {
            response.sendRedirect("dsaQuiz");
            return;
        }

        User user = (User) session.getAttribute("user");

        int score = 0;
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("d" + (i + 1));

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

        QuizResultsDao.saveQuizResult(user.getUsername(), "DSA", score, 10);

        session.removeAttribute("dsaQuestions");
        session.removeAttribute("dsaCorrectAnswers");
        session.removeAttribute("dsaOptions");

        session.setAttribute("score", score);
        session.setAttribute("quizType", "DSA");
        session.setAttribute("answerKey", answerKey);

        response.sendRedirect("result.jsp");
    }
}
