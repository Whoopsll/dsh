package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.security.utils.TokenUtils;
import com.nh.dsh.admin.service.AuthService;
import com.nh.dsh.admin.service.SysCaptchaService;
import com.nh.dsh.admin.model.vo.SysAccountLoginVO;
import com.nh.dsh.admin.model.vo.SysCaptchaVO;
import com.nh.dsh.admin.model.vo.SysTokenVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "认证管理")
@RestController
@RequestMapping("sys/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final SysCaptchaService sysCaptchaService;

    @PostMapping("login")
    @Operation(summary = "账号密码登录")
    public Result<SysTokenVO> login(@RequestBody SysAccountLoginVO login) {
        return Result.ok(authService.loginByAccount(login));
    }

    @PostMapping("captcha")
    @Operation(summary = "图片验证码")
    public Result<SysCaptchaVO> captcha() {
        SysCaptchaVO captchaVO = sysCaptchaService.generate();

        return Result.ok(captchaVO);
    }

    @PostMapping("logout")
    @Operation(summary = "退出")
    public Result<String> logout(HttpServletRequest request) {
        authService.logout(TokenUtils.getAccessToken(request));

        return Result.ok();
    }

}
