package com.Quiz.Website.Quiz.Website.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Quiz.Website.Quiz.Website.model.Question;
import com.Quiz.Website.Quiz.Website.model.Quiz;
import com.Quiz.Website.Quiz.Website.repository.QuestionRepository;
import com.Quiz.Website.Quiz.Website.repository.QuizRepository;

@Service
public class QuizService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<Question> getQuestionsBySubject(String subject){
        return questionRepository.findBySubject(subject);
    }

    public Quiz saveQuiz(Quiz quiz)
    {
        return quizRepository.save(quiz);
    }

}
