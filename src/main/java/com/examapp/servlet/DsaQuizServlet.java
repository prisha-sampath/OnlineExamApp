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
        "What is the topological sort used for?",
        "What is the difference between BFS and DFS?",
        "What is a trie data structure?",
        "What is the purpose of a heap?",
        "What is the difference between a stack and a queue?",
        "What is dynamic programming?",
        "What is the purpose of memoization?",
        "What is the time complexity of searching in a balanced BST?",
        "What is a graph cycle?",
        "What does the Dijkstra algorithm find?",
        "What is the purpose of hashing?"
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
        "Sorting in topological order",
        "BFS level by level DFS depth by depth",
        "Prefix search tree structure",
        "Priority queue data structure",
        "Stack LIFO Queue FIFO",
        "Breaking problems into subproblems",
        "Caching/storing results",
        "O(log n)",
        "Cycle is loop in graph",
        "Shortest path algorithm",
        "Fast data retrieval"
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
        {"Sorting elements", "Sorting in topological order", "Creating loops", "Deleting nodes"},
        {"BFS uses queue DFS uses stack", "BFS level by level DFS depth by depth", "Same thing", "BFS slower"},
        {"Hash table", "Prefix search tree structure", "Binary tree", "Stack"},
        {"Sorting algorithm", "Priority queue data structure", "Linked list", "Graph"},
        {"No difference", "Stack LIFO Queue FIFO", "Both FIFO", "Both LIFO"},
        {"Recursion method", "Breaking problems into subproblems", "Sorting method", "Searching method"},
        {"Sorting results", "Caching/storing results", "Deleting results", "Printing results"},
        {"O(1)", "O(log n)", "O(n)", "O(n log n)"},
        {"Cycle is missing edge", "Cycle is loop in graph", "No cycles exist", "Cycle is node"},
        {"Minimum path", "Shortest path algorithm", "Longest path", "Random path"},
        {"Slow lookup", "Fast data retrieval", "Data storage only", "No use"}
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
        
        session.setAttribute("dsaQuestions", selectedQuestions);
        session.setAttribute("dsaCorrectAnswers", correctAnswers);
        session.setAttribute("dsaOptions", selectedOptions);
        
        request.getRequestDispatcher("dsaQuiz.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String[] correctAnswers = (String[]) session.getAttribute("dsaCorrectAnswers");
        String[] selectedQuestions = (String[]) session.getAttribute("dsaQuestions");
        
        String[] userAnswers = new String[10];
        Map<Integer, Map<String, Object>> answerKey = new HashMap<>();
        
        int score = 0;
        for (int i = 0; i < 10; i++) {
            String userAnswer = request.getParameter("d" + (i + 1));
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
