package com.nh.dsh.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {
    RESOURCE("resource", "资源码"),

    EXCHANGE("exchange", "兑换码"),
    BOOK("book", "书籍");

    String value;
    String name;
}
