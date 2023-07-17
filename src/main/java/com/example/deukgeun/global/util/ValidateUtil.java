package com.example.deukgeun.global.util;

import java.lang.reflect.Field;

public class ValidateUtil {
    /**
     * 객체의 필드 값을 가져옵니다.
     *
     * @param object    값을 가져올 객체
     * @param fieldName 필드 이름
     * @return 필드의 값 (문자열 형식)
     */
    public static String getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        Field dateField;
        try {
            dateField = clazz.getDeclaredField(fieldName);
            dateField.setAccessible(true);
            Object target = dateField.get(object);

            if (target == null) {
                return null;
            }

            if ((target instanceof Enum)) {
                return target.toString();
            }

            if (!(target instanceof String)) {
                return null;
            }

            return target.toString();
        } catch (NoSuchFieldException e) {
            System.out.println("NoSuchFieldException : " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException : " + e.getMessage());
        }

        return null;
    }
}
