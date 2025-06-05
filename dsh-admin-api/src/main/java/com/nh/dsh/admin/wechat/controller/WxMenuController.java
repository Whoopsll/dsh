package com.nh.dsh.admin.wechat.controller;

import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.wechat.model.dto.AddWxMenuDto;
import com.nh.dsh.admin.wechat.model.entity.WxMenuModel;
import com.nh.dsh.admin.wechat.service.WxMenuService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wechat/menu")
@AllArgsConstructor
public class WxMenuController {

    private final WxMenuService wxMenuService;

    @Schema(name = "批量新增")
    @PostMapping("/add")
    public Result addInfo(@RequestBody AddWxMenuDto dto) {
        return wxMenuService.batchInsertMenuInfo(dto);
    }


    @Schema(name = "获取自定义菜单列表")
    @PostMapping("/customMenu/{id}")
    Result getCustomMenuByConfigId(@PathVariable("id") int id) {
        return wxMenuService.getCustomMenu(id);
    }
}
