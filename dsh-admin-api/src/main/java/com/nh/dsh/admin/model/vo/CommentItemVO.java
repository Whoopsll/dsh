package com.nh.dsh.admin.model.vo;

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
    private Integer type;
    private String content;
    private Integer fileType;
    private List<String> files;
    private Integer userId;
    private String username;
    private String avatar;
    private String createTime;
    private Integer enabled;
    private long replyNum;
    private List<CommentItemVO> replyList;
}
