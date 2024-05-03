package com.mysite.sbb.boundedContext.comment.entity;

import com.mysite.sbb.boundedContext.answer.entity.Answer;
import com.mysite.sbb.boundedContext.question.entity.Question;
import com.mysite.sbb.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder(toBuilder = true)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private SiteUser writer;

        @ManyToOne
        private Question question;

        @ManyToOne
        private Answer answer;

        @ToString.Exclude
        @ManyToOne
        private Comment parent;

        @OneToMany(mappedBy = "parent", orphanRemoval = true)
        @ToString.Exclude
        @Builder.Default // 빌더패턴 리스트시 초기화
        private List<Comment> children = new ArrayList<>();

        private String content;

        @CreatedDate
        private LocalDateTime createDate;

        @LastModifiedDate
        private LocalDateTime modifyDate;

        @Builder.Default
        private Boolean secret = false;

        //삭제 여부 나타내는 속성 추가
        @Builder.Default
        private Boolean deleted = false;

        @PrePersist
        public void prePersist() {
                this.modifyDate = null;
        }

        public void deleteParent() {
                deleted = true;
        }

        //타임리프에서 비밀 댓글이면 댓글의 내용이 안보이게 하기 위함
        public boolean isSecret() {
                return this.secret == true;
        }

        //타임리프에서 삭제 댓글이면 댓글의 내용이 안보이게 하기 위함
        public boolean isDeleted() {
                return this.deleted == true;
        }
}
