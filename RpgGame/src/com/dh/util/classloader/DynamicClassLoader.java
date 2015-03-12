package com.dh.util.classloader;

import java.io.InputStream;

public class DynamicClassLoader extends ClassLoader {
	public Class<?> findClass(byte[] b) throws ClassNotFoundException {
		return defineClass(null, b, 0, b.length);
	}

	public Class<?> instantiateClass(String name, InputStream fin, long len) throws Exception {
		byte[] raw = new byte[(int) len];
		fin.read(raw);
		fin.close();
		return defineClass(name, raw, 0, raw.length);
	}
}