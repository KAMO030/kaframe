package com.kamo.jdbc.mapper_support;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class IPage<T> implements List<T> {
    private List<T> dataList = Collections.emptyList();;
    private Integer currentPage;
    private Boolean isFistPage;
    private Boolean isLastPage;
    private Integer startPage;
    private Integer pageSize;
    private Integer totalPage;
    private Integer totalCount;

    private Integer[] pageNumbers = new Integer[5];

    public IPage(Integer currentPage, Integer pageSize) {
        this.pageSize = pageSize;
        init(currentPage);
    }

    public IPage() {
    }

    private void init(Integer currentPage) {
        this.currentPage = currentPage <= 1 ? 1 : currentPage;
        this.startPage = PageHelper.getStartPage(currentPage, pageSize);
        this.isFistPage = currentPage <= 1;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Integer getCurrentPage() {
        return currentPage;
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

    public Integer[] getPageNumbers() {
        return pageNumbers;
    }

    public void setTotalCount(Integer totalCount) {
        if (startPage == null) {
            init(currentPage);
        }
        this.totalCount = totalCount;
        this.totalPage = PageHelper.getTotalPage(totalCount, pageSize);
        this.currentPage = currentPage > totalPage ? totalPage : currentPage;
        this.startPage = PageHelper.getStartPage(currentPage, pageSize);
        this.isLastPage = this.currentPage == this.totalPage;
        initPageNumbers();
    }

    private void initPageNumbers() {
        int length = pageNumbers.length;
        int half = length / 2;
        if (totalPage < length) {
            for (int i = 1; i <= totalPage; i++) {
                pageNumbers[i - 1] = i;
            }
        } else {
            Integer p = currentPage - half;
            if (currentPage + half > totalPage) {
                p = currentPage - (currentPage + half) + totalPage - half;
            }
            if (p <= 0) {
                p = 1;
            }
            for (int i = 0; i < length && p + i <= totalPage; i++) {
                pageNumbers[i] = p + i;
            }
        }


    }
    public PageInfo<T> getPageInfo() {

        List<Integer> pageNumberList = new ArrayList<>();
        for (Integer pageNumber : pageNumbers) {
            if (pageNumber==null) {
                break;
            }
            pageNumberList.add(pageNumber);
        }
        return new PageInfo<T>(currentPage,isFistPage,isLastPage,startPage,pageSize,totalPage,totalCount, pageNumberList,dataList);
    }
    @Override
    public String toString() {
        return "IPage{ currentPage=" + currentPage +
                ", isFistPage=" + isFistPage +
                ", isLastPage=" + isLastPage +
                ", startPage=" + startPage +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalCount=" + totalCount +
                "dataList=" + dataList +
                ", pageNumbers=" + Arrays.toString(pageNumbers) +
                '}';
    }

    @Override
    public int size() {
        return dataList.size();
    }

    @Override
    public boolean isEmpty() {
        return dataList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return dataList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return dataList.iterator();
    }

    @Override
    public Object[] toArray() {
        return dataList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return dataList.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return dataList.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return dataList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return dataList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return dataList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return dataList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return dataList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return dataList.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        dataList.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        dataList.sort(c);
    }

    @Override
    public void clear() {
        dataList.clear();
    }

    @Override
    public boolean equals(Object o) {
        return dataList.equals(o);
    }


    @Override
    public T get(int index) {
        return dataList.get(index);
    }

    @Override
    public T set(int index, T element) {
        return dataList.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        dataList.add(index, element);
    }

    @Override
    public T remove(int index) {
        return dataList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return dataList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return dataList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return dataList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return dataList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return dataList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return dataList.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return dataList.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return dataList.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return dataList.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        dataList.forEach(action);
    }
}
