package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.SysManagerConvert;
import com.nh.dsh.admin.enums.SuperAdminEnum;
import com.nh.dsh.admin.mapper.SysManagerMapper;
import com.nh.dsh.admin.model.entity.SysManager;
import com.nh.dsh.admin.model.query.ChangePasswordQuery;
import com.nh.dsh.admin.model.query.SysManagerQuery;
import com.nh.dsh.admin.model.vo.SysManagerVO;
import com.nh.dsh.admin.security.user.ManagerDetail;
import com.nh.dsh.admin.service.ManagerPublisherService;
import com.nh.dsh.admin.service.SysManagerRoleService;
import com.nh.dsh.admin.service.SysManagerService;
import lombok.AllArgsConstructor;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SysManagerServiceImpl extends BaseServiceImpl<SysManagerMapper, SysManager> implements SysManagerService {

    private SysManagerRoleService sysManagerRoleService;
    private ManagerPublisherService managerPublisherService;

    @Override
    public PageResult<SysManagerVO> page(SysManagerQuery query) {
        Page<SysManagerVO> page = new Page<>(query.getPage(), query.getLimit());
        List<SysManagerVO> list = baseMapper.getManagerPage(page, query);
        list.forEach(vo -> {
            vo.setPublisherIdList(managerPublisherService.getPublisherIdList(vo.getPkId()));
            vo.setPublisherList(managerPublisherService.getPublisherList(vo.getPkId()));
        });
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysManagerVO vo) {
        SysManager entity = SysManagerConvert.INSTANCE.convert(vo);
        entity.setSuperAdmin(SuperAdminEnum.NO.getValue());

        // 判断用户名是否存在
        SysManager manager = baseMapper.getByUsername(entity.getUsername());
        if (manager != null) {
            throw new ServerException("用户名已经存在");
        }
        if (StringUtils.isNullOrEmpty(entity.getAvatar())) {
            entity.setAvatar("https://my-egg.oss-cn-beijing.aliyuncs.com/default_avatar.png");
        }
        // 保存用户
        baseMapper.insert(entity);
        // 保存用户角色关系
        sysManagerRoleService.saveOrUpdate(entity.getPkId(), vo.getRoleId());
        // 保存用户出版社关系
        managerPublisherService.save(entity.getPkId(), vo.getPublisherIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysManagerVO vo) {
        SysManager entity = SysManagerConvert.INSTANCE.convert(vo);
        // 判断用户名是否存在
        SysManager manager = baseMapper.getByUsername(entity.getUsername());
        if (manager != null && !manager.getPkId().equals(entity.getPkId())) {
            throw new ServerException("用户名已经存在");
        }
        if (StringUtils.isNullOrEmpty(entity.getAvatar())) {
            entity.setAvatar("https://my-egg.oss-cn-beijing.aliyuncs.com/default_avatar.png");
        }
        // 更新用户
        updateById(entity);
        // 更新用户角色关系
        sysManagerRoleService.saveOrUpdate(entity.getPkId(), vo.getRoleId());
        // 保存用户出版社关系
        managerPublisherService.update(entity.getPkId(), vo.getPublisherIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> idList) {
        // 删除管理员
        removeByIds(idList);
        // 删除用户角色关系
        sysManagerRoleService.removeByManagerId(idList);
        // 删除用户出版社关系
        idList.forEach(id -> managerPublisherService.delete(id));
    }

    @Override
    public SysManagerVO getManagerInfo(ManagerDetail manage) {
        SysManagerVO sysManagerVO = new SysManagerVO();
        System.out.println(">>>>>>getInfo" + manage.getPkId());
        SysManager sysManager = baseMapper.selectById(manage.getPkId());
        if (sysManager == null) {
            throw new ServerException("管理员不存在");
        }
        sysManagerVO.setPkId(sysManager.getPkId());
        sysManagerVO.setAvatar(sysManager.getAvatar());
        sysManagerVO.setUsername(sysManager.getUsername());
        sysManagerVO.setStatus(sysManager.getStatus());
        sysManagerVO.setRoleId(sysManagerRoleService.getByManagerId(manage.getPkId()).getRoleId());
        sysManagerVO.setCreateTime(sysManager.getCreateTime());
        sysManagerVO.setPublisherIdList(managerPublisherService.getPublisherIdList(manage.getPkId()));
        sysManagerVO.setPublisherList(managerPublisherService.getPublisherList(manage.getPkId()));
        return sysManagerVO;
    }

    @Override
    public void changePassword(ChangePasswordQuery query) {
        SysManager sysManager = baseMapper.selectById(query.getPkId());
        if (sysManager == null) {
            throw new ServerException("管理员不存在");
        }
        sysManager.setPassword(query.getPassword());
        updateById(sysManager);
    }
}
