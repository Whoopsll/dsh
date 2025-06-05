package com.nh.dsh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nh.dsh.admin.model.entity.SysRole;
import com.nh.dsh.admin.model.query.SysRoleQuery;
import com.nh.dsh.admin.model.vo.SysRoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRoleVO> getRoleList(@Param("query") SysRoleQuery query);

}
