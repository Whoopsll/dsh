package com.nh.dsh.client.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author Flobby
 */
public interface CommonService {

    JSONObject wxLogin(String code);

}