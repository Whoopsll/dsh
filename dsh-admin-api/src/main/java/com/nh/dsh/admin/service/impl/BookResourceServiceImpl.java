package com.nh.dsh.admin.service.impl;

import cn.hutool.extra.qrcode.QrCodeUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.nh.dsh.admin.common.config.CodeConfig;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.BookResourceConvert;
import com.nh.dsh.admin.enums.ResourceTypeEnum;
import com.nh.dsh.admin.mapper.AuditMapper;
import com.nh.dsh.admin.mapper.BookMapper;
import com.nh.dsh.admin.mapper.BookResourceMapper;
import com.nh.dsh.admin.mapper.ConfigMapper;
import com.nh.dsh.admin.model.dto.BookResourceDTO;
import com.nh.dsh.admin.model.dto.ResourceImportDTO;
import com.nh.dsh.admin.model.entity.AuditEntity;
import com.nh.dsh.admin.model.entity.BookEntity;
import com.nh.dsh.admin.model.entity.BookResourceEntity;
import com.nh.dsh.admin.model.entity.ConfigEntity;
import com.nh.dsh.admin.model.query.BookResourceQuery;
import com.nh.dsh.admin.model.vo.AuditResourceVO;
import com.nh.dsh.admin.model.vo.BookResourceVO;
import com.nh.dsh.admin.security.cache.QuartzTaskStoreCache;
import com.nh.dsh.admin.security.user.SecurityUser;
import com.nh.dsh.admin.service.BookResourceService;
import com.nh.dsh.admin.service.EmailService;
import com.nh.dsh.admin.utils.HttpUtils;
import com.nh.dsh.admin.utils.WxUtils;
import com.nh.dsh.admin.wechat.cache.AccessTokenCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 09:49
 **/

@Slf4j
@Service
@AllArgsConstructor
public class BookResourceServiceImpl extends BaseServiceImpl<BookResourceMapper, BookResourceEntity> implements BookResourceService {
    private final BookMapper bookMapper;
    private final CodeConfig codeConfig;
    private final AuditMapper auditMapper;
    private final QuartzTaskStoreCache quartzTaskStoreCache;
    private final EmailService emailService;
    private final RestTemplate restTemplate;
    private final ConfigMapper configMapper;
    private final AccessTokenCache tokenCache;

    @Override
    public void save(BookResourceDTO dto) {
        BookResourceEntity bookResource = BookResourceConvert.INSTANCE.convert(dto);
        baseMapper.insert(bookResource);
    }

