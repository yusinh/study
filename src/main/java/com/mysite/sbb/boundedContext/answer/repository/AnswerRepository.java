package com.mysite.sbb.boundedContext.answer.repository;

import com.mysite.sbb.boundedContext.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}