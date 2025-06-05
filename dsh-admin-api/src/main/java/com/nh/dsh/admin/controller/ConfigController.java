package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.ConfigDto;
import com.nh.dsh.admin.model.query.ConfigQuery;
import com.nh.dsh.admin.model.vo.ConfigVo;
import com.nh.dsh.admin.service.ConfigService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("config")
@AllArgsConstructor
public class ConfigController {
    private final ConfigService configService;

    @Schema(description = "获取社区公众号信息")
    @PostMapping("/list")
    public Result<PageResult<ConfigVo>> queryConfigList(@RequestBody ConfigQuery query) {
        return Result.ok(configService.getWxConfigByPublisher(query));
    }

    @Schema(description = "修改公众号信息")
    @PostMapping("/modify")
    public Result modifyConfig(@RequestBody ConfigDto configDto) {
        return configService.modifyConfig(configDto);
    }

    @Schema(description = "删除公众号信息")
    @PostMapping("/delete")
    public Result deleteConfig(@RequestBody ConfigDto configDto) {
        return configService.deleteConfig(configDto);
    }

    @Schema(description = "获取配置详情")
    @PostMapping("/get/{id}")
    public Result getConfigById(@PathVariable(name = "id") int id) {
        return configService.getConfigById(id);
    }
}
