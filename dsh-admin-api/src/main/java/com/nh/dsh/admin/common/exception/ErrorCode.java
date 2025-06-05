package com.nh.dsh.admin.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(401, "还未授权，不能访问"),
    FORBIDDEN(403, "没有权限，禁止访问"),
    INTERNAL_SERVER_ERROR(500, "服务器异常，请稍后再试"),

    FILE_NAME_NOT_NULL(10001, "文件名不能为空"),
    BUCKET_NAME_NOT_NULL(10002, "桶名称不能为空"),
    FILE_NOT_EXIST(10003, "文件不存在"),
    BUCKET_NOT_EXIST(10004, "桶不存在"),
    BUCKET_NAME_NOT_EXIST(10005, "桶不存在，需要先创建桶在创建文件夹")
    ;

    private final int code;
    private final String msg;
}
