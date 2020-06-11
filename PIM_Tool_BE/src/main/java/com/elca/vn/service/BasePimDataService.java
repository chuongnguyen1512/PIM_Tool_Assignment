package com.elca.vn.service;

import java.util.List;

/**
 * Base service for handle data
 *
 * @param <T>
 */
public interface BasePimDataService<T> {

    T importData(T project);

    T updateData(T project);

    T queryData(String id);

    Iterable<T> getData();

    Iterable<T> getData(List<String> ids);

    long getTotalDataSize();

    long getTotalDataSize(String... contentSearch);

    Iterable<T> findDataWithPaging(int indexPage, String... contentSearch);

    Iterable<T> findAllDataWithPaging(int indexPage);

    int deleteData(List<Integer> deleteIDs);
}
