package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.NoticeDTO;
import com.nh.dsh.admin.model.query.NoticeQuery;
import com.nh.dsh.admin.model.vo.ForumNoticeVO;
import com.nh.dsh.admin.model.vo.NoticeVO;
import com.nh.dsh.admin.security.cache.TokenStoreCache;
import com.nh.dsh.admin.service.NoticeService;
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
 * @create : 2024-06-20 17:30
 **/

@Tag(name = "通知公告管理")
@AllArgsConstructor
@RestController
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final TokenStoreCache tokenStoreCache;

    @PostMapping("page")
    @PreAuthorize("hasAuthority('sys:notice:view')")
    public Result<PageResult<NoticeVO>> page(@RequestBody NoticeQuery query) {
        return Result.ok(noticeService.page(query));
    }

    @PostMapping("pageByForum")
    @PreAuthorize("hasAuthority('sys:notice:view')")
    public Result<PageResult<ForumNoticeVO>> pageByForum(@RequestBody NoticeQuery query) {
        return Result.ok(noticeService.forumPage(query));
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:notice:add')")
    public Result<String> save(@RequestBody @Valid NoticeDTO dto, @RequestHeader("Authorization") String authorization) {
        dto.setAdminId(tokenStoreCache.getUser(authorization).getPkId());
        noticeService.save(dto);
        return Result.ok();
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('sys:notice:edit')")
    public Result<String> update(@RequestBody @Valid NoticeDTO dto) {
        noticeService.update(dto);
        return Result.ok();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('sys:notice:remove')")
    public Result<String> delete(@RequestBody List<Integer> ids) {
        noticeService.deleteInBatch(ids);
        return Result.ok();
    }
}
