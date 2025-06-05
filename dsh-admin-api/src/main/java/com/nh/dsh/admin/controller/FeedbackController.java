package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.query.FeedbackQuery;
import com.nh.dsh.admin.model.vo.FeedbackVO;
import com.nh.dsh.admin.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 10:04
 **/

@AllArgsConstructor
@RestController
@Tag(name = "意见反馈接口")
@RequestMapping("feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping("page")
    public Result<PageResult<FeedbackVO>> page(@RequestBody FeedbackQuery query) {
        return Result.ok(feedbackService.page(query));
    }

    @PostMapping("delete")
    public Result<FeedbackVO> delete(@RequestBody List<Integer> ids) {
        feedbackService.deleteInBatch(ids);
        return Result.ok();
    }
}
