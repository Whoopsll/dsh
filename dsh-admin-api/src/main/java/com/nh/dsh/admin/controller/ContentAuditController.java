package com.nh.dsh.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.mapper.AuditLogMapper;
import com.nh.dsh.admin.model.dto.AuditDto;
import com.nh.dsh.admin.model.dto.ContentAuditDto;
import com.nh.dsh.admin.model.entity.AuditEntity;
import com.nh.dsh.admin.model.entity.AuditLogEntity;
import com.nh.dsh.admin.model.entity.ContentAuditEntity;
import com.nh.dsh.admin.model.query.AuditQuery;
import com.nh.dsh.admin.model.query.ContentAuditQuery;
import com.nh.dsh.admin.model.vo.AuditVo;
import com.nh.dsh.admin.service.AuditService;
import com.nh.dsh.admin.service.ContentAuditService;
import com.nh.dsh.admin.utils.PdfGeneratorUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "内容审核接口")
@RequestMapping("/contentAudit")
public class ContentAuditController {
    private final ContentAuditService contentAuditService;
    private final AuditLogMapper auditLogMapper;

    @PostMapping("/save")
    // @PreAuthorize("hasAuthority('sys:audit:add')")
    public Result saveAudit(@RequestBody ContentAuditDto auditDto) {
        return contentAuditService.addInfo(auditDto);
    }

    @PostMapping("/page")
    // @PreAuthorize("hasAuthority('sys:audit:view')")
    public Result<PageResult> queryListByPage(@RequestBody ContentAuditQuery query) {
        return Result.ok(contentAuditService.queryListByPage(query));
    }

    @PostMapping("/detail")
    // @PreAuthorize("hasAuthority('sys:audit:view')")
    public Result getDetailById(@RequestParam("taskId") int taskId) {
        return contentAuditService.getAuditDetail(taskId);
    }

    @PostMapping("/del")
    // @PreAuthorize("hasAuthority('sys:audit:delete')")
    public Result deleteAudit(@RequestBody List<Integer> ids) {
        return Result.ok(contentAuditService.deleteInfo(ids));
    }

    @GetMapping("/download")
    @Operation(summary = "下载测试报告")
    public void download(HttpServletResponse response, @RequestParam("auditId") int auditId) {
        ContentAuditEntity auditEntity = contentAuditService.getById(auditId);
        AuditLogEntity auditLogEntity = auditLogMapper.selectOne(new QueryWrapper<AuditLogEntity>().eq("delete_flag", 0).eq("audit_id", auditId));
        PdfGeneratorUtils.downloadOtherAuditReport(response, auditLogEntity.getContent(), auditEntity.getName());
    }
}
