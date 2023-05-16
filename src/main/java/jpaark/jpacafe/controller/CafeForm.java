package jpaark.jpacafe.controller;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Getter @Setter
public class CafeForm {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotBlank(message = "정보는 필수입니다.")
    private String info;
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickName;
}
