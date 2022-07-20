package com.socialmedia.dao;

import java.lang.reflect.InvocationTargetException;

public class MyGeneric<T> {
	private T obj; // khởi tạo đối tượng lưu tham số generic
	public MyGeneric(Class<T> classObject) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        // lấy tên Class và gán nó vào đối tượng obj
        this.obj = (T) classObject.getDeclaredConstructor().newInstance();
    }
    public T getObj() {
        return obj; // trả về obj
    }

}
