package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.convert.CategoryConvert;
import com.nh.dsh.admin.model.dto.CategoryDTO;
import com.nh.dsh.admin.model.query.CategoryQuery;
import com.nh.dsh.admin.model.vo.Category;
import com.nh.dsh.admin.model.vo.CategoryTreeNode;
import com.nh.dsh.admin.model.vo.CategoryVO;
import com.nh.dsh.admin.service.CategoryService;
import com.nh.dsh.admin.utils.JsonParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:49
 **/

@Tag(name = "分类管理接口")
@AllArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("page")
    @PreAuthorize("hasAuthority('sys:category:view')")
    public Result<List<CategoryTreeNode>> page(@RequestBody CategoryQuery query) {
        // categoryService.batchInsert();
        return categoryService.page(query);
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:category:add')")
    public Result<String> save(@RequestBody @Valid CategoryDTO dto) {
        categoryService.save(dto);
        return Result.ok();
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('sys:category:edit')")
    public Result<String> update(@RequestBody @Valid CategoryDTO dto) {
        categoryService.update(dto);
        return Result.ok();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('sys:category:remove')")
    public Result<String> delete(@RequestBody List<Integer> ids) {
        categoryService.deleteInBatch(ids);
        return Result.ok();
    }

    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:category:view')")
    public Result<List<CategoryVO>> list() {
        return Result.ok(CategoryConvert.INSTANCE.convertList(categoryService.list()));
    }

    @GetMapping("/tree")
    public void saveTree() {
        categoryService.batchInsert();
    }
}
