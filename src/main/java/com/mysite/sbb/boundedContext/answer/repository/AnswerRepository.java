package com.mysite.sbb.boundedContext.answer.repository;

import com.mysite.sbb.boundedContext.answer.entity.Answer;
import com.mysite.sbb.boundedContext.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    Page<Answer> findAllByQuestion(Question question, Pageable pageable);
}