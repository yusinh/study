package com.mysite.sbb.boundedContext.answer.service;

import java.util.Optional;

import com.mysite.sbb.boundedContext.answer.entity.Answer;
import com.mysite.sbb.boundedContext.answer.repository.AnswerRepository;
import com.mysite.sbb.boundedContext.question.entity.Question;
import com.mysite.sbb.base.exception.DataNotFoundException;
import com.mysite.sbb.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;

    @Transactional
    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setQuestion(question);
        answer.setAuthor(author);
        answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    @Transactional
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answerRepository.save(answer);
    }

    @Transactional
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    @Transactional
    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoters().add(siteUser);
        answerRepository.save(answer);
    }

    public Page<Answer> getAnswerPage(Question question, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        return answerRepository.findAllByQuestion(question, pageable);
    }
}