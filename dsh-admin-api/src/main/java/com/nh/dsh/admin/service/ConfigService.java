package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.ConfigDto;
import com.nh.dsh.admin.model.entity.ConfigEntity;
import com.nh.dsh.admin.model.query.ConfigQuery;
import com.nh.dsh.admin.model.vo.ConfigVo;

public interface ConfigService extends IService<ConfigEntity> {

    /**
     * 获取出版社的公众号配置信息
     * @param configQuery
     * @return
     */
    PageResult getWxConfigByPublisher(ConfigQuery configQuery);

    /**
     * 新增、修改配置信息
     * @param configDto
     * @return
     */
    Result modifyConfig(ConfigDto configDto);

    /**
     * 删除配置信息
     * @param dto
     * @return
     */
    Result deleteConfig(ConfigDto dto);

    /**
     * 根据id获取配置信息
     * @param id
     * @return
     */
    Result getConfigById(int id);
}
