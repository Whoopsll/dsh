package com.nh.dsh.admin.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {

    /**
     * 分页函数
     *
     * @param currentPage 当前页数
     * @param pageSize    每一页的数据条数
     * @param list        要进行分页的数据列表
     * @return 当前页要展示的数据
     */
    public static <T>  Page<T> getPages(Integer currentPage, Integer pageSize, List<T> list) {
        if (currentPage == null || pageSize == null || list == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        if (CollectionUtils.isEmpty(list)){
            return new Page<>();
        }

        Page<T> page = new Page<>();
        int size = list.size();


        if(pageSize > size) {
            pageSize = size;
        }

        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;

        if(currentPage > maxPage) {
            //currentPage = maxPage;
            return new Page<>();
        }

        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;

        List<T> pageList = new ArrayList<>();

        // 将当前页的数据放进pageList
        for(int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }

        page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return page;
    }
}
