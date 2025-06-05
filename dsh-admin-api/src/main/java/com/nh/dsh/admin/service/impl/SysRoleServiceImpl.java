package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.SysRoleConvert;
import com.nh.dsh.admin.model.entity.SysRole;
import com.nh.dsh.admin.mapper.SysRoleMapper;
import com.nh.dsh.admin.model.query.SysRoleQuery;
import com.nh.dsh.admin.service.SysManagerRoleService;
import com.nh.dsh.admin.service.SysRoleMenuService;
import com.nh.dsh.admin.service.SysRoleService;
import com.nh.dsh.admin.model.vo.SysRoleVO;
import com.nh.dsh.admin.utils.PageUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    private SysRoleMenuService sysRoleMenuService;
    private SysManagerRoleService sysManagerRoleService;

    @Override
    public PageResult<SysRoleVO> page(SysRoleQuery query) {
        List<SysRoleVO> list = baseMapper.getRoleList(query);
        Page<SysRoleVO> page = PageUtil.getPages(query.getPage(), query.getLimit(), list);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<SysRoleVO> getList(SysRoleQuery query) {
        List<SysRole> entityList = baseMapper.selectList(new LambdaQueryWrapper<>());
        return SysRoleConvert.INSTANCE.convertList(entityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysRoleVO vo) {
        SysRole entity = SysRoleConvert.INSTANCE.convert(vo);
        // 保存角色
        baseMapper.insert(entity);
        // 保存角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getPkId(), vo.getMenuIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleVO vo) {
        SysRole entity = SysRoleConvert.INSTANCE.convert(vo);

        // 更新角色
        updateById(entity);

        // 更新角色菜单关系
        sysRoleMenuService.saveOrUpdate(entity.getPkId(), vo.getMenuIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> idList) {
        // idList包含小于3的数值返回异常
        if (idList.stream().anyMatch(item -> item < 3)) {
            throw new ServerException("存在不能删除的角色类型（超级管理员，出版社管理员）");
        }

        // 删除角色
        removeByIds(idList);

        // 删除用户角色关系
        sysManagerRoleService.deleteByRoleIdList(idList);

        // 删除角色菜单关系
        sysRoleMenuService.deleteByRoleIdList(idList);
    }
}
