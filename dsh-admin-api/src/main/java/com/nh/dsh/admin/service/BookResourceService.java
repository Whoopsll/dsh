package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.BookResourceDTO;
import com.nh.dsh.admin.model.entity.BookResourceEntity;
import com.nh.dsh.admin.model.query.BookResourceQuery;
import com.nh.dsh.admin.model.vo.AuditResourceVO;
import com.nh.dsh.admin.model.vo.BookResourceVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 09:48
 **/

public interface BookResourceService extends IService<BookResourceEntity> {

    void save(BookResourceDTO dto);

    void update(BookResourceDTO dto);

    void deleteInBatch(List<Integer> ids);

    PageResult<BookResourceVO> page(BookResourceQuery query);

    ResponseEntity<byte[]> downloadResourcesQrCode(int bookId);

    void importResources(Integer bookId, MultipartFile file, int type);

    PageResult<AuditResourceVO> auditResourcePage(BookResourceQuery query);

    void resourceEnabled(BookResourceDTO dto);

    /**
     * 二维码资源审核
     * @return
     */
    Boolean auditResource();
}
