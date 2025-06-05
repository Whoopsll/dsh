package com.nh.dsh.client.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 14:59
 **/

@Data
public class CommentDTO {
    private Integer id;
    @NotNull
    private Integer forumId;
    @NotBlank
    private String content;
    private Integer parentId;
    @NotNull
    private Integer type;
    private Integer fileType;
    private List<String> files;
}
