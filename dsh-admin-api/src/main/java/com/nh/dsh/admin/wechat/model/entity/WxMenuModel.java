package com.nh.dsh.admin.wechat.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.nh.dsh.admin.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("t_wx_menu")
@Schema(description = "微信菜单表")
public class WxMenuModel extends BaseEntity {

    @Schema(name = "菜单的key")
    private String keyId = "";
    @Schema(name = "菜单类型，view-网页，click-点击事件，min-小程序")
    private String type = "click";
    @Schema(name = "跳转的网页、小程序链接")
    private String url = "";
    @Schema(name = "父类id")
    private String parentKey = "";
    @Schema(name = "微信公众号的appid")
    private String appId ="";
    @Schema(name = "回复内容，主要用于回复文本和图片")
    private String replyContent = "";
    @Schema(name = "菜单名称")
    private String name = "";
    @Schema(name = "回复类型")
    private Integer replyType = -1;
    @Schema(name = "图片id")
    private String mediaId = "";

}
