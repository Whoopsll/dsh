package com.nh.dsh.admin.service.impl;

import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.security.cache.TokenStoreCache;
import com.nh.dsh.admin.security.user.ManagerDetail;
import com.nh.dsh.admin.security.utils.TokenUtils;
import com.nh.dsh.admin.service.AuthService;
import com.nh.dsh.admin.model.vo.SysAccountLoginVO;
import com.nh.dsh.admin.model.vo.SysTokenVO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenStoreCache tokenStoreCache;
    private final AuthenticationManager authenticationManager;

    @Override
    public SysTokenVO loginByAccount(SysAccountLoginVO params) {
        Authentication authentication;
        try {
            // 用户认证
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(params.getUsername(), params.getPassword()));
        } catch (DisabledException e) {
            throw new ServerException("该账号已被禁用");
        } catch (BadCredentialsException e) {
            throw new ServerException("用户名或密码错误");
        }
        // 用户信息
        ManagerDetail user = (ManagerDetail) authentication.getPrincipal();
        // 生成 accessToken
        String accessToken = TokenUtils.generator();
        // 保存用户信息到缓存
        tokenStoreCache.saveUser(accessToken, user);

        return new SysTokenVO(accessToken);
    }

    @Override
    public void logout(String accessToken) {
        // 用户信息
        ManagerDetail manager = tokenStoreCache.getUser(accessToken);

        // 删除用户信息
        tokenStoreCache.deleteUser(accessToken);

    }

}
