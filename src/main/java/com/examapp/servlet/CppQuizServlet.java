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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

public class CppQuizServlet extends HttpServlet {
    // Question-Option mapping - each question has index and its 4 options
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
        "What is the purpose of delete operator in C++?",
        "What is the difference between public and private access modifiers?",
        "What is operator overloading in C++?",
        "What is a constructor in C++?",
        "What is the purpose of the scope resolution operator (::)?",
        "What is an abstract class in C++?",
        "What does inline function do in C++?",
        "What is the difference between vector and array?",
        "What is smart pointer in C++?",
        "What does std::endl do in C++?",
        "What is the difference between pass by value and pass by reference?"
    };
    
    private static final String[] ANSWER_POOL = {
        "int* ptr;",
        "11",
        "Reference cannot be null or reassigned",
        "new",
        "struct members public by default",
        "polymorphism",
        "template <typename T>",
        "Makes variable unchangeable",
        "bag",
        "Deallocates memory",
        "Public is accessible externally",
        "Redefining operator for custom types",
        "Initializes object members",
        "Access members of namespace or class",
        "Class with virtual functions",
        "Expands at compile time",
        "Vector is dynamic array",
        "Automatic memory management",
        "Flushes output stream",
        "Reference is alias to variable"
    };

    // Options for each question (4 options per question)
    private static final String[][] OPTIONS = {
        {"int ptr;", "int* ptr;", "int &ptr;", "pointer int ptr;"},
        {"16", "11", "8", "compile error"},
        {"No difference", "References can be reassigned", "Reference cannot be null or reassigned", "Pointers cannot be modified"},
        {"malloc()", "new", "alloc()", "create()"},
        {"No difference", "struct members public by default", "class members cannot be inherited", "struct has no members"},
        {"Increased speed", "polymorphism", "Better memory management", "Simplified syntax"},
        {"template <typename T>", "generic<T>", "typeof<T>", "var<T>"},
        {"Makes variable unchangeable", "Prevents inheritance", "Makes it a public element", "Optimizes memory"},
        {"vector", "array", "list", "bag"},
        {"Deletes a variable", "Deallocates memory", "Removes a file", "Clears the program"},
        {"Public is accessible externally", "Private is accessible only internally", "Protected is accessible only in subclasses", "All are same"},
        {"No such thing", "Redefining operator for custom types", "Changing data type", "Creating new operators"},
        {"Destructor", "Initializes object members", "Deletes object", "Handles errors"},
        {"Displays variable scope", "Access members of namespace or class", "Shows memory address", "Separates namespaces"},
        {"No such concept", "Class with virtual functions", "Class that cannot be inherited", "Class with pure virtual functions"},
        {"Slows execution", "Expands at compile time", "Creates duplicate code", "Improves cache performance"},
        {"No difference", "Vector is dynamic array", "Array is dynamic vector", "Array can store different types"},
        {"Regular pointer", "Automatic memory management", "Very slow pointer", "Read-only pointer"},
        {"Ends program", "Flushes output stream", "Clears variables", "Terminates thread"},
        {"No difference", "Reference is alias to variable", "Reference is same as pointer", "Pass by value is better"}
    };

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Shuffle questions and store in session
        List<Question> shuffledQuestions = QuestionShuffler.shuffleQuestions(QUESTION_POOL, ANSWER_POOL);
        
        String[] selectedQuestions = new String[10];
        String[] correctAnswers = new String[10];
        String[][] selectedOptions = new String[10][4];
        int[] questionIndices = new int[10];
        
        for (int i = 0; i < 10; i++) {
            selectedQuestions[i] = shuffledQuestions.get(i).text;
            correctAnswers[i] = shuffledQuestions.get(i).correctAnswer;
            
            // Find the index of this question in QUESTION_POOL to get its options
            int questionIndex = Arrays.asList(QUESTION_POOL).indexOf(shuffledQuestions.get(i).text);
            questionIndices[i] = questionIndex;
            selectedOptions[i] = OPTIONS[questionIndex].clone();
        }
        
        session.setAttribute("cppQuestions", selectedQuestions);
        session.setAttribute("cppCorrectAnswers", correctAnswers);
        session.setAttribute("cppOptions", selectedOptions);
        
        request.getRequestDispatcher("cppQuiz.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String[] correctAnswers = (String[]) session.getAttribute("cppCorrectAnswers");
        String[] selectedQuestions = (String[]) session.getAttribute("cppQuestions");
        
        String[] userAnswers = new String[10];
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();
        
        int score = 0;
        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("c" + (i + 1));
            userAnswers[i] = userAnswer;
            boolean isCorrect = correctAnswers[i] != null && correctAnswers[i].equals(userAnswer);
            if (isCorrect) score++;
            
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("question", (i + 1) + ". " + selectedQuestions[i]);
            questionData.put("userAnswer", userAnswer != null ? userAnswer : "Not answered");
            questionData.put("correctAnswer", correctAnswers[i]);
            questionData.put("isCorrect", isCorrect);
            answerKey.put(i + 1, questionData);
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
