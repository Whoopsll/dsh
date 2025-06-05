package com.nh.dsh.client.enums;

import com.nh.dsh.client.common.exception.ServerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 14:27
 **/

@Getter
@AllArgsConstructor
public enum UserActionEnum {
    FOLLOW_FORUM(0, "关注论坛"),
    LIKE_COMMENT(1, "点赞评论"),
    STAR_COMMENT(2, "收藏评论"),
    ;

    private final Integer type;
    private final String desc;

    // 根据type获取枚举
    public static UserActionEnum getByType(Integer type) {
        for (UserActionEnum userAction : values()) {
            if (userAction.getType().equals(type)) {
                return userAction;
            }
        }
        throw new ServerException("行为枚举不存在");
    }
}
