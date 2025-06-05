package com.nh.dsh.client.controller;

import com.nh.dsh.client.common.result.PageResult;
import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.model.dto.FeedbackDTO;
import com.nh.dsh.client.model.query.Query;
import com.nh.dsh.client.model.vo.CommentItemVO;
import com.nh.dsh.client.model.vo.UserInfoVO;
import com.nh.dsh.client.service.CommentService;
import com.nh.dsh.client.service.FeedbackService;
import com.nh.dsh.client.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 15:54
 **/

@Slf4j
@Tag(name = "账户服务")
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final CommentService commentService;
    private final FeedbackService feedbackService;

    @PostMapping("userInfo")
    public Result<UserInfoVO> queryUserInfo() {
        return Result.ok(userService.userInfo());
    }

    @PostMapping("starCommentList")
    public Result<PageResult<CommentItemVO>> queryStarCommentList(@RequestBody Query query) {
        return Result.ok(commentService.queryStarCommentList(query));
    }

    @PostMapping("myCommentList")
    public Result<PageResult<CommentItemVO>> queryMyCommentList(@RequestBody Query query) {
        return Result.ok(commentService.queryMyCommentList(query));
    }

    @PostMapping("reviewCommentList")
    public Result<PageResult<CommentItemVO>> queryReviewCommentList(@RequestBody Query query) {
        return Result.ok(commentService.queryReviewCommentList(query));
    }

    @PostMapping("feedback")
    public Result<Object> queryFeedback(@RequestBody FeedbackDTO dto) {
        feedbackService.feedback(dto);
        return Result.ok();
    }

}
