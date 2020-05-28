package com.elca.vn.transform;

/**
 * Transform form {@code I} model to {@code O} model in 2 ways
 *
 * @param <I> input model
 * @param <O> output model
 */
public interface BaseTransformService<I, O> {

    /**
     * Transform form {@code I} model to {@code O}
     *
     * @param sourceObject input object
     * @return transformed object
     */
    O transformFromSourceToDes(I sourceObject);

    /**
     * Transform form {@code O} model to {@code I}
     *
     * @param destinationObject input object
     * @return transformed object
     */
    I transformFromDesToSource(O destinationObject);
}
