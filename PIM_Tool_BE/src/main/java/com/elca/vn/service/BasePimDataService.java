package com.elca.vn.service;

import java.util.Iterator;
import java.util.List;

public interface BasePimDataService<T> {

    T importData(T project);

    T queryData(String id);

    Iterator<T> getData();

    long getTotalDataSize();

    long getTotalDataSize(String... contentSearch);

    List<T> findDataWithPaging(int indexPage, String... contentSearch);

    List<T> findAllDataWithPaging(int indexPage);
}
