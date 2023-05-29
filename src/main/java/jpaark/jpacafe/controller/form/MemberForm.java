package jpaark.jpacafe.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberForm {
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;
}
