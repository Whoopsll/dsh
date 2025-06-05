package com.nh.dsh.admin.service;

import com.nh.dsh.admin.model.vo.SysAccountLoginVO;
import com.nh.dsh.admin.model.vo.SysTokenVO;

public interface AuthService {

    /**
     * 使用账号密码登录
     * @param params
     * @return
     */
    SysTokenVO loginByAccount(SysAccountLoginVO params);

    /**
     * 退出登录
     *
     * @param accessToken accessToken
     */
    void logout(String accessToken);
}
