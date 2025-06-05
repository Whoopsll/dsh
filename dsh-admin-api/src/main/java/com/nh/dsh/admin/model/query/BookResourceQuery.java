package com.nh.dsh.admin.model.query;

import com.nh.dsh.admin.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 09:52
 **/

@Data
@Schema(description = "书本资源查询")
public class BookResourceQuery extends Query {

    @Schema(description = "书本id")
    @NotNull(message = "书本id不能为空")
    private Integer bookId;
    @Schema(description = "资源类型")
    private Integer type;
}
