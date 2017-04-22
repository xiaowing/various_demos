package org.wingsdak.spbtmybatis.utility;

import java.lang.reflect.Field;

import javax.validation.constraints.NotNull;

public class ReflectionUtil {
    public static <T> T getFieldValue(@NotNull Object object,
            @NotNull String fullName) throws IllegalAccessException {
    			return getFieldValue(object, fullName, false);
    }

    public static <T> T getFieldValue(@NotNull Object object,
            @NotNull String fieldName,
            boolean traceable) throws IllegalAccessException {
    	Field field;
    	String[] fieldNames = fieldName.split("\\.");
    	for (String targetField : fieldNames) {
    		field = searchField(object.getClass(), targetField, traceable);
    		if (field == null)
    			return null;

    		object = getValue(object, field);
    	}

    	return (T) object;
    }

    private static Field searchField(Class c, String targetField, boolean traceable) {
    	do {
    		Field[] fields = c.getDeclaredFields();
    		for (Field f : fields) {
    			if (f.getName().equals(targetField)) {
    				return f;
    			}
    		}
    		c = c.getSuperclass();
    		traceable = traceable && c != Object.class;
    	} while (traceable);

    	return null;
    }

    private static <T> T getValue(Object target, Field field) 
    		throws IllegalAccessException {
    	if (!field.isAccessible())
    		field.setAccessible(true);
    	return (T) field.get(target);
    }

    public static boolean setFieldValue(@NotNull Object target,
              @NotNull String fieldName,
              @NotNull Object value) throws IllegalAccessException {
    	return setFieldValue(target, fieldName, value, false);
    }

    public static boolean setFieldValue(@NotNull Object target,
              @NotNull String fieldName,
              @NotNull Object value,
              boolean traceable) throws IllegalAccessException {
    	Field field = searchField(target.getClass(), fieldName, traceable);
    	if (field != null)
    		return setValue(field, target, value);
    	return false;
    }

    private static boolean setValue(Field field, Object target, Object value) throws IllegalAccessException {
    	if (!field.isAccessible())
    		field.setAccessible(true);
    	field.set(target, value);
    	return true;
    }	
}
