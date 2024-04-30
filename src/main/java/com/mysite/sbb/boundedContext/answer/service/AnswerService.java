package com.mysite.sbb.boundedContext.answer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mysite.sbb.boundedContext.answer.entity.Answer;
import com.mysite.sbb.boundedContext.answer.repository.AnswerRepository;
import com.mysite.sbb.boundedContext.question.entity.Question;
import com.mysite.sbb.base.exception.DataNotFoundException;
import com.mysite.sbb.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Answer> getAnswerPage(Question question, int page, String sort) {

        Pageable pageable;

        //최신순
        if(sort.equals("createDate")) {
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("createDate"));
            pageable = PageRequest.of(page, 10, Sort.by(sorts)); //페이지 번호, 개수
            return answerRepository.findAllByQuestion(question, pageable);
        }

        //추천순, 기본
        else {
            pageable = PageRequest.of(page, 10);
            if(sort.equals("voter"))
                //추천순 : 10개에 페이지정보만 주면 알아서
                return answerRepository.findAllByQuestionOrderByVoter(question, pageable);

            //기본
            return answerRepository.findAllByQuestion(question, pageable);
            }
        }
    }