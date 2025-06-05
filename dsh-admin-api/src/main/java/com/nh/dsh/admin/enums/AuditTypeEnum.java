package com.nh.dsh.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum AuditTypeEnum {

    SHORT_VIDEO(0, "短视频"),

    OFFLINE_VIDEO(1, "离线视频"),
    BOOK_TEXT(2, "图书文字"),
    BOOK_PICTURE(3, "图书插画"),
    AUTO_AUDIT(4, "自动审核");

    private final int value;
    private final String name;

    public static String getNameByValue(int value) {
        for (AuditTypeEnum s : AuditTypeEnum.values()) {
            if (s.getValue() == value) {
                return s.getName();
            }
        }
        return "";
    }

    public static Integer getValueByName(String name) {
        for (AuditTypeEnum s : AuditTypeEnum.values()) {
            if (Objects.equals(s.getName(), name)) {
                return s.getValue();
            }
        }
        return null;
    }
}