package com.mysite.sbb.boundedContext.comment.service;

import com.mysite.sbb.boundedContext.answer.entity.Answer;
import com.mysite.sbb.boundedContext.comment.entity.Comment;
import com.mysite.sbb.boundedContext.comment.repository.CommentRepository;
import com.mysite.sbb.boundedContext.question.entity.Question;
import com.mysite.sbb.user.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public Comment createByQuestion(String content, Boolean secret, SiteUser user, Question question) {
        Comment newComment = Comment.builder()
                .content(content)
                .writer(user)
                .question(question)
                .secret(secret)
                .build();

        //댓글 생성
        commentRepository.save(newComment);

        return newComment;
    }

    //마지막 페이지 번호 가져오기
    public int getLastPageNumberByQuestion(Question question) {
        int commentCount = commentRepository.countByQuestion(question);
        int pageSize = 5; // 페이지 당 댓글 수 (조정 가능)
        int lastPageNumber = (int) Math.ceil((double) commentCount / pageSize);

        //스프링 0페이지 부터 시작하니 1 빼주기
        //단 댓글이 1개도 없던 상황일 경우, 댓글이 달린 직후에는 0일테니 -1하면 음수값이 나온다.
        //따라서 음수이면 0을 반환하도록 수정
        if (lastPageNumber - 1 < 0)
            return 0;
        else
            return lastPageNumber - 1;
    }

    @Transactional
    public Comment createByAnswer(String content, Boolean secret, SiteUser user, Answer answer) {
        Comment newcomment = Comment.builder()
                .content(content)
                .writer(user)
                .answer(answer)
                .secret(secret)
                .build();
        //댓글 생성
        commentRepository.save(newcomment);

        return newcomment;
    }

    public int getLastPageNumberByAnswer(Answer answer) {
        int commentCount = commentRepository.countByAnswer(answer);
        int pageSize = 10; //페이지 당 댓글 수 (조절 가능)
        int lastPageNumber = (int) Math.ceil((double) commentCount / pageSize);

        //스프링 0페이지 부터 시작하니 1 빼주기
        //단 댓글이 1개도 없던 상황일 경우, 댓글이 달린 직후에는 0일테니 -1하면 음수값이 나온다.
        //따라서 음수이면 0을 반환하도록 수정
        if (lastPageNumber - 1 < 0)
            return 0;
        else
            return lastPageNumber - 1;
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    @Transactional
    public Comment createReplyCommentByQuestion(String commentContents, Boolean secret, SiteUser user, Question question, Comment parent) {
        {
            Comment newComment = Comment.builder()
                    .content(commentContents)
                    .writer(user)
                    .question(question)
                    .secret(secret)
                    .parent(parent)
                    .build();
            // 대댓글 저장
            commentRepository.save(newComment);
            return newComment;
        }
    }

    @Transactional
    public Comment createReplyCommentByAnswer(String commentContents, Boolean secret, SiteUser user, Answer answer,
                                              Comment parent) {
        Comment newComment = Comment.builder()
                .content(commentContents)
                .writer(user)
                .answer(answer)
                .secret(secret)
                .parent(parent)
                .build();
        // 대댓글 저장
        commentRepository.save(newComment);
        return newComment;
    }

    public Page<Comment> getCommentPageByQuestion(int page, Question question) {
        Pageable pageable = PageRequest.of(page, 5); // 페이지네이션 정보
        return commentRepository.findAllByQuestion(question, pageable);
    }

    public Page<Comment> getCommentPageByAnswer(int page, Answer answer) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지네이션 정보
        return commentRepository.findAllByAnswer(answer, pageable);
    }

    @Transactional
    public void modify(Comment comment, String content, Boolean secret) {
        Comment mComment = comment.toBuilder()
                .content(content)
                .secret(secret)
                .build();
        commentRepository.save(mComment);
    }

    @Transactional
    public void delete(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        if (comment.getChildren().size() != 0) {
            // 자식이 있으면 삭제 상태만 변경
            comment.deleteParent();
        } else { // 자식이 없다 -> 대댓글이 없다 -> 객체 그냥 삭제해도 된다.
            // 삭제 가능한 조상 댓글을 구해서 삭제
            // ex) 할아버지 - 아버지 - 대댓글, 3자라 했을 때 대댓글 입장에서 자식이 없으니 삭제 가능
            // => 삭제하면 아버지도 삭제 가능 => 할아버지도 삭제 가능하니 이런식으로 조상 찾기 메서드
            Comment tmp = getDeletableAncestorComment(comment);
            commentRepository.delete(tmp);
        }
    }

    @Transactional
    public Comment getDeletableAncestorComment(Comment comment) {
        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if (parent != null && parent.getChildren().size() == 1 && parent.isDeleted() == true) {
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            // 삭제가능 댓글 -> 만일 댓글의 조상(대댓글의 입장에서 할아버지 댓글)도 해당 댓글 삭제 시 삭제 가능한지 확인
            // 삭제 -> Cascade 옵션으로 가장 부모만 삭제 해도 자식들도 다 삭제 가능

            // Ajax로 비동기로 리스트 가져오기에, 대댓글 1개인거 삭제할 때 연관관계 삭제하고 부모 댓글 삭제하기 필요
            // 컨트롤러가 아닌 서비스의 삭제에서 처리해주는 이유는 연관관계를 삭제해주면 parent를 구할 수 없기에 여기서 끊어줘야 함
            // 연관관계만 끊어주면 orphanRemoval 옵션으로 자식 객체는 삭제되니 부모를 삭제 대상으로 넘기면 됨
            parent.getChildren().remove(comment);

            return getDeletableAncestorComment(parent);
        }

        return comment;
    }
    public int getPageNumberByQuestion(Question question, Comment comment, int pageSize) {
        List<Comment> commentList = question.getComments();
        int index = commentList.indexOf(comment);

        if (index == -1) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        }

        return index / pageSize;
    }
}
