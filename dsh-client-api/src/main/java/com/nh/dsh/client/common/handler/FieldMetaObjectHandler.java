package com.nh.dsh.client.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.nh.dsh.client.common.constant.Constant;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FieldMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 创建时间
        strictInsertFill(metaObject, Constant.CREATE_TIME, Date.class, new Date());
        // 更新时间
        strictInsertFill(metaObject, Constant.UPDATE_TIME, Date.class, new Date());
        // 删除标识
        strictInsertFill(metaObject, Constant.DELETE_FLAG, Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时间
        strictUpdateFill(metaObject, Constant.UPDATE_TIME, Date.class, new Date());
    }
}