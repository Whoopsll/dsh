package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.query.UserQuery;
import com.nh.dsh.admin.model.vo.UserVO;
import com.nh.dsh.admin.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 10:11
 **/

@Tag(name = "用户管理")
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("page")
    public Result<PageResult<UserVO>> page(@RequestBody UserQuery userQuery) {
        return Result.ok(userService.page(userQuery));
    }

    @GetMapping("list")
    public Result<List<Map<String, Object>>> selectedList() {
        return Result.ok(userService.selectedList());
    }

    @PostMapping("delete")
    public Result<Integer> delete(@RequestBody List<Integer> ids) {
        return Result.ok(userService.delete(ids));
    }
}