    @Override
    public void update(BookResourceDTO dto) {

        BookResourceEntity bookResource = BookResourceConvert.INSTANCE.convert(dto);
        baseMapper.updateById(bookResource);
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public PageResult<BookResourceVO> page(BookResourceQuery query) {
        Page<BookResourceEntity> page = getPage(query);
        BookEntity entity = bookMapper.selectById(query.getBookId());
        boolean isShowWxQrCode = entity.getWxConfigId() != 0; //是否生成微信二维码
        LambdaQueryWrapper<BookResourceEntity> wrapper = new LambdaQueryWrapper<BookResourceEntity>()
                .eq(BookResourceEntity::getBookId, query.getBookId())
                .eq(BookResourceEntity::getType, query.getType())
                .orderByDesc(BookResourceEntity::getCreateTime);
        Page<BookResourceEntity> pageResult = baseMapper.selectPage(page, wrapper);
        List<BookResourceVO> vos = BookResourceConvert.INSTANCE.convertList(pageResult.getRecords());
        vos.forEach(item ->
        {
            item.setSecret(codeConfig.getSecret());
            String link = String.format("http://demo.dianhuiyun.com.cn/dsh-client-h5/#/pages/book/resource?resourceId=%s&secret=%s", item.getId(), codeConfig.getSecret());
            if(isShowWxQrCode) {
                ConfigEntity config = configMapper.selectById(entity.getWxConfigId());
                // 生成微信公众号带参数二维码
                String wxLink = WxUtils.getWxLink(ResourceTypeEnum.RESOURCE.getValue(), item.getId(), config.getAccount(), config.getPassword(), config.getId());
                if (wxLink == null) {
                    item.setQrLink(link);
                } else {
                    item.setQrLink(wxLink);
                }
            } else {
                //普通生成
                item.setQrLink(link);
            }
        });
        return new PageResult<>(vos, pageResult.getTotal());
    }

    @Override
    public ResponseEntity<byte[]> downloadResourcesQrCode(int bookId) {
        Map<String, byte[]> map = new HashMap<>();
        list(new LambdaQueryWrapper<BookResourceEntity>()
                .eq(BookResourceEntity::getBookId, bookId)
                .eq(BookResourceEntity::getValidStatus, 0)
                .eq(BookResourceEntity::getEnabled, 0)
        )
                .stream()
                .forEach(item -> {
            try {
                map.put(item.getResourceName() + item.getId() + ".png",
                        generateQrCode("http://demo.dianhuiyun.com.cn/dsh-client-h5/#/pages/book/resource?resourceId=" + item.getId() + "&secret=" + codeConfig.getSecret()));
            } catch (IOException e) {
                log.info("{} 二维码生成失败, {}", item.getResourceName(), e.getMessage());
            }
        });
        try {
            byte[] zipBytes = generateZip(map);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=qrcodes.zip");
            return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("二维码生成失败", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importResources(Integer bookId, MultipartFile file, int type) {
        List<BookResourceEntity> partialImportList = new ArrayList<>();
        // 获取二维码审核服务状态
        AuditEntity auditEntity = auditMapper.selectOne(new QueryWrapper<AuditEntity>().eq("name", "二维码审核").eq("delete_flag", 0));
        AtomicBoolean isHasErrorResource = new AtomicBoolean(false);
        try {
            EasyExcel.read(file.getInputStream(), ResourceImportDTO.class, new PageReadListener<ResourceImportDTO>(dataList -> {
                partialImportList.addAll(dataList.stream().map(item -> {
                    BookResourceEntity resource = new BookResourceEntity();
                    resource.setBookId(bookId);
                    // 判断url是否是网络链接
                    if (!item.getUrl().startsWith("http")) {
                        resource.setValidStatus(1);
                    }
                    resource.setLink(item.getUrl());
                    resource.setResourceName(item.getDesc());
                    if(StringUtils.isNullOrEmpty(resource.getLink()) || StringUtils.isNullOrEmpty(resource.getResourceName())) {
                        // 名称和link有空的情况
                        isHasErrorResource.set(true);
                    }
                    resource.setUserId(SecurityUser.getManagerId());
                    resource.setType(type);
                    // 校验网址信息是否异常
                    if (auditEntity != null) {
                        if (auditEntity.getStatus() == 1) {
                            resource.setValidStatus(0);
                        } else {
                            resource.setValidStatus(1);
                        }
                    } else {
                        resource.setValidStatus(0);
                    }
                    return resource;
                }).toList());
            })).sheet().doRead();
        } catch (IOException e) {
            log.error("导入失败", e);
            throw new ServerException("导入失败");
        }
        if(isHasErrorResource.get()) {
            //返回异常资源
            throw new ServerException("导入的资源链接和名称不得为空");
        }
        this.saveBatch(partialImportList);
    }

    @Override
    public PageResult<AuditResourceVO> auditResourcePage(BookResourceQuery query) {
        Page<BookResourceEntity> page = getPage(query);
        LambdaQueryWrapper<BookResourceEntity> wrapper = new LambdaQueryWrapper<BookResourceEntity>()
                .orderByDesc(BookResourceEntity::getCreateTime);
        baseMapper.selectPage(page, wrapper);
        List<AuditResourceVO> result = page.getRecords().stream().map(item -> {
            AuditResourceVO vo = BookResourceConvert.INSTANCE.convert(item);
            BookEntity bookEntity = bookMapper.selectById(item.getBookId());
            vo.setBookName(bookEntity.getBookName());
            vo.setBookCover(bookEntity.getBookCover());
            vo.setIsChanged(item.getValidStatus());
            vo.setIsCanAccess(item.getCanAccess());
            vo.setReason(item.getErrorReason());
            vo.setResult(item.getAuditResult());
            vo.setUpdateTime(item.getUpdateTime());
            vo.setType(item.getType());
            return vo;
        }).sorted(Comparator.comparingInt(AuditResourceVO::getIsChanged).reversed()).toList();
        return new PageResult<>(result, page.getTotal());
    }

    @Override
    public void resourceEnabled(BookResourceDTO dto) {
        BookResourceEntity entity = baseMapper.selectById(dto.getId());
        entity.setEnabled(entity.getEnabled() ^ 1);
        baseMapper.updateById(entity);
    }

    @Override
    public Boolean auditResource() {
        try {
            List<BookResourceEntity> list = baseMapper.selectList(new QueryWrapper<BookResourceEntity>().eq("delete_flag", 0).orderByDesc("create_time"));
            List<Map> results;
            results = HttpUtils.batchPing(list,quartzTaskStoreCache);
            AtomicBoolean isHasError = new AtomicBoolean(false); //是否有异常
            List<BookResourceEntity> collect = results.stream().map(item -> {
                BookResourceEntity entity = new BookResourceEntity();
                entity.setAuditResult((Integer) item.get("result"));
                entity.setId((Integer) item.get("id"));
                entity.setCanAccess((Integer) item.get("isCanAccess"));
                entity.setErrorReason((String) item.get("reason"));
                if (!isHasError.get()) {
                    if (entity.getAuditResult() == 1) {
                        isHasError.set(true);
                    }
                }
                // entity.setUpdateTime((Date) item.get("time"));
                return entity;
            }).collect(Collectors.toList());
            if (isHasError.get()) {
                // 如果有异常，发送信息到通知邮箱
                AuditEntity auditEntity = null;
                List<AuditEntity> auditEntities = auditMapper.selectList(new QueryWrapper<AuditEntity>().eq("delete_flag", 0).eq("service_type", 1));
                if (!auditEntities.isEmpty()) {
                    auditEntity = auditEntities.get(0);
                }
                if (auditEntity != null) {
                    emailService.sendMail("1244353765@qq.com", auditEntity.getEmail(), "二维码审核异常提示", "您当前的资源存在异常问题，请及时处理");
                }
            }
            boolean b = saveOrUpdateBatch(collect);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    private byte[] generateQrCode(String resource) throws IOException {
        BufferedImage bufferedImage = QrCodeUtil.generate(resource, 300, 300);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }

    private byte[] generateZip(Map<String, byte[]> resources) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);

        for (Map.Entry<String, byte[]> entry : resources.entrySet()) {
            zos.putNextEntry(new ZipEntry(entry.getKey()));
            zos.write(entry.getValue());
            zos.closeEntry();
        }

        zos.close();
        return baos.toByteArray();
    }
}
