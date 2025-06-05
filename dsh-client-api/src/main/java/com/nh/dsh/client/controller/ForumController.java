package com.nh.dsh.client.controller;

import com.nh.dsh.client.common.result.PageResult;
import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.model.dto.CommentActionDTO;
import com.nh.dsh.client.model.dto.CommentDTO;
import com.nh.dsh.client.model.query.ForumQuery;
import com.nh.dsh.client.model.vo.CommentItemVO;
import com.nh.dsh.client.model.vo.ForumDetailVO;
import com.nh.dsh.client.model.vo.ForumListItemVO;
import com.nh.dsh.client.service.CommentService;
import com.nh.dsh.client.service.ForumService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 15:47
 **/

@Slf4j
@Tag(name = "论坛服务")
@RestController
@RequestMapping("/forum")
@AllArgsConstructor
public class ForumController {
    private final ForumService forumService;
    private final CommentService commentService;

    @PostMapping("page")
    public Result<PageResult<ForumListItemVO>> forumPage(@RequestBody ForumQuery query) {
        return Result.ok(forumService.forumList(query));
    }

    @PostMapping("followedPage")
    public Result<PageResult<ForumListItemVO>> forumFollowedPage(@RequestBody ForumQuery query) {
        return Result.ok(forumService.forumListFollowed(query));
    }

    @PostMapping("detail")
    public Result<ForumDetailVO> forumDetail(@RequestParam("forumId") Integer forumId) {
        return Result.ok(forumService.forumDetail(forumId));
    }

    @PostMapping("followed")
    public Result<String> forumFollowed(@RequestParam("forumId") Integer forumId) {
        forumService.followedForum(forumId);
        return Result.ok();
    }

    @PostMapping("comment/add")
    public Result<String> forumCommentAdd(@RequestBody @Valid CommentDTO dto) {
        commentService.addComment(dto);
        return Result.ok();
    }

    @PostMapping("comment/modify")
    public Result<String> forumCommentModify(@RequestBody CommentDTO dto) {
        commentService.modifyComment(dto);
        return Result.ok();
    }

    @PostMapping("comment/list")
    public Result<List<CommentItemVO>> commentList(@RequestParam("forumId") Integer forumId) {
        return Result.ok(commentService.queryForumCommentList(forumId));
    }

    @PostMapping("comment")
    public Result<CommentItemVO> commentDetail(@RequestParam("commentId") Integer commentId) {
        return Result.ok(commentService.getCommentDetail(commentId));
    }

    @PostMapping("comment/action")
    public Result<String> doCommentAction(@RequestBody CommentActionDTO dto) {
        commentService.doCommentAction(dto);
        return Result.ok();
    }


    @Schema(name = "获取可能喜欢的书籍论坛")
    @PostMapping("/like")
    public Result getLikedBook(@RequestParam("type") int bookType, @RequestParam("bookId") int bookId) {
        return forumService.getLikedBook(bookType, bookId);
    }
}
