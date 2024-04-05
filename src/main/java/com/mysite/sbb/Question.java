package com.mysite.sbb;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // 질문 데이터의 고유 번호

    @Column(length = 200)
    private String subject; // 질문 데이터의 제목

    @Column(columnDefinition = "TEXT")
    private String content; // 질문 데이터의 내용

    private LocalDateTime createDate; // 질문 데이터를 작성한 일시

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}
