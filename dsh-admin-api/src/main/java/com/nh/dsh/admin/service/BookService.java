package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.dto.BookDTO;
import com.nh.dsh.admin.model.entity.BookEntity;
import com.nh.dsh.admin.model.query.BookQuery;
import com.nh.dsh.admin.model.vo.BookVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 12:43
 **/

public interface BookService extends IService<BookEntity> {

    void saveBook(BookDTO dto);

    void updateBook(BookDTO dto);

    void deleteBookInBatch(List<Integer> ids);

    PageResult<BookVO> page(BookQuery query, String authorization);

    List<BookVO> selectedList(String authorization);

    /**
     * 置顶图书
     * @param bookId
     * @return
     */
    boolean stickyBook(int bookId);
}
