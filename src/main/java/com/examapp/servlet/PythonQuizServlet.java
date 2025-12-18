package com.examapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import com.examapp.dao.QuizResultsDao;
import com.examapp.model.User;
import com.examapp.util.QuestionShuffler;
import com.examapp.util.QuestionShuffler.Question;

public class PythonQuizServlet extends HttpServlet {
    private static final String[] QUESTION_POOL = {
        "What keyword is used to define a function in Python?",
        "Which data structure is used for key-value pairs in Python?",
        "Which library is used for numerical computing in Python?",
        "Which keyword is used for exception handling in Python?",
        "What is an anonymous function in Python called?",
        "What does a single leading underscore mean in Python?",
        "What is the Python slicing syntax called?",
        "What is the result of bool(0) in Python?",
        "How do you include external modules in Python?",
        "What is significant in Python programming?",
        "What is the purpose of the __init__ method?",
        "How do you create a list in Python?",
        "What is the difference between a list and a tuple?",
        "How do you create a dictionary in Python?",
        "What is list comprehension in Python?",
        "What does the enumerate() function do?",
        "What is the difference between == and is in Python?",
        "What are decorators in Python?",
        "What is a generator in Python?",
        "What does the yield keyword do in Python?"
    };
    
    private static final String[] ANSWER_POOL = {
        "def",
        "dictionary",
        "numpy",
        "try",
        "lambda",
        "private convention",
        "slicing",
        "false",
        "import",
        "indentation",
        "Constructor for class",
        "square brackets with values",
        "Tuple is immutable",
        "curly braces with key-value",
        "Create list concisely",
        "Returns index with values",
        "is checks object identity == checks value",
        "Function modifiers",
        "Memory efficient iterator",
        "Suspend and resume function"
    };
    
    private static final String[][] OPTIONS = {
        {"function", "func", "def", "define"},
        {"list", "dictionary", "tuple", "set"},
        {"pandas", "numpy", "scipy", "matplotlib"},
        {"try", "throw", "catch", "handle"},
        {"arrow function", "lambda", "anonymous", "func"},
        {"private convention", "protected member", "underscore variable", "local variable"},
        {"cut", "slicing", "portion", "section"},
        {"true", "false", "null", "none"},
        {"include", "import", "require", "use"},
        {"semicolons", "indentation", "braces", "parentheses"},
        {"Destructor method", "Constructor for class", "Static method", "Class method"},
        {"parentheses with values", "square brackets with values", "curly braces", "angle brackets"},
        {"List is ordered tuple ordered", "Tuple is immutable", "List is immutable", "Both mutable"},
        {"square brackets", "parentheses", "curly braces with key-value", "angle brackets"},
        {"Using for loops", "Create list concisely", "Creating dictionaries", "Creating sets"},
        {"Prints all values", "Returns index with values", "Deletes elements", "Reverses list"},
        {"== checks value is checks type", "is checks object identity == checks value", "No difference", "is is faster"},
        {"Loop modifiers", "Function modifiers", "Class creators", "Variable setters"},
        {"Recursive function", "Memory efficient iterator", "Compiled function", "Imported function"},
        {"Loop keyword", "Suspend and resume function", "Create variables", "End program"}
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
        
        session.setAttribute("pythonQuestions", selectedQuestions);
        session.setAttribute("pythonCorrectAnswers", correctAnswers);
        session.setAttribute("pythonOptions", selectedOptions);
        
        request.getRequestDispatcher("pythonQuiz.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String[] correctAnswers = (String[]) session.getAttribute("pythonCorrectAnswers");
        String[] selectedQuestions = (String[]) session.getAttribute("pythonQuestions");
        
        String[] userAnswers = new String[10];
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();
        
        int score = 0;
        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("p" + (i + 1));
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
