package com.mysite.sbb.boundedContext.comment.form;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentForm {

    private Long id;

    @NotBlank(message = "내용은 필수항목입니다.")
    private String commentContents;

    private Integer questionId;

    private Integer answerId;

    private Boolean secret = false;

    private Long parentId;

    private Long commentWriter;
}
