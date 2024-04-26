package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.answer.AnswerService;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;

    @GetMapping("/sbb")
    @ResponseBody
    public String index() {
        return "안녕하세요 sbb에 오신 것을 환영합니다.";
    }
    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }

    @GetMapping("/test")
    public String test(Model model, AnswerForm answerForm) {

        Question question = this.questionService.getQuestion(306);
        Page<Answer> answerPaging = this.answerService.getListSortedByCreateDate(question, 1);

        model.addAttribute("answerPaging", answerPaging);
        model.addAttribute("question", question);

        return "test";
    }
}
