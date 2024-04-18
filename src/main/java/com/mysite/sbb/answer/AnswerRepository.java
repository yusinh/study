package com.mysite.sbb.answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mysite.sbb.question.Question;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findByQuestion(Question question, Pageable pageable);

    List<Answer> findByQuestionIdOrderByCreateDateDesc(Integer questionId);

    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId ORDER BY SIZE(a.voter) DESC, a.createDate DESC")
    List<Answer> findByQuestionIdOrderByVotesDesc(Integer questionId);
}
