package com.nh.dsh.admin.model.query;

import com.nh.dsh.admin.common.model.Query;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 12:51
 **/

@Data
public class BookQuery extends Query {

    private String bookName;
    private String writer;
    private String isbn;
    private Integer publisherId;
}
