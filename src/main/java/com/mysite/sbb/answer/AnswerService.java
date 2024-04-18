package com.mysite.sbb.answer;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;


    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    @Transactional
    public void vote(Answer answer, SiteUser siteUser) {
        // 추천자 목록을 가져옵니다. Set은 중복을 허용하지 않습니다.
        Set<SiteUser> voters = answer.getVoter();
        // 새로운 추천을 추가합니다. 이미 추천한 사용자가 다시 추천을 시도하면 Set은 중복을 허용하지 않기 때문에 추가되지 않습니다.
        boolean added = voters.add(siteUser);
        // 새로운 추천이 추가되었다면 데이터베이스를 업데이트합니다.
        if (added) {
            answerRepository.save(answer);
        }
    }

    public Page<Answer> getList(Question question, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("Voter"));
        sorts.add(Sort.Order.asc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.answerRepository.findByQuestion(question, pageable);
    }
    public Page<Answer> getListSortedByVotes(Question question, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createDate"));
        return answerRepository.findByQuestionSortedByVoterSize(question, pageable);
    }

    public Page<Answer> getListSortedByCreateDate(Question question, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createDate"));
        return answerRepository.findByQuestion(question, pageable);
    }
}
