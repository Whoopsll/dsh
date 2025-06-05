package com.nh.dsh.client.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 15:17
 **/

@Data
public class CommentItemVO {
    private Integer id;
    private Integer forumId;
    private Integer parentId;
    private String parentUsername;
    private String forumName;
    private Integer type;
    private String content;
    private Integer fileType;
    private List<String> files;
    private Integer userId;
    private String username;
    private String avatar;
    private String createTime;
    private long likeNum;
    private boolean isLike;
    private boolean isStar;
    private List<CommentItemVO> replyList;
}
