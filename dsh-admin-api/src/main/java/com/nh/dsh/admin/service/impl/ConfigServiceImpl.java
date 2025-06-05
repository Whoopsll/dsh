package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.mapper.BookMapper;
import com.nh.dsh.admin.mapper.ConfigMapper;
import com.nh.dsh.admin.model.dto.ConfigDto;
import com.nh.dsh.admin.model.entity.BookEntity;
import com.nh.dsh.admin.model.entity.ConfigEntity;
import com.nh.dsh.admin.model.query.ConfigQuery;
import com.nh.dsh.admin.model.vo.ConfigVo;
import com.nh.dsh.admin.service.BookService;
import com.nh.dsh.admin.service.ConfigService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConfigServiceImpl extends BaseServiceImpl<ConfigMapper, ConfigEntity> implements ConfigService {
    @Resource
    BookService bookService;

    @Override
    public PageResult getWxConfigByPublisher(ConfigQuery configQuery) {
        List<ConfigVo> results = new ArrayList<>();
        try {
            Page<ConfigEntity> entityPage = getPage(configQuery);
            LambdaQueryWrapper<ConfigEntity> wrapper = new LambdaQueryWrapper<ConfigEntity>()
                    .eq(ConfigEntity::getDeleteFlag, 0)
                    .eq(ConfigEntity::getType, 0).
                    eq(ConfigEntity::getPublisherId, configQuery.getPublisherId()).orderByDesc(ConfigEntity::getCreateTime);
            Page<ConfigEntity> pageResult = baseMapper.selectPage(entityPage, wrapper);
            results = pageResult.getRecords().stream().map(item -> {
                ConfigVo configVo = new ConfigVo();
                BeanUtils.copyProperties(item, configVo);
                return configVo;
            }).collect(Collectors.toList());
            return new PageResult<>(results, pageResult.getTotal());
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

    @Override
    public Result modifyConfig(ConfigDto configDto) {
        try {
            ConfigEntity entity = null;
            if (configDto.getId() == 0) {
                // 新增
                List<ConfigEntity> list = baseMapper.selectList(new QueryWrapper<ConfigEntity>().eq("publisher_id", configDto.getPublisherId()).eq("delete_flag", 0).eq("account", configDto.getAccount()));
                if (!list.isEmpty()) {
                    throw new ServerException("该配置信息已存在");
                }
                entity = new ConfigEntity();
                BeanUtils.copyProperties(configDto, entity);
                entity.setType(0);
                entity.setId(null);
                boolean result = saveOrUpdate(entity);
                if (result) {
                    //新增成功
                    return Result.ok("新增成功");
                }
                return Result.error("新增异常");
            } else {
                //更新操作
                entity = getById(configDto.getId());
                BeanUtils.copyProperties(configDto, entity);
                boolean result = saveOrUpdate(entity);
                if (result) {
                    //新增成功
                    return Result.ok("更新成功");
                }
                return Result.error("更新异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerException("接口异常");
        }
    }

    @Override
    public Result deleteConfig(ConfigDto dto) {
        try {
            int result = 0;
            List<BookEntity> entities = bookService.getBaseMapper().selectList(new QueryWrapper<BookEntity>().eq("wx_config_id", dto.getId()).eq("delete_flag", 0));
            List<BookEntity> collect = entities.stream().map(item -> {
                item.setWxConfigId(0);
                return item;
            }).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                boolean b = bookService.saveOrUpdateBatch(collect);
                if (b) {
                    result = baseMapper.deleteById(dto.getId());
                }
            } else {
                result = baseMapper.deleteById(dto.getId());
            }
            if (result == 0) {
                // 删除异常
                return Result.error();
            } else {
                // 删除成功
                return Result.ok();
            }
        } catch (Exception e) {
            throw new ServerException("删除异常");
        }
    }

    @Override
    public Result getConfigById(int id) {
        try {
            ConfigEntity entity = baseMapper.selectById(id);
            if (entity == null) {
                return Result.error("数据不存在");
            }
            return Result.ok(entity);
        } catch (Exception e) {
            throw new ServerException("数据不存在");
        }
    }
}
