package com.dh.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.builder.xml.dynamic.ForEachSqlNode;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SqlBuild {
	// private Logger logger = LoggerFactory.getLogger(SqlBuild.class);
	// this.sqlBuild.getSql( ActivitiesItemMapper.class.getName()+".insertItem",
	// item)
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	/**
	 * 从mybatis底层获取sql语句
	 * 
	 * @param id
	 * @param parameterObject
	 * @return
	 * @throws Exception
	 */
	public String getSql(String id, Object parameterObject) {

		MappedStatement ms = sqlSessionFactory.getConfiguration().getMappedStatement(id);

		BoundSql boundSql = ms.getBoundSql(parameterObject);
		String sql = boundSql.getSql();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Object[] parameterArray = new Object[parameterMappings.size()];
			MetaObject metaObject = parameterObject == null ? null : MetaObject.forObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = MetaObject.forObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					parameterArray[i] = value;
				}
			}
			// String[] sqls = sql.split("[?]"+"");
			// if (sqls.length == parameterArray.length) {
			// for (int j = 0; j < parameterArray.length; j++) {
			// Object object = parameterArray[j];
			// if (object instanceof Integer)
			// sqls[j] = sqls[j] + object.toString();
			// else if (object instanceof Float)
			// sqls[j] = sqls[j] + object.toString();
			// else if (object instanceof Double)
			// sqls[j] = sqls[j] + object.toString();
			// else if (object instanceof Long)
			// sqls[j] = sqls[j] + object.toString();
			// else if (object instanceof Date)
			// sqls[j] = sqls[j] + new
			// StringBuffer().append("'").append(DateUtil.formatDate((Date)
			// object, DateUtil.DAY_HOUR_FORMAT_STRING)).append("'").toString();
			// else {
			// sqls[j] = sqls[j] + "'" + object.toString() + "'";
			// }
			//
			// }
			// StringBuffer sqlBuffer = new StringBuffer("");
			// for (String string : sqls) {
			// sqlBuffer = sqlBuffer.append(string);
			// }
			// sql = sqlBuffer.toString();
			// }
			for (Object object : parameterArray) {
				if (object instanceof Integer)
					sql = sql.replaceFirst("[?]", new StringBuffer().append(object).toString());
				else if (object instanceof Float)
					sql = sql.replaceFirst("[?]", new StringBuffer().append(object).toString());
				else if (object instanceof Double)
					sql = sql.replaceFirst("[?]", new StringBuffer().append(object).toString());
				else if (object instanceof Long)
					sql = sql.replaceFirst("[?]", new StringBuffer().append(object).toString());
				else if (object instanceof Date)
					sql = sql.replaceFirst("[?]", new StringBuffer().append("'").append(DateUtil.formatDate((Date) object, DateUtil.DAY_HOUR_FORMAT_STRING)).append("'").toString());
				else {
					if (object != null) {
						object = java.util.regex.Matcher.quoteReplacement(MYSQLEncoder.encode(object == null ? "" : object.toString()));
						object = object.toString().replaceAll("\\u003F", "\\\\u003F");
						sql = sql.replaceFirst("[?]", "'" + new StringBuffer().append(object) + "'");
					} else {
						sql = sql.replaceFirst("[?]", new StringBuffer().append(object).toString());
					}

				}
			}
		}
		sql = sql.replaceAll("u003F", "?");
		if (StringUtils.isEmpty(sql)) {
			throw new RuntimeException("sql 为空");
		}

		return sql;
	}

}

class MYSQLEncoder {

	private static Map<String, String> referencesMap = new HashMap<String, String>();

	static {
		referencesMap.put("'", "\\'");
		referencesMap.put("\"", "\\\"");
		referencesMap.put("\\", "\\\\");

		referencesMap.put("\n", "\\\n");
		referencesMap.put("\0", "\\\0");
		referencesMap.put("\b", "\\\b");
		referencesMap.put("\r", "\\\r");
		referencesMap.put("\t", "\\\t");
		referencesMap.put("\f", "\\\f");
	}

	// escape sql tag with the source code.
	public static String encode(String source) {
		if (source == null)
			return "";

		StringBuffer sbuffer = new StringBuffer(source.length());

		for (int i = 0; i < source.length(); i++) {
			String c = source.substring(i, i + 1);

			if (referencesMap.get(c) != null) {
				sbuffer.append(referencesMap.get(c));
			} else {
				sbuffer.append(c);
			}
		}
		return sbuffer.toString();
	}
}