package jpaark.jpacafe.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryForm {
    @NotBlank(message = "카테고리명은 필수입니다.")
    private String name;
}
