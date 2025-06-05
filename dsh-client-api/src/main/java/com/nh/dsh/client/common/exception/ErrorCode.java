package com.nh.dsh.client.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(401, "登录失效，请重新登录"),
    INTERNAL_SERVER_ERROR(500, "服务器异常，请稍后再试"),

    OPERATION_FAIL(3000, "操作失败"),
    CODE_SEND_FAIL(3002, "短信发送失败"),
    PARAMS_ERROR(3003, "参数异常"),
    SMS_CODE_ERROR(3004, "短信验证码错误"),
    ACCOUNT_DISABLED(3005, "账号被禁用"),
    USER_NOT_EXIST( 3006,  "用户不存在"),
    PHONE_IS_EXIST(3007, "手机号已被他人绑定"),
    THE_SAME_PHONE(3008, "该手机号和当前绑定手机号相同" ),
    ALREADY_HAS_CHECK(3009, "账号出现重复异常"),

    FILE_NAME_NOT_NULL(10001, "文件名不能为空"),
    BUCKET_NAME_NOT_NULL(10002, "桶名称不能为空"),
    FILE_NOT_EXIST(10003, "文件不存在"),
    BUCKET_NOT_EXIST(10004, "桶不存在"),
    BUCKET_NAME_NOT_EXIST(10005, "桶不存在，需要先创建桶在创建文件夹")
    ;

    private final int code;
    private final String msg;
}
