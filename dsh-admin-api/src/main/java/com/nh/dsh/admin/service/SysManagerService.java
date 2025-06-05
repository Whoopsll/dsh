package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.entity.SysManager;
import com.nh.dsh.admin.model.query.ChangePasswordQuery;
import com.nh.dsh.admin.model.query.SysManagerQuery;
import com.nh.dsh.admin.security.user.ManagerDetail;
import com.nh.dsh.admin.model.vo.SysManagerVO;

import java.util.List;

public interface SysManagerService extends IService<SysManager> {

    /**
     * 管理员列表
     *
     * @param query
     * @return
     */
    PageResult<SysManagerVO> page(SysManagerQuery query);

    /**
     * 新增管理员
     *
     * @param vo
     */
    void save(SysManagerVO vo);

    /**
     * 修改管理员信息
     *
     * @param vo
     */
    void update(SysManagerVO vo);

    /**
     * 删除管理员信息
     *
     * @param idList
     */
    void delete(List<Integer> idList);

    /**
     * 修改密码
     *
     * @param query
     */

    void changePassword(ChangePasswordQuery query);

    /**
     * 获取管理员信息
     *
     * @param manage
     * @return
     */

    SysManagerVO getManagerInfo(ManagerDetail manage);
}
