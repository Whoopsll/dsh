package com.nh.dsh.admin.service.impl;

import com.nh.dsh.admin.service.ManagerPublisherService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.nh.dsh.admin.convert.SysManagerConvert;
import com.nh.dsh.admin.enums.AccountStatusEnum;
import com.nh.dsh.admin.model.entity.SysManager;
import com.nh.dsh.admin.security.user.ManagerDetail;
import com.nh.dsh.admin.service.SysManagerDetailsService;
import com.nh.dsh.admin.service.SysMenuService;

import java.util.Set;

@Service
@AllArgsConstructor
public class SysManagerDetailsServiceImpl implements SysManagerDetailsService {
    private final SysMenuService sysMenuService;
    private final ManagerPublisherService managerPublisherService;

    @Override
    public UserDetails getManagerDetails(SysManager sysManager) {
        // 转换成UserDetail对象
        ManagerDetail managerDetail = SysManagerConvert.INSTANCE.convertDetail(sysManager);

        // 账号不可用
        if (sysManager.getStatus() == AccountStatusEnum.DISABLE.getValue()) {
            managerDetail.setEnabled(false);
        }

        // 用户权限列表
        Set<String> authoritySet = sysMenuService.getManagerAuthority(managerDetail);
        managerDetail.setAuthoritySet(authoritySet);
        // 出版社列表
        managerDetail.setPublisherIdList(managerPublisherService.getPublisherIdList(sysManager.getPkId()));
        return managerDetail;
    }

}
