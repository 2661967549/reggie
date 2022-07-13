package com.zyp.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类，服务端响应结果数据都会封装成此对象
 * @param <T>
 */
@Data
public class R <T> implements Serializable {
    private Integer code; //0失败，1成功
    private String msg; //错误消息
    private T data;
    private Map map = new HashMap();  //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}


















