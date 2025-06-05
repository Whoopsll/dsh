package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.entity.BookExchangeEntity;
import com.nh.dsh.admin.model.query.BookExchangeQuery;
import com.nh.dsh.admin.model.vo.BookExchangeVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-30 15:38
 **/

public interface BookExchangeService extends IService<BookExchangeEntity> {

    PageResult<BookExchangeVO> page(BookExchangeQuery query);

    void exchange(Integer id);

    void deleteInBatch(List<Integer> ids);

    void bookLinkImport(Integer bookId, MultipartFile file);

    ResponseEntity<byte[]> downloadExchangeQrCode(int bookId);

    void downloadExchangeXlsx(Integer bookId, HttpServletResponse response);

    String generateQrCodeLink(BookExchangeEntity bookExchange);
}
