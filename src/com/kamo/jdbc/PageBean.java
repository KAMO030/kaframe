package com.kamo.jdbc;

import java.util.List;

public class PageBean<T> {
    private List<T> datas;
    private Integer currentPage ;
    private Integer pageSize;
    private Integer totalPage;
    private Integer totalCount;

    public List<T> getDatas() {
        return datas;
    }

    public PageBean() {
    }

    public PageBean(List<T> datas, Integer currentPage, Integer pageSize, Integer totalPage, Integer totalCount) {
        this.datas = datas;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;

    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "datas=" + datas +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalCount=" + totalCount +
                '}';
    }
}
