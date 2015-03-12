package com.dh.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {
	public static void setter(String filedName, Object obj, String value) throws Exception {
		try {
			Class<?> clz = obj.getClass();
			Field field = getFiled(filedName, clz);
			field.setAccessible(true);

			if (field.getType().equals(String.class)) {
				field.set(obj, value);
			} else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
				if (value.length() == 0) {
					field.set(obj, 0);
				} else {
					field.set(obj, Long.valueOf(value));
				}

			} else {
				if (value.length() == 0) {
					field.set(obj, 0);
				} else {
					field.set(obj, Integer.valueOf(value));
				}

			}

			field.setAccessible(false);
		} catch (Exception e) {
			System.err.println(filedName + " error");
			throw e;
		}
	}

	public static void setter2(String fieldname, Object obj, String value) throws Exception {
		Class<?> clz = obj.getClass();
		Field field = getFiled(fieldname, clz);
		Method method = geSetterByFieldName(fieldname, clz, field.getType());

		if (field.getType().equals(String.class)) {
			method.invoke(obj, value);
		} else if (field.getType().equals(long.class) || field.getType().equals(Long.class)) {
			method.invoke(obj, Long.valueOf(value));
		} else {
			method.invoke(obj, Integer.valueOf(value));
		}
	}

	protected static Method geSetterByFieldName(String filedName, Class<?> clz, Class<?> pClz) throws NoSuchMethodException, SecurityException {
		String methodName = "set" + filedName.substring(0, 1).toUpperCase() + filedName.substring(1);

		Method[] s = clz.getDeclaredMethods();
		for (Method method : s) {
			if (method.getName().equals(methodName)) {
				methodName = method.getName();
				System.out.println(methodName);
			}
		}
		return clz.getDeclaredMethod(methodName, pClz);
	}

	protected static Field getFiled(String filedname, Class<?> clz) throws NoSuchFieldException, SecurityException {
		return clz.getDeclaredField(filedname);
	}
}
