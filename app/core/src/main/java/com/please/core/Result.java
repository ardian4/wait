package com.please.core;

import androidx.annotation.NonNull;

public class Result<T> {

    public static final int RESULT_PROGRESS = 0;
    public static final int RESULT_SUCCESS = -1;
    public static final int RESULT_FAIL = 2;
    public static final int RESULT_CANCELLED = 3;

    private final int mStatus;
    private final T mData;
    private final Throwable mThrowable;

    private Result(int status, T data, Throwable exception) {
        mStatus = status;
        mData = data;
        mThrowable = exception;
    }

    public static <T> Result<T> successOf(T data) {
        return new Result<>(RESULT_SUCCESS, data, null);
    }

    public static <T> Result<T> cancelled() {
        return new Result<>(RESULT_CANCELLED, null, null);
    }

    public static <T> Result<T> cancelled(Result<T> result) {
        return new Result<>(RESULT_CANCELLED, result.mData, null);
    }

    public static <T> Result<T> errorOf(Throwable exception) {
        return new Result<>(RESULT_FAIL, null, exception);
    }

    public static <T> Result<T> errorOf(T data, Throwable exception) {
        return new Result<>(RESULT_FAIL, data, exception);
    }

    public static <T> Result<T> progress() {
        return new Result<>(RESULT_PROGRESS, null, null);
    }

    public static <T> Result<T> progress(T actualData) {
        return new Result<>(RESULT_PROGRESS, actualData, null);
    }

    public int getStatus() {
        return mStatus;
    }

    public Throwable getException() {
        return mThrowable;
    }

    public T getData() {
        return mData;
    }

    @NonNull
    @Override
    public String toString() {
        return "Result{" +
                "mStatus=" + mStatus +
                ", mData=" + mData +
                ", mThrowable=" + mThrowable +
                '}';
    }
}
