package com.nh.dsh.admin.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.wechat.model.dto.AddWxMenuDto;
import com.nh.dsh.admin.wechat.model.entity.WxMenuModel;

import java.util.List;

public interface WxMenuService extends IService<WxMenuModel> {

    /**
     * 批量新增菜单信息
     * @param dto
     * @return
     */
    Result batchInsertMenuInfo(AddWxMenuDto dto);

    /**
     * 获取公众号自定义菜单
     * @param configId 配置id
     * @return
     */
    Result getCustomMenu(int configId);
}
