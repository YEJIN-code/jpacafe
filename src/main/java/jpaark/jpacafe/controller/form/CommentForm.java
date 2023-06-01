package jpaark.jpacafe.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentForm {
    @NotBlank(message = "내용을 입력하세요.")
    private String content;

}
