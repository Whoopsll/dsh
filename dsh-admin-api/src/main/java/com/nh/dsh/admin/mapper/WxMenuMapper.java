package com.nh.dsh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nh.dsh.admin.wechat.model.entity.WxMenuModel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WxMenuMapper extends BaseMapper<WxMenuModel> {

    @Delete({"<script>" +
            "DELETE FROM t_wx_menu WHERE key_id IN "+
            " <foreach collection='keys' item='key' separator=',' open='(' close=')'> " +
            " #{key}"+
            " </foreach>"+
            "</script>"
    })
    int deleteBatchByKey(@Param("keys") List<String> keys);

}
