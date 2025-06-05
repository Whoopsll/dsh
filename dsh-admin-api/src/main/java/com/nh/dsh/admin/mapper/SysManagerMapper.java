package com.nh.dsh.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.model.entity.SysManager;
import com.nh.dsh.admin.model.query.SysManagerQuery;
import com.nh.dsh.admin.model.vo.SysManagerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysManagerMapper extends BaseMapper<SysManager> {

    default SysManager getByUsername(String username){
        return this.selectOne(new LambdaQueryWrapper<SysManager>().eq(SysManager::getUsername, username));
    }

    List<SysManagerVO> getManagerPage(Page<SysManagerVO> page, @Param("query") SysManagerQuery query);
}
