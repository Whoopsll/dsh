package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.DictRequestDTO;
import com.nh.dsh.admin.model.entity.Dict;
import com.nh.dsh.admin.model.query.DictQuery;
import com.nh.dsh.admin.service.DictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "字典管理")
@RestController
@RequestMapping("dict")
@AllArgsConstructor
public class DictController {
    private final DictService dictService;

    @PostMapping("page")
    @Operation(summary = "字典列表")
    public Result<PageResult<Dict>> page(@RequestBody @Validated DictQuery query) {
        PageResult<Dict> page = dictService.getPage(query);
        return Result.ok(page);
    }


    @PostMapping("save")
    @Operation(summary = "新增字典")
    public Result save(@RequestBody @Validated DictRequestDTO requestDTO) {
        dictService.add(requestDTO);
        return Result.ok();
    }

    @PostMapping("edit")
    @Operation(summary = "修改字典")
    public Result edit(@RequestBody @Validated DictRequestDTO requestDTO) {
        dictService.edit(requestDTO);
        return Result.ok();
    }

    @PostMapping("remove")
    @Operation(summary = "删除字典")
    public Result remove(@RequestBody List<Integer> ids) {
        if (ids.size() == 0) {
            throw new ServerException("选择数据不能为空");
        }
        dictService.remove(ids);
        return Result.ok();
    }

}
