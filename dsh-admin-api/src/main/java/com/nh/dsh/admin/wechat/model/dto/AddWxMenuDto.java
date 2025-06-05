package com.nh.dsh.admin.wechat.model.dto;

import com.nh.dsh.admin.wechat.model.entity.WxMenuModel;
import lombok.Data;

import java.util.List;

@Data
public class AddWxMenuDto {
    private List<WxMenuModel> datas; // 存入数据库的菜单接口
    private String menuBody; // 提交到微信公众号接口的menu参数
    private int configId;//配置id
}
