package com.nh.dsh.client.controller;

import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.model.dto.WxMiniInfoDto;
import com.nh.dsh.client.model.vo.UserLoginVO;
import com.nh.dsh.client.model.vo.WxInfoVo;
import com.nh.dsh.client.service.AuthService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 15:54
 **/

@Slf4j
@Tag(name = "认证服务")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("wxLogin")
    public Result<UserLoginVO> login(@RequestParam String code) {
        return Result.ok(authService.login(code));
    }

    @Schema(description = "微信小程序登录")
    @PostMapping("wxMiniLogin")
    public Result wxMiniLogin(@RequestParam String code) {
        return authService.wxMiniLogin(code);
    }

    @Schema(description = "微信小程序初始化用户信息")
    @PostMapping("initProfile")
    public Result<UserLoginVO> initUserInfo(@RequestBody WxMiniInfoDto wxMiniInfoDto) {
        return Result.ok(authService.initUserInfo(wxMiniInfoDto));
    }

    @PostMapping("wxVerify")
    public Result<String> wxVerify(@RequestParam String code) {
        return Result.ok(authService.wxVerify(code));
    }

    @PostMapping("logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return Result.ok();
    }
}
