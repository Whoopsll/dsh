package com.nh.dsh.client.service;


import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.model.dto.WxMiniInfoDto;
import com.nh.dsh.client.model.vo.UserLoginVO;
import com.nh.dsh.client.model.vo.WxInfoVo;

public interface AuthService {

    boolean checkUserEnabled(Integer pkId);

    UserLoginVO login(String code);

    void logout(String token);

    String wxVerify(String code);

    //微信小程序登录
    Result wxMiniLogin(String code);

    //微信小程序用户信息新增
    UserLoginVO initUserInfo(WxMiniInfoDto wxMiniInfoDto);
}
