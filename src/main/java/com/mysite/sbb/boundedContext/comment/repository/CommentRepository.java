package com.mysite.sbb.boundedContext.comment.repository;

import com.mysite.sbb.boundedContext.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
