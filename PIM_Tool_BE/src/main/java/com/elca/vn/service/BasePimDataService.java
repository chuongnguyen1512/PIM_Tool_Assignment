package com.elca.vn.service;

import java.util.Iterator;
import java.util.List;

public interface BasePimDataService<T> {

    T importData(T project);

    List<T> queryData(String id);

    Iterator<T> getData();
}
