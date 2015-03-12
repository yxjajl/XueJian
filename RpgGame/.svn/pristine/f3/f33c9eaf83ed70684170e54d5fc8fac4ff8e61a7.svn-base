package com.dh.fx;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.protobuf.GeneratedMessage;

public class ProtocolParseForm {
	private static Logger logger = Logger.getLogger(ProtocolParseForm.class);
	private static HashMap<String, Method> map = new HashMap<String, Method>();

	@SuppressWarnings("unchecked")
	public static <T> T parseFrom(Class<? extends GeneratedMessage> c, byte[] bytes) throws Exception {
		try {
			Method m = map.get(c.getSimpleName());
			if (m == null) {
				m = c.getMethod("parseFrom", new Class[] { byte[].class });
				map.put(c.getSimpleName(), m);
			}
			return (T) m.invoke(c, new Object[] { bytes });
		} catch (Exception e) {
			logger.error("协议解码异常", e);
			throw e;
		}
	}

}
