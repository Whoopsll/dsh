package com.nh.dsh.client.common.interceptor;

import com.nh.dsh.client.common.cache.RequestContext;
import com.nh.dsh.client.common.cache.TokenStoreCache;
import com.nh.dsh.client.common.constant.Constant;
import com.nh.dsh.client.common.exception.ErrorCode;
import com.nh.dsh.client.common.exception.ServerException;
import com.nh.dsh.client.model.vo.UserLoginVO;
import com.nh.dsh.client.service.AuthService;
import com.nh.dsh.client.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Flobby
 */
@Slf4j
@AllArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private final TokenStoreCache tokenStoreCache;
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取token
        String accessToken = JwtUtil.getAccessToken(request);
        if (StringUtils.isBlank(accessToken)) {
            throw new ServerException(ErrorCode.UNAUTHORIZED);
        }
        // 校验token
        if (!JwtUtil.validate(accessToken)) {
            throw new ServerException(ErrorCode.UNAUTHORIZED);
        }
        // 验证用户是否可用
        UserLoginVO user = tokenStoreCache.getUser(accessToken);
        if (ObjectUtils.isEmpty(user)) {
            throw new ServerException(ErrorCode.UNAUTHORIZED);
        }
        boolean enabledFlag = authService.checkUserEnabled(user.getPkId());
        if (!enabledFlag) {
            throw new ServerException(ErrorCode.ACCOUNT_DISABLED);
        }
        RequestContext.put(Constant.USER_ID, user.getPkId());
        // RequestContext.put(Constant.USER_ID, 28);
        return true;
    }

}