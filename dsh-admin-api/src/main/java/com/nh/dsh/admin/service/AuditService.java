package com.nh.dsh.admin.service;

import com.nh.dsh.admin.common.model.Query;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.AuditDto;
import com.nh.dsh.admin.model.entity.AuditEntity;
import com.nh.dsh.admin.model.query.AuditQuery;
import com.nh.dsh.admin.model.vo.AuditVo;

import java.util.List;

public interface AuditService {

    /**
     * 新增审核管理项
     * @param auditDto 新增参数
     * @return
     */
    Result addAudit(AuditDto auditDto);

    /**
     * 分页查询审核管理项
     * @param query 分页参数
     * @return
     */
    PageResult<AuditEntity> queryListByPage(AuditQuery query);

    /**
     * 修改管理项内容
     * @param auditDto 修改参数
     * @return
     */
    Result modifyAudit(AuditDto auditDto);

    /**
     * 删除
     * @param ids
     * @return
     */
    Result delete(List<Integer> ids);

    /**
     * 获取二维码审核任务信息
     * @return
     */
    AuditEntity getAuditTask();

    /**
     * 开启扫码定时任务
     * @param auditVo
     * @return
     */
    Result startAuditTask(AuditVo auditVo);

    /**
     * 停止定时任务
     * @return
     */
    Result stopAuditTask();
}
