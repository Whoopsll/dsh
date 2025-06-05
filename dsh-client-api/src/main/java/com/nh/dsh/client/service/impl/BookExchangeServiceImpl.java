package com.nh.dsh.client.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nh.dsh.client.common.exception.ErrorCode;
import com.nh.dsh.client.common.exception.ServerException;
import com.nh.dsh.client.mapper.BookExchangeMapper;
import com.nh.dsh.client.mapper.UserMapper;
import com.nh.dsh.client.model.BaseServiceImpl;
import com.nh.dsh.client.model.entity.BookExchangeEntity;
import com.nh.dsh.client.model.entity.BookResourceEntity;
import com.nh.dsh.client.model.entity.UserEntity;
import com.nh.dsh.client.service.BookExchangeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-30 17:43
 **/

@Slf4j
@Service
@AllArgsConstructor
public class BookExchangeServiceImpl extends BaseServiceImpl<BookExchangeMapper, BookExchangeEntity> implements BookExchangeService {
    private final UserMapper userMapper;

    @Override
    public String exchangeBookLink(String cardNum, String openId, String verifyCode) {
        String keyStr = "1234567890123456";
        Long count = userMapper.selectCount(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getWxId, openId));
        if (count != 1) {
            log.error("不是本系统的用户,openId:{}", openId);
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        }
        cardNum = cardNum.replaceAll(" ", "+");
        String[] decryptStr = SecureUtil
                .aes(keyStr.getBytes())
                .decryptStr(cardNum).split("&");
        List<BookExchangeEntity> lsit = baseMapper.selectList(new QueryWrapper<BookExchangeEntity>().eq("delete_flag", 0));
        System.out.println(lsit.size());
        BookExchangeEntity bookExchange = baseMapper.selectById(decryptStr[1]);
        if (StringUtils.isBlank(verifyCode) || !bookExchange.getVerifyCode().equals(verifyCode)) {
            log.error("防伪码错误,{},查询得到结果： {}", verifyCode, bookExchange);
            throw new ServerException("防伪码错误");
        }
        // 防伪码已被使用，且不是当前用户使用
        if (bookExchange.getStatus() != 0 && !Objects.equals(bookExchange.getUserFlag(), openId)) {
            log.error("防伪码已被使用,{}", verifyCode);
            throw new ServerException("资源已被他人兑换");
        }
        // 判断用户是否兑换过其他防伪码
        return checkRepeatExchange(bookExchange, openId);
    }

    @Override
    public String checkVerifyCodeAvailable(String cardNum, String openId) {
        String keyStr = "1234567890123456";
        if (StringUtils.isBlank(cardNum)) {
            return null;
        }
        Long count = userMapper.selectCount(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getWxId, openId));
        if (count != 1) {
            log.error("不是本系统的用户,openId:{}", openId);
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        }
        cardNum = cardNum.replaceAll(" ", "+");
        String[] decryptStr = SecureUtil
                .aes(keyStr.getBytes())
                .decryptStr(cardNum).split("&");
        BookExchangeEntity bookExchange = baseMapper.selectById(decryptStr[1]);
        if (bookExchange == null) {
            return null;
        }
        // 如果有防伪码，返回空值
        if (StringUtils.isNotBlank(bookExchange.getVerifyCode())) {
            if (StringUtils.isNotBlank(bookExchange.getUserFlag())) {
                if(openId.equals(bookExchange.getUserFlag())) {
                    // 如果该用户已经兑换该资源，直接返回资源链接
                    return checkRepeatExchange(bookExchange, openId);
                } else {
                    throw new ServerException("资源已被他人兑换");
                }
            } else {
                return null;
            }
        }
        // 没有防伪码，证明不需要验证，直接返回链接
        return checkRepeatExchange(bookExchange, openId);
    }

    /**
     * 获取最终返回的书链，同时记录兑换信息
     *
     * @param bookExchange 图书交换
     * @param openId       微信 ID
     * @return {@link String }
     */
    private String checkRepeatExchange(BookExchangeEntity bookExchange, String openId) {
        // 查询本书是否被当前用户兑换过
        LambdaQueryWrapper<BookExchangeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookExchangeEntity::getId, bookExchange.getId());
        wrapper.eq(BookExchangeEntity::getUserFlag, openId);
        List<BookExchangeEntity> list = baseMapper.selectList(wrapper);
        if (!list.isEmpty()) {
            // 当前用户已经兑换过，直接返回
            return list.get(0).getBookLink();
        }
        // 如果为空，说明没有被当前用户兑换过，记录信息
        bookExchange.setStatus(1);
        bookExchange.setUserFlag(openId);
        bookExchange.setExchangeTime(new Date());
        baseMapper.updateById(bookExchange);
        return bookExchange.getBookLink();
    }


    @Override
    public String generateCardNum(int id) {
        BookExchangeEntity bookExchange = getById(id);
        String keyStr = "1234567890123456";
        String key = String.format("%s&%s", bookExchange.getBookLink(), bookExchange.getId());
        String cardNum = SecureUtil
                .aes(keyStr.getBytes())
                .encryptBase64(key);
        return cardNum;
    }

}
