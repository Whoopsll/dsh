package com.nh.dsh.client.model.query;

import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 11:32
 **/

@Data
public class ForumQuery extends Query{
    private Integer categoryId;
    private String keyword;
}
