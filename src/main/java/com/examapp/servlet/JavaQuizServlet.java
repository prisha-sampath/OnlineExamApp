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
        "What is the purpose of the final keyword in Java?",
        "What is the difference between ArrayList and LinkedList?",
        "Which method is used to find the length of a string in Java?",
        "What does the synchronized keyword do in Java?",
        "What is the purpose of the volatile keyword in Java?",
        "What is a package in Java used for?",
        "How is memory management handled in Java?",
        "What is method overloading in Java?",
        "What is the difference between abstract class and interface?",
        "What does the 'new' keyword do in Java?",
        "What is the purpose of the this keyword in Java?"
    };
    
    private static final String[] ANSWER_POOL = {
        "pointer",
        "0",
        ".equals() method",
        "20 bytes",
        "HashArray",
        "IndexOutOfBoundsException",
        "No",
        "3030",
        "implements",
        "Prevent modification",
        "LinkedList is better for insertion",
        ".length()",
        "Makes code thread-safe",
        "Indicates variable changes externally",
        "Grouping related classes",
        "Garbage collection",
        "Same method name different parameters",
        "Interface has no state",
        "Creates new object instance",
        "Reference to current object"
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
        {"Prevent modification", "Allow modification", "Increase speed", "Reduce memory"},
        {"ArrayList is better for insertion", "LinkedList is better for insertion", "No difference", "ArrayList is faster"},
        {".size()", ".length()", ".count()", ".getLength()"},
        {"Makes code readable", "Makes code thread-safe", "Improves performance", "Reduces memory"},
        {"Improves performance", "Indicates variable changes externally", "Prevents modification", "Allows modification"},
        {"Grouping related classes", "Creating objects", "Declaring methods", "Creating variables"},
        {"Reference counting", "Garbage collection", "Manual management", "Memory pooling"},
        {"Same method name different parameters", "Different method names only", "Same parameters only", "Same return type"},
        {"Abstract has no methods", "Interface has no state", "Same thing", "Abstract is faster"},
        {"Deletes object", "Creates new object instance", "Assigns variable", "Calls method"},
        {"Refers to global object", "Reference to current object", "Reference to parent", "Reference to static member"}
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
        
        for (int i = 0; i < 10; i++) {
            selectedQuestions[i] = shuffledQuestions.get(i).text;
            correctAnswers[i] = shuffledQuestions.get(i).correctAnswer;
            
            // Find the index of this question to get its options
            int questionIndex = Arrays.asList(QUESTION_POOL).indexOf(shuffledQuestions.get(i).text);
            selectedOptions[i] = OPTIONS[questionIndex].clone();
        }
        
        session.setAttribute("javaQuestions", selectedQuestions);
        session.setAttribute("javaCorrectAnswers", correctAnswers);
        session.setAttribute("javaOptions", selectedOptions);
        
        request.getRequestDispatcher("javaQuiz.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String[] correctAnswers = (String[]) session.getAttribute("javaCorrectAnswers");
        String[] selectedQuestions = (String[]) session.getAttribute("javaQuestions");
        
        String[] userAnswers = new String[10];
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();
        
        int score = 0;
        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("j" + (i + 1));
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
