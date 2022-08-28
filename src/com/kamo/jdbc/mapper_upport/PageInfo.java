package com.kamo.jdbc.mapper_upport;

import java.util.List;

public class PageInfo<T> {

    private Integer currentPage;
    private Boolean isFistPage;
    private Boolean isLastPage;
    private Integer startPage;
    private Integer pageSize;
    private Integer totalPage;
    private Integer totalCount;
    private List<Integer> pageNumbers;
    private List<T> dataList;

    public PageInfo(Integer currentPage,
                    Boolean isFistPage, Boolean isLastPage,
                    Integer startPage, Integer pageSize,
                    Integer totalPage, Integer totalCount,
                    List<Integer> pageNumbers, List<T> dataList) {
        this.currentPage = currentPage;
        this.isFistPage = isFistPage;
        this.isLastPage = isLastPage;
        this.startPage = startPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.pageNumbers = pageNumbers;
        this.dataList = dataList;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Boolean getFistPage() {
        return isFistPage;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public  List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public List<T> getDataList() {
        return dataList;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "currentPage=" + currentPage +
                ", isFistPage=" + isFistPage +
                ", isLastPage=" + isLastPage +
                ", startPage=" + startPage +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalCount=" + totalCount +
                ", pageNumbers=" +  pageNumbers +
                ", dataList=" + dataList +
                '}';
    }
}
