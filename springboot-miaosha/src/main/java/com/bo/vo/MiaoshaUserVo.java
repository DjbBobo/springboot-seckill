package com.bo.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class MiaoshaUserVo {
    @NotNull(message = "邮箱不能为空")
    @Email
    private String email;
    @NotNull(message = "密码不能为空")
    private String password;
}
