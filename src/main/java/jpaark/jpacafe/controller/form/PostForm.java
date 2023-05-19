package jpaark.jpacafe.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostForm {
    @NotBlank(message = "제목을 작성해주세요.")
    private String title;
    @NotBlank(message = "본문을 작성해주세요.")
    private String content;
    private String category;
}
