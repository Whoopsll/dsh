package com.nh.dsh.admin.model.query;

import com.nh.dsh.admin.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 17:15
 **/

@Data
@Schema(description = "查询")
public class NoticeQuery extends Query {
    private Integer forumId;
    private String keyword;
}
