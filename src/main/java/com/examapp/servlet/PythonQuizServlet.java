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

public class PythonQuizServlet extends HttpServlet {

    private static final String[] QUESTION_POOL = {
        "What keyword is used to define a function in Python?",
        "Which data structure is used for key-value pairs in Python?",
        "Which library is used for numerical computing in Python?",
        "Which keyword is used for exception handling in Python?",
        "What is an anonymous function in Python called?",
        "What does a single leading underscore mean in Python?",
        "What is the result of bool(0) in Python?",
        "How do you include external modules in Python?",
        "What is significant in Python programming?",
        "What is the purpose of the __init__ method?"
    };

    private static final String[] ANSWER_POOL = {
        "def",
        "dictionary",
        "numpy",
        "try",
        "lambda",
        "private convention",
        "false",
        "import",
        "indentation",
        "Constructor for class"
    };

    private static final String[][] OPTIONS = {
        {"function", "func", "def", "define"},
        {"list", "dictionary", "tuple", "set"},
        {"pandas", "numpy", "scipy", "matplotlib"},
        {"try", "throw", "catch", "handle"},
        {"arrow function", "lambda", "anonymous", "func"},
        {"private convention", "protected member", "underscore variable", "local variable"},
        {"true", "false", "null", "none"},
        {"include", "import", "require", "use"},
        {"semicolons", "indentation", "braces", "parentheses"},
        {"Destructor method", "Constructor for class", "Static method", "Class method"}
    };

    // ======================= GET =======================
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

        session.setAttribute("pythonQuestions", questions);
        session.setAttribute("pythonCorrectAnswers", correctAnswers);
        session.setAttribute("pythonOptions", options);

        request.getRequestDispatcher("pythonQuiz.jsp").forward(request, response);
    }

    // ======================= POST =======================
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String[] correctAnswers = (String[]) session.getAttribute("pythonCorrectAnswers");
        String[] questions = (String[]) session.getAttribute("pythonQuestions");

        // ✅ CRITICAL SAFETY CHECK
        if (correctAnswers == null || questions == null) {
            response.sendRedirect("pythonQuiz");
            return;
        }

        User user = (User) session.getAttribute("user");

        int score = 0;
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("p" + (i + 1));

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

        QuizResultsDao.saveQuizResult(user.getUsername(), "Python", score, 10);

        session.removeAttribute("pythonQuestions");
        session.removeAttribute("pythonCorrectAnswers");
        session.removeAttribute("pythonOptions");

        session.setAttribute("score", score);
        session.setAttribute("quizType", "Python");
        session.setAttribute("answerKey", answerKey);

        response.sendRedirect("result.jsp");
    }
}
