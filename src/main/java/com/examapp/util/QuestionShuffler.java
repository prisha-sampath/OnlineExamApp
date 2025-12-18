package com.examapp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionShuffler {
    public static class Question {
        public String text;
        public String correctAnswer;
        
        public Question(String text, String correctAnswer) {
            this.text = text;
            this.correctAnswer = correctAnswer;
        }
    }
    
    public static List<Question> shuffleQuestions(String[] questionTexts, String[] correctAnswers) {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < questionTexts.length; i++) {
            questions.add(new Question(questionTexts[i], correctAnswers[i]));
        }
        Collections.shuffle(questions);
        return questions;
    }
}
