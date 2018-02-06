package org.wpy.utils;

import org.apache.http.util.Asserts;

import java.lang.reflect.Field;

public class CheckBean {


    public void check() throws IllegalAccessException {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object o = field.get(this);
            FieldNotNull annotation = field.getAnnotation(FieldNotNull.class);
            if (annotation != null)
                Asserts.notNull(o, field.getName());
            FieldNotEmpty annotation1 = field.getAnnotation(FieldNotEmpty.class);
            if (annotation1 != null && o instanceof String)
                Asserts.notEmpty(o.toString(), field.getName());
        }
    }


}
