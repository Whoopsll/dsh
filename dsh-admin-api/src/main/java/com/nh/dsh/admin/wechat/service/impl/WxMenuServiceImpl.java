package com.nh.dsh.admin.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.mapper.ConfigMapper;
import com.nh.dsh.admin.mapper.WxMenuMapper;
import com.nh.dsh.admin.model.entity.ConfigEntity;
import com.nh.dsh.admin.utils.WxUtils;
import com.nh.dsh.admin.wechat.model.dto.AddWxMenuDto;
import com.nh.dsh.admin.wechat.model.entity.WxMenuModel;
import com.nh.dsh.admin.wechat.model.vo.WxMenuVo;
import com.nh.dsh.admin.wechat.service.WxMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WxMenuServiceImpl extends BaseServiceImpl<WxMenuMapper, WxMenuModel> implements WxMenuService{
    private final ConfigMapper configMapper;

    @Override
    public Result batchInsertMenuInfo(AddWxMenuDto dto) {
        try {
            List<String> keys = new ArrayList<>();
            List<WxMenuModel> datas = dto.getDatas();
            String menuBody = dto.getMenuBody();
            int configId = dto.getConfigId();
            // 获取配置信息
            ConfigEntity entity = configMapper.selectById(configId);
            //按理说需要对secret解密
            String secret = entity.getPassword();
            // 添加到微信公众号菜单
            boolean customMenuResult = WxUtils.createCustomMenu(configId, entity.getAccount(), entity.getPassword(), menuBody);
            if(customMenuResult) {
                // 创建成功后，往数据库写入新增菜单内容
                List<WxMenuModel> entities = datas.stream().map(item -> {
                    item.setCreateTime(new Date());
                    item.setDeleteFlag(0);
                    keys.add(item.getKeyId());
                    return item;
                }).collect(Collectors.toList());
                // 直接删除之前新增的记录
                int i = getBaseMapper().deleteBatchByKey(keys);
                // 批量新增微信菜单
                boolean result = saveBatch(entities);
                if (result) {
                    return Result.ok();
                }
                return Result.error("批量新增异常");
            }
            return Result.error("菜单创建异常");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerException("菜单创建异常");
        }
    }


    @Override
    public Result getCustomMenu(int configId) {
        ConfigEntity entity = configMapper.selectById(configId);
        if (entity == null) {
            throw new ServerException("配置不存在");
        }
        // 获取微信公众号自定义菜单
        JSONObject customMenu = WxUtils.getCustomMenu(entity.getAccount(),entity.getPassword(), configId);
        JSONArray menus = null;
        if (customMenu != null) {
            JSONObject menu = customMenu.getJSONObject("menu");
            if (menu != null) {
                menus = customMenu.getJSONObject("menu").getJSONArray("button");
            } else {
                return Result.ok(new ArrayList<>());
            }
        }
        Gson gson = new Gson();
        //定义转换的类型
        Type menuListType = new TypeToken<List<WxMenuVo>>() {}.getType();
        // 将JSON 转换为 List<WxMenuVo>
        List<WxMenuVo> menuVos = gson.fromJson(String.valueOf(menus), menuListType);
        // 构建返回内容
        List<WxMenuVo> result = menuVos.stream().map(menu -> {
            String key = menu.getKey();
            menu = getMenuDetail(key, menu);
            //提取子集内容
            List<WxMenuVo> subButtons = menu.getSub_button();
            if(!subButtons.isEmpty()) {
                AtomicReference<String> parentKey = new AtomicReference<>(""); // 当子类不为空的时候，父类key没有，故需要取子集内查询父类key
                // 处理子集内容
                subButtons.forEach(subMenu -> {
                    String subKey = subMenu.getKey();
                    subMenu = getMenuDetail(subKey, subMenu);
                    parentKey.set(subMenu.getParentKey());
                });
                menu.setKey(parentKey.get());
            }
            return menu;
        }).collect(Collectors.toList());
        //遍历menu，构建菜单的返回类型
        return Result.ok(result);
    }

    // 获取menu详情
    private WxMenuVo getMenuDetail(String key, WxMenuVo menu) {
        List<WxMenuModel> wxMenuModels = getBaseMapper().selectList(new QueryWrapper<WxMenuModel>().eq("key_id", key));
        if (!wxMenuModels.isEmpty()) {
            //如果不为空，获取第一个
            WxMenuModel wxMenuModel = wxMenuModels.get(0);
            //判断菜单的url，若为空串，则为发送信息类型，否则为网页跳转
            if (menu.getType().equalsIgnoreCase("CLICK")) {
                // 发送信息类型,判断是文本还是图片,0-文本，1-图片
                if (wxMenuModel.getReplyType() == 0) {
                    menu.setTextContent(wxMenuModel.getReplyContent());
                    menu.setPictureUrl("");
                    menu.setMediaId("");
                } else {
                    // 截取
                    menu.setPictureUrl(wxMenuModel.getReplyContent());
                    menu.setMediaId(wxMenuModel.getMediaId());
                    menu.setTextContent("");
                }
            }
            menu.setReplyType(wxMenuModel.getReplyType());
            menu.setParentKey(wxMenuModel.getParentKey());
        }
        return menu;
    }

}
