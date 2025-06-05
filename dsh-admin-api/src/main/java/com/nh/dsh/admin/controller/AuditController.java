package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.model.Query;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.AuditDto;
import com.nh.dsh.admin.model.entity.AuditEntity;
import com.nh.dsh.admin.model.query.AuditQuery;
import com.nh.dsh.admin.model.vo.AuditVo;
import com.nh.dsh.admin.service.AuditService;
import com.nh.dsh.admin.utils.PdfGeneratorUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "审核管理接口")
@RequestMapping("/audit")
public class AuditController {
    private final AuditService auditService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:audit:add')")
    public Result saveAudit(@RequestBody AuditDto auditDto) {
        return auditService.addAudit(auditDto);
    }

    @PostMapping("/page")
    @PreAuthorize("hasAuthority('sys:audit:view')")
    public Result<PageResult<AuditEntity>> queryListByPage(@RequestBody AuditQuery query) {
        return Result.ok(auditService.queryListByPage(query));
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:audit:delete')")
    public Result deleteAudit(@RequestBody List<Integer> ids) {
        return Result.ok(auditService.delete(ids));
    }

    @PostMapping("/modify")
    @PreAuthorize("hasAuthority('sys:audit:edit')")
    public Result modifyAudit(@RequestBody AuditDto auditDto) {
        return Result.ok(auditService.modifyAudit(auditDto));
    }


    @PostMapping("/startAudit")
    @Operation(summary = "审核资源")
    public Result startAuditTask(@RequestBody AuditVo auditVo) {
        return Result.ok(auditService.startAuditTask(auditVo));
    }

    @PostMapping("/getAuditTask")
    @Operation(summary = "获取定时任务信息")
    public Result getAuditTask() {
        return Result.ok(auditService.getAuditTask());
    }

    @PostMapping("/stopAuditTask")
    @Operation(summary = "停止定时任务信息")
    public Result stopAuditTask() {
        return Result.ok(auditService.stopAuditTask());
    }

    @PostMapping("/test")
    @Operation(summary = "测试爬虫")
    public Result testGetArticle() {
        // PdfGeneratorUtils.generatorPdf();
        return Result.ok();
    }

    @GetMapping("/download")
    @Operation(summary = "下载测试报告")
    public void download(HttpServletResponse response) {
      PdfGeneratorUtils.downloadReport(response,"");
    }
}
