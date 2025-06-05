package com.nh.dsh.client.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nh.dsh.client.common.cache.RequestContext;
import com.nh.dsh.client.common.cache.TokenStoreCache;
import com.nh.dsh.client.common.exception.ErrorCode;
import com.nh.dsh.client.common.exception.ServerException;
import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.enums.CommonStatusEnum;
import com.nh.dsh.client.mapper.UserMapper;
import com.nh.dsh.client.model.dto.WxMiniInfoDto;
import com.nh.dsh.client.model.entity.UserEntity;
import com.nh.dsh.client.model.vo.UserLoginVO;
import com.nh.dsh.client.model.vo.WxInfoVo;
import com.nh.dsh.client.service.AuthService;
import com.nh.dsh.client.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-06-28 10:09
 **/

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final TokenStoreCache tokenStoreCache;

    // final static String APP_ID = "wx731630b450248376";
    // final static String APP_SECRET = "65f6d70befb283d461486a63922652e6";
    final static String APP_ID = "wx0055deef26d951ff";
    final static String APP_SECRET = "02cc1aef64542aba6e45e8c2753c4ba4";
    final static String WX_MINI_APP_ID = "wx0896af4b9fcc8d67";
    final static String WX_MINI_APP_SECRET = "31925eb365eb669629c86cd927a6f4f8";

    @Override
    public boolean checkUserEnabled(Integer pkId) {
        return userMapper.selectById(pkId).getDeleteFlag().equals(CommonStatusEnum.NO.getStatus());
    }

    @Override
    public UserLoginVO login(String code) {
        try {
            log.info("微信登录: {}", code);
            String urlForAK = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=" + APP_ID +
                    "&secret=" + APP_SECRET +
                    "&code=" + code +
                    "&grant_type=authorization_code";
            RestTemplate restTemplate = new RestTemplate();
            String accessData = restTemplate.getForObject(urlForAK, String.class);
            log.info("accessData: {}", accessData);
            JSONObject accessObj = JSONObject.parseObject(accessData);
            String openid = accessObj.getString("openid");
            String unionId = accessObj.getString("unionid");
            String accessToken = accessObj.getString("access_token");
            String urlForUserInfo = "https://api.weixin.qq.com/sns/userinfo?" +
                    "access_token=" + accessToken +
                    "&openid=" + openid +
                    "&lang=zh_CN";
            StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
            stringConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_PLAIN));
            // 将StringHttpMessageConverter添加到RestTemplate的消息转换器列表中
            restTemplate.getMessageConverters().add(0, stringConverter);
            // 创建HttpHeaders对象，设置Accept头部的值为"text/plain;charset=UTF-8"
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
            headers.set(HttpHeaders.ACCEPT_CHARSET, "UTF-8");
            String userInfoStr = restTemplate.getForObject(urlForUserInfo, String.class);
            log.info("userInfoStr: {}", userInfoStr);
            JSONObject userInfo = JSON.parseObject(userInfoStr);
            String nickname = userInfo.getString("nickname");
            userInfo.getString("sex");
            String headimgurl = userInfo.getString("headimgurl");

            LambdaQueryWrapper<UserEntity> userQueryWrapper = new LambdaQueryWrapper<>();
            //根据unionId获取用户信息
            // userQueryWrapper.eq(UserEntity::getUnionId, unionId).eq(UserEntity::getDeleteFlag, CommonStatusEnum.NO);
            userQueryWrapper.eq(UserEntity::getWxId, openid).eq(UserEntity::getDeleteFlag, CommonStatusEnum.NO);
            UserEntity userEntity = userMapper.selectOne(userQueryWrapper);
            UserEntity user;
            if (userEntity == null) {
                //判断用户的微信网页openId是否存在，若不存在更新openId
                // if (!StringUtils.hasText(userEntity.getWxId())) {
                //     userEntity.setWxId(openid);
                //     userMapper.updateById(userEntity);
                // }
                user = new UserEntity();
                user.setWxId(openid);
                user.setWxName(nickname);

                user.setAvatar(headimgurl);
                userMapper.insert(user);
            } else {
                user = userEntity;
            }
            log.info("user: {}", JSON.toJSONString(user));
            String token = JwtUtil.createToken(user.getId());
            UserLoginVO userLoginVO = new UserLoginVO();
            userLoginVO.setAccessToken(token);
            userLoginVO.setWxOpenId(openid);
            userLoginVO.setPkId(user.getId());
            tokenStoreCache.saveUser(token, userLoginVO);
            return userLoginVO;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public void logout(String token) {
        Integer userId = RequestContext.getUserId();
        tokenStoreCache.deleteUserById(userId);
        tokenStoreCache.deleteUser(token);
    }

    @Override
    public String wxVerify(String code) {
        log.info("微信校验: {}", code);
        String urlForAK = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + APP_ID +
                "&secret=" + APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String accessData = restTemplate.getForObject(urlForAK, String.class);
        log.info("accessData: {}", accessData);
        JSONObject accessObj = JSONObject.parseObject(accessData);
        return accessObj.getString("openid");
    }

    @Override
    public Result wxMiniLogin(String code) {
        try {
            // 1. 通过 code 请求微信服务器，获取 openid 和 session_key
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + WX_MINI_APP_ID
                    + "&secret=" + WX_MINI_APP_SECRET + "&js_code=" + code + "&grant_type=authorization_code";

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("返回内容是====");
            System.out.println(response);
            //解析响应内容，转为map类型
            Map responseResult = JSON.parseObject(response, Map.class);
            if (responseResult == null || responseResult.get("openid") == null) {
                throw new ServerException("微信登录失败");
            }
            String openid = (String) responseResult.get("openid");
            String sessionKey = (String) responseResult.get("session_key");
            // String unionId = (String) responseResult.get("unionid");
            String unionId = "sdfjalsdflasdjlfajs;ldfjl;as";
            //判断数据库是否存在该用户
            LambdaQueryWrapper<UserEntity> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(UserEntity::getUnionId, unionId).eq(UserEntity::getDeleteFlag, CommonStatusEnum.NO);
            UserEntity userEntity = userMapper.selectOne(userQueryWrapper);
            //如果用户已存在，报异常信息
            if (userEntity != null) {
                //判断用户的小程序openId是否存在，若不存在，则更新用户小程序的openId
                if(!StringUtils.hasText(userEntity.getOpenIdMp())) {
                    //更新用户的微信小程序openId
                    userEntity.setOpenIdMp(openid);
                    userMapper.updateById(userEntity);
                }
                //返回用户信息
                UserLoginVO userLoginVO = new UserLoginVO();
                BeanUtils.copyProperties(userEntity, userLoginVO);
                userLoginVO.setPkId(userEntity.getId());
                String token = JwtUtil.createToken(userEntity.getId());
                userLoginVO.setAccessToken(token);
                //将用户信息存入redis
                tokenStoreCache.saveUser(token, userLoginVO);
                return Result.ok(userLoginVO);
            } else {
                //返回微信小程序信息，用户需要手动初始化信息
                WxInfoVo infoVo = new WxInfoVo();
                infoVo.setUnionId(unionId);
                infoVo.setWxOpenId(openid);
                infoVo.setSessionKey(sessionKey);
                return Result.ok(infoVo);
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public UserLoginVO initUserInfo(WxMiniInfoDto wxMiniInfoDto) {
        try {
            UserLoginVO userLoginVO = new UserLoginVO();
            //构建用户信息
            UserEntity userEntity = new UserEntity();
            userEntity.setAvatar(wxMiniInfoDto.getAvatar());
            userEntity.setWxName(wxMiniInfoDto.getNickname());
            userEntity.setOpenIdMp(wxMiniInfoDto.getWxOpenId());
            userEntity.setUnionId(wxMiniInfoDto.getUnionId());
            userEntity.setDeleteFlag(CommonStatusEnum.NO.getStatus());
            //新增信息
            int result = userMapper.insert(userEntity);
            if (result != 0) {
                //新增成功，返回用户信息
                BeanUtils.copyProperties(userEntity, userLoginVO);
                userLoginVO.setPkId(userEntity.getId());
                String token = JwtUtil.createToken(userEntity.getId());
                userLoginVO.setAccessToken(token);
                //登录成功后，将用户信息存入redis
                tokenStoreCache.saveUser(token, userLoginVO);
                return userLoginVO;
            } else {
                throw new ServerException("数据新增异常");
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
