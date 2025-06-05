package com.nh.dsh.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.client.model.entity.BookExchangeEntity;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-30 17:43
 **/

public interface BookExchangeService extends IService<BookExchangeEntity> {

    String exchangeBookLink(String cardNum, String openId, String verifyCode);

    String checkVerifyCodeAvailable(String cardNum, String openId);
    String generateCardNum(int id);
}
