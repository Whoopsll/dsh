package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.mapper.ContentAuditMapper;
import com.nh.dsh.admin.model.dto.AuditDto;
import com.nh.dsh.admin.model.dto.ContentAuditDto;
import com.nh.dsh.admin.model.entity.AuditEntity;
import com.nh.dsh.admin.model.entity.ContentAuditEntity;
import com.nh.dsh.admin.model.query.AuditQuery;
import com.nh.dsh.admin.model.query.ContentAuditQuery;
import com.nh.dsh.admin.model.vo.AuditVo;

import java.util.List;

public interface ContentAuditService extends IService<ContentAuditEntity> {
    /**
     * 新增审核管理项
     * @param auditDto 新增参数
     * @return
     */
    Result addInfo(ContentAuditDto auditDto);

    /**
     * 分页查询审核管理项
     * @param query 分页参数
     * @return
     */
    PageResult<ContentAuditEntity> queryListByPage(ContentAuditQuery query);

    /**
     * 删除
     * @param ids
     * @return
     */
    Result deleteInfo(List<Integer> ids);

    /**
     * 获取审核详情
     * @return
     */
    Result getAuditDetail(int auditId);
}
