package com.nh.dsh.admin.common.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Constant {
    Integer ROOT = 0;
    String CREATE_TIME = "createTime";
    String UPDATE_TIME = "updateTime";
    String DELETE_FLAG = "deleteFlag";

    String AUTO_AUDIT_JOB = "AUTO_AUDIT_JOB";
    String AUTO_AUDIT_TRIGGER = "AUTO_AUDIT_TRIGGER";
    List<String> WEEK_ARRAY = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
    String AUDIT_TASK_IS_STOP_KEY = "AUDIT_TASK_IS_STOP_KEY";
}
