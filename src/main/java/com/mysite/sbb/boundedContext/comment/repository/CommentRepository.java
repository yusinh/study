package com.mysite.sbb.boundedContext.comment.repository;

import com.mysite.sbb.boundedContext.answer.entity.Answer;
import com.mysite.sbb.boundedContext.comment.entity.Comment;
import com.mysite.sbb.boundedContext.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByQuestion(Question question);

    int countByAnswer(Answer answer);

    Page<Comment> findAllByQuestion(Question question, Pageable pageable);

    Page<Comment> findAllByAnswer(Answer answer, Pageable pageable);
}
