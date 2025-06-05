package com.nh.dsh.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.client.model.entity.ForumEntity;
import com.nh.dsh.client.model.query.ForumQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-06-19 15:52
 **/

@Mapper
public interface ForumMapper extends BaseMapper<ForumEntity> {

    List<ForumEntity> selectByPage(Page<ForumEntity> page, @Param("params") ForumQuery query);

    @Select("<script>" +
            "SELECT * FROM forum " +
            "WHERE book_id IN " +
            "<foreach item='id' collection='bookIds' open='(' separator=',' close=')'> " +
            "#{id} " +
            "</foreach> " +
            "AND delete_flag=0 " +
            "</script>")
    List<ForumEntity> getLikedForums(@Param("bookIds") List<Integer> bookIds);
}
