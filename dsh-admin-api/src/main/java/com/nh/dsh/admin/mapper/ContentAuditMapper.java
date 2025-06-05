package com.nh.dsh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nh.dsh.admin.model.entity.AuditEntity;
import com.nh.dsh.admin.model.entity.ContentAuditEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContentAuditMapper extends BaseMapper<ContentAuditEntity> {
}
