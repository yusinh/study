package com.mysite.sbb.answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mysite.sbb.question.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findByQuestion(Question question, Pageable pageable);

    List<Answer> findByQuestionIdOrderByCreateDateDesc(Integer questionId);

    @Query("SELECT a FROM Answer a WHERE a.question = :question ORDER BY SIZE(a.voter) DESC, a.createDate DESC")
    Page<Answer> findByQuestionSortedByVoterSize(@Param("question") Question question, Pageable pageable);
}