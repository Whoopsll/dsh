package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.model.entity.SysManagerRole;
import com.nh.dsh.admin.mapper.SysManagerRoleMapper;
import com.nh.dsh.admin.service.SysManagerRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysManagerRoleServiceImpl extends BaseServiceImpl<SysManagerRoleMapper, SysManagerRole> implements SysManagerRoleService {

    @Override
    public void saveOrUpdate(Integer managerId, Integer roleId) {
        SysManagerRole sysManagerRole = baseMapper.selectOne(new LambdaQueryWrapper<SysManagerRole>()
                .eq(SysManagerRole::getManagerId, managerId));
        if (sysManagerRole == null) {
            sysManagerRole = new SysManagerRole();
            sysManagerRole.setManagerId(managerId);
        }
        sysManagerRole.setRoleId(roleId);
        saveOrUpdate(sysManagerRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByManagerId(List<Integer> idList) {
        baseMapper.delete(new LambdaQueryWrapper<SysManagerRole>()
                .in(SysManagerRole::getManagerId, idList));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByRoleIdList(List<Integer> roleIdList) {
        remove(new LambdaQueryWrapper<SysManagerRole>().in(SysManagerRole::getRoleId, roleIdList));
    }

    @Override
    public SysManagerRole getByManagerId(Integer managerId) {
        System.out.println(">>>>>>>>>>>>>>>manager" + managerId);
        SysManagerRole sysManagerRole = baseMapper.selectOne(new LambdaQueryWrapper<SysManagerRole>()
                .eq(SysManagerRole::getManagerId, managerId));
        if (sysManagerRole == null) {
            throw new ServerException("该管理员暂未绑定角色");
        }
        return sysManagerRole;
    }
}
