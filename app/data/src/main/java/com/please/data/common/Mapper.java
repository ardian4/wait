package com.please.data.common;

//we can use io.reactivex.functions.Function but lets be less dependent from 3 party libs
public interface Mapper<R, V> {
    R map(V value);
}
