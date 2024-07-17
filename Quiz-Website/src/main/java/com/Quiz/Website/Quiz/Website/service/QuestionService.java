package com.Quiz.Website.Quiz.Website.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Quiz.Website.Quiz.Website.model.Question;
import com.Quiz.Website.Quiz.Website.repository.QuestionRepository;


@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }
}
