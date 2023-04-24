package com.huang.res;

import java.io.Serializable;

/**
 * @Time 2023-04-24
 * Created by Huang
 * className: R
 * Description:
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int SUCCESS = 1;
    private static final int ERROR = -1;
    private static final String SUCCESS_MESSAGE = "成功";
    private static final String ERROR_MESSAGE = "失败";

    private int code;
    private String message;
    private T data;

    public R(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> R<T> success() {
        return success(SUCCESS_MESSAGE, null);
    }

    public static <T> R<T> success(String message) {
        return success(message, null);
    }

    public static <T> R<T> success(T data) {
        return success(SUCCESS_MESSAGE, data);
    }

    public static <T> R<T> success(String message, T data) {
        return success(SUCCESS, message, data);
    }

    public static <T> R<T> success(int code, String message) {
        return success(code, message, null);
    }

    public static <T> R<T> success(int code, String message, T data) {
        return new R<>(code, message, data);
    }

    public static <T> R<T> error() {
        return error(ERROR_MESSAGE, null);
    }

    public static <T> R<T> error(String message) {
        return error(message, null);
    }

    public static <T> R<T> error(T data) {
        return error(ERROR_MESSAGE, data);
    }

    public static <T> R<T> error(String message, T data) {
        return error(ERROR, message, data);
    }

    public static <T> R<T> error(int code, String message) {
        return error(code, message, null);
    }

    public static <T> R<T> error(int code, String message, T data) {
        return new R<>(code, message, data);
    }

    @Override
    public String toString() {
        return "R{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}