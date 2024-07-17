package com.Quiz.Website.Quiz.Website.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Quiz.Website.Quiz.Website.model.Question;
import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question,Long>{

    List<Question> findBySubject(String subject);
    
}
