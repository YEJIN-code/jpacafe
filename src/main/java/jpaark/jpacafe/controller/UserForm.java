package jpaark.jpacafe.controller;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter @Setter
public class UserForm {

    @NotBlank(message = "ID는 필수입니다.")
    private String id;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birthDate;
}
