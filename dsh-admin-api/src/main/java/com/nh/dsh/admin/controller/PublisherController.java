package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.convert.PublisherConvert;
import com.nh.dsh.admin.model.dto.PublisherDTO;
import com.nh.dsh.admin.model.query.PublisherQuery;
import com.nh.dsh.admin.model.vo.PublisherVO;
import com.nh.dsh.admin.service.PublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:49
 **/

@Tag(name = "出版社管理接口")
@AllArgsConstructor
@RestController
@RequestMapping("/publisher")
public class PublisherController {
    private final PublisherService publisherService;

    @PostMapping("page")
    @PreAuthorize("hasAuthority('sys:publisher:view')")
    public Result<PageResult<PublisherVO>> page(@RequestBody PublisherQuery query) {
        return Result.ok(publisherService.page(query));
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:publisher:add')")
    public Result<String> save(@RequestBody @Valid PublisherDTO dto) {
        publisherService.save(dto);
        return Result.ok();
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('sys:publisher:edit')")
    public Result<String> update(@RequestBody @Valid PublisherDTO dto) {
        publisherService.update(dto);
        return Result.ok();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('sys:publisher:remove')")
    public Result<String> delete(@RequestBody List<Integer> ids) {
        publisherService.deleteInBatch(ids);
        return Result.ok();
    }

    @GetMapping("list")
    // @PreAuthorize("hasAuthority('sys:publisher:view')")
    public Result<List<PublisherVO>> list() {
        return Result.ok(PublisherConvert.INSTANCE.convertList(publisherService.list()));
    }
}
