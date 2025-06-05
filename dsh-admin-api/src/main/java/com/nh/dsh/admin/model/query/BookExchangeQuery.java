package com.nh.dsh.admin.model.query;

import com.nh.dsh.admin.common.model.Query;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-30 15:49
 **/

@Data
public class BookExchangeQuery extends Query {
    private String bookId;

}
