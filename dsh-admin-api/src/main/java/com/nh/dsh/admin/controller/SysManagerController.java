package com.nh.dsh.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.query.ChangePasswordQuery;
import com.nh.dsh.admin.model.query.SysManagerQuery;
import com.nh.dsh.admin.security.user.ManagerDetail;
import com.nh.dsh.admin.security.user.SecurityUser;
import com.nh.dsh.admin.service.SysManagerService;
import com.nh.dsh.admin.model.vo.SysManagerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "管理员管理")
@AllArgsConstructor
@RestController
@RequestMapping("/sys/manager")
public class SysManagerController {

    private final SysManagerService sysManagerService;

    private final PasswordEncoder passwordEncoder;



    @PostMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<SysManagerVO>> page(@RequestBody @Valid SysManagerQuery query) {
        PageResult<SysManagerVO> page = sysManagerService.page(query);

        return Result.ok(page);
    }

    @PostMapping("add")
    @Operation(summary = "保存")
    public Result<String> save(@RequestBody @Valid SysManagerVO vo) {
        // 新增密码不能为空
        if (StrUtil.isBlank(vo.getPassword())) {
            Result.error("密码不能为空");
        }
        // 密码加密
        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        // 保存
        sysManagerService.save(vo);

        return Result.ok();
    }

    @PostMapping("edit")
    @Operation(summary = "修改")
    public Result<String> update(@RequestBody @Valid SysManagerVO vo) {
        // 如果密码不为空，则进行加密处理
        if (StrUtil.isBlank(vo.getPassword())) {
            vo.setPassword(null);
        } else {
            vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        }

        sysManagerService.update(vo);

        return Result.ok();
    }

    @PostMapping("remove")
    @Operation(summary = "删除")
    public Result<String> delete(@RequestBody List<Integer> idList) {
        Integer managerId = SecurityUser.getManagerId();
        if (idList.contains(managerId)) {
            return Result.error("不能删除当前登录管理员");
        }

        sysManagerService.delete(idList);

        return Result.ok();
    }

    @PostMapping("getManagerInfo")
    @Operation(summary = "获取管理员信息")
    public Result<SysManagerVO> getManagerInfo() {
        ManagerDetail manager = SecurityUser.getManager();
        return Result.ok(sysManagerService.getManagerInfo(manager));
    }

    @PostMapping("changePassword")
    @Operation(summary = "修改密码")
    public Result<String> editPassword(@RequestBody @Valid ChangePasswordQuery query) {
        ManagerDetail manager = SecurityUser.getManager();
        if (manager.getPkId() == null) {
            throw new ServerException("管理员不存在");
        }
        query.setPkId(manager.getPkId());
        query.setPassword(passwordEncoder.encode(query.getPassword()));
        sysManagerService.changePassword(query);
        return Result.ok();
    }


}
