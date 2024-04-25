package com.mysite.sbb.answer;

import org.springframework.data.domain.Page;
import com.mysite.sbb.question.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findByQuestion(Question question, Pageable pageable);
}
