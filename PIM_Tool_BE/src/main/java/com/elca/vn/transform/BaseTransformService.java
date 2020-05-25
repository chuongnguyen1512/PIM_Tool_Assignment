package com.elca.vn.transform;

public interface BaseTransformService<I, O> {

    O transformFromSourceToDes(I sourceObject);

    I transformFromDesToSource(O destinationObject);
}
