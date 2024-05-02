package com.mysite.sbb.boundedContext.answer.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mysite.sbb.boundedContext.comment.entity.Comment;
import com.mysite.sbb.boundedContext.question.entity.Question;
import com.mysite.sbb.user.entity.SiteUser;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class) // + enableJpaAuditing => JPA Auditing 활성
// JPA Auditing : 시간에 대해 자동으로 값을 넣어주는 기능
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @PrePersist
    public void prePersist() {
        this.modifyDate = null; // 객체 생성 시 처음에 수정일 null값으로 설정해줌
    }

    @ManyToOne // 부모 : question
    private Question question;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    private Set<SiteUser> voters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "answer", cascade = {CascadeType.REMOVE})
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();
}