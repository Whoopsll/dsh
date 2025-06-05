package com.nh.dsh.client.controller;

import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.utils.MinioUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Flobby
 */
@Slf4j
@Tag(name = "基础服务")
@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CommonController {
    private final MinioUtils minioUtils;

    @PostMapping("upload/img")
    @Operation(summary = "上传图片")
    public Result<Map<String, Object>> uploadImg(@RequestParam(defaultValue = "dsh") String bucketName,
                                                 @RequestParam(name = "file", required = false) MultipartFile[] file) {
        return Result.ok(minioUtils.uploadFile(bucketName, file));
    }

    @PostMapping("upload/voice")
    @Operation(summary = "上传语音")
    public Result<Map<String, Object>> uploadVoice(@RequestParam(defaultValue = "dsh") String bucketName,
                                                   @RequestParam(name = "file", required = false) MultipartFile[] file) {
        return Result.ok(minioUtils.uploadFile(bucketName, file));
    }
}