package com.nh.dsh.admin.websocket;

import lombok.Data;

@Data
public class WebSocketMessageVo {
    Integer taskId;
    Integer result;
    Integer status;
    Integer userId;
}
