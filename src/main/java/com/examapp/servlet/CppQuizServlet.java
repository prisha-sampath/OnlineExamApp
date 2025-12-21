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

public class CppQuizServlet extends HttpServlet {

    private static final String[] QUESTION_POOL = {
        "Which of the following is the correct way to declare a pointer in C++?",
        "What is the output of std::cout << 5 + 3 * 2; in C++?",
        "In C++, what is the difference between a reference and a pointer?",
        "Which of the following is used to allocate memory dynamically in C++?",
        "What is the main difference between struct and class in C++?",
        "What does virtual function in C++ provide?",
        "What is the correct syntax for a template function in C++?",
        "What does the const keyword do in C++?",
        "Which of the following is NOT a standard C++ container?",
        "What is the purpose of delete operator in C++?"
    };

    private static final String[] ANSWER_POOL = {
        "int* ptr;", "11", "Reference cannot be null or reassigned", "new",
        "struct members public by default", "polymorphism",
        "template <typename T>", "Makes variable unchangeable", "bag",
        "Deallocates memory"
    };

    private static final String[][] OPTIONS = {
        {"int ptr;", "int* ptr;", "int &ptr;", "pointer int ptr;"},
        {"16", "11", "8", "compile error"},
        {"No difference", "References can be reassigned",
         "Reference cannot be null or reassigned", "Pointers cannot be modified"},
        {"malloc()", "new", "alloc()", "create()"},
        {"No difference", "struct members public by default",
         "class members cannot be inherited", "struct has no members"},
        {"Increased speed", "polymorphism", "Better memory management", "Simplified syntax"},
        {"template <typename T>", "generic<T>", "typeof<T>", "var<T>"},
        {"Makes variable unchangeable", "Prevents inheritance",
         "Makes it a public element", "Optimizes memory"},
        {"vector", "array", "list", "bag"},
        {"Deletes a variable", "Deallocates memory", "Removes a file", "Clears the program"}
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

        session.setAttribute("cppQuestions", questions);
        session.setAttribute("cppCorrectAnswers", correctAnswers);
        session.setAttribute("cppOptions", options);

        request.getRequestDispatcher("cppQuiz.jsp").forward(request, response);
    }

    // ====================== POST ======================
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String[] correctAnswers = (String[]) session.getAttribute("cppCorrectAnswers");
        String[] questions = (String[]) session.getAttribute("cppQuestions");

        // ✅ CRITICAL SAFETY CHECK
        if (correctAnswers == null || questions == null) {
            response.sendRedirect("cppQuiz");
            return;
        }

        User user = (User) session.getAttribute("user");

        int score = 0;
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("c" + (i + 1));

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

        QuizResultsDao.saveQuizResult(user.getUsername(), "C++", score, 10);

        session.removeAttribute("cppQuestions");
        session.removeAttribute("cppCorrectAnswers");
        session.removeAttribute("cppOptions");

        session.setAttribute("score", score);
        session.setAttribute("quizType", "C++");
        session.setAttribute("answerKey", answerKey);

        response.sendRedirect("result.jsp");
    }
}
