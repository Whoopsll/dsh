package com.nh.dsh.admin.common.constant;

public interface PythonAuditInterface {

    String base_url = "http://8.153.16.66:8000";
    //图文审核
    String BOOK_TEXT_API = base_url + "/upload";
    //图文插图
    String BOOK_PICTURE__API = base_url + "/uploadimagefile";
    //离线视频
    String OFFLINE_VIDEO__API = base_url + "/uploadvideo";
    //短视频
    String SHORT_VIDEO__API = base_url + "/uploadvideoimage";
}
