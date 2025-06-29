package com.github.cloudgyb.javafxtest.database;

import java.util.List;

/**
 * 数据库分页 pojo
 *
 * @author cloudgyb
 * @since 2025/6/28 17:49
 */
public class Page<T> {
    private final long total;
    private final int pageNum;
    private final int pageSize;
    private final int totalPage;
    private final List<T> list;

    public Page(long total, int pageNum, int pageSize, List<T> list) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<T> getList() {
        return list;
    }
}
