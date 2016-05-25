/**
 * Copyright &copy; 2012-2013 Zaric All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package apm.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * <p>
 * 
 * @author zhangzuoqiang
 * 
 * @version 2013-05-22
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * 
	 * @param str
	 *            目标字符串
	 * @param length
	 *            截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : str.toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 获得i18n字符串
	 */
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder
				.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code,
				args, localLocale);
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (isNotBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	public static String nullToString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String lTrim(String str) {
		return str.replaceAll("^\\s*", "");
	}

	public static String rTrim(String str) {
		return str.replaceAll("\\s*$", "");
	}

	@SuppressWarnings({ "rawtypes" })
	public static String join(Object items, String splitChar) {
		StringBuffer sb = new StringBuffer();
		if (items instanceof Collection) {
			Collection collectionItems = (Collection) items;
			int index = 0;
			for (Object collectionItem : collectionItems) {
				if (++index > 1) {
					sb.append(splitChar);
				}
				sb.append(nullToString(collectionItem));
			}
		} else {
			Object[] arrayItems = (Object[]) items;
			for (int i = 0; i < arrayItems.length; i++) {
				if (i > 0) {
					sb.append(splitChar);
				}
				sb.append(nullToString(arrayItems[i]));
			}
		}
		return sb.toString();
	}

	public static String toSqlJoinString(String joinString, String splitChar) {
		if (null == joinString)
			return "''";
		String[] arr = joinString.split(splitChar);
		StringBuffer sb = new StringBuffer();
		int index = 0;
		for (String str : arr) {
			if (index++ > 0) {
				sb.append(",");
			}
			sb.append("'" + str + "'");
		}
		return sb.toString();
	}

	public static String empty2Other(Object obj, String replaceValue) {
		String value = nullToString(obj);
		if (value.trim().isEmpty()) {
			return replaceValue;
		}
		return value;
	}

	public static String getJsonString(Object str) {

		return nullToString(str).replaceAll("\r", "\\\\r")
				.replaceAll("\n", "\\\\n").replaceAll("\\\\", "\\\\\\\\");
	}

	public static String traceExceptionMessage(Object source, Throwable e) {
		String newLine = System.getProperty("line.separator");
		StringBuffer exceptionInfo_sb = new StringBuffer(source.toString());
		exceptionInfo_sb.append(newLine + e.toString());
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			exceptionInfo_sb.append(newLine + "\tat " + trace[i]);
		}
		return exceptionInfo_sb.toString();
	}

	public static String getClearWhereSQL(String table_sql_temp) {
		Pattern p = Pattern.compile("\\s*where\\s*1\\s*=\\s*1\\s*and?",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		table_sql_temp = p.matcher(table_sql_temp).replaceAll(" _W_H_E_R_E_ ");
		p = Pattern.compile("\\s*where\\s*1\\s*=\\s*1\\s*",
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		table_sql_temp = p.matcher(table_sql_temp).replaceAll(" ");
		p = Pattern.compile(" _W_H_E_R_E_ ", Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE);
		table_sql_temp = p.matcher(table_sql_temp).replaceAll(" WHERE ");
		return table_sql_temp;
	}

	public static boolean isExistElement(Object[] arr, Object findValue) {
		boolean isFound = false;
		for (Object value : arr) {
			if (findValue.equals(value)) {
				isFound = true;
			}
			break;
		}
		return isFound;
	}

	public static Collection<String> getCollectionByString(
			String collectionType, String sourceString, String splitString) {
		Collection<String> collection = null;
		if ("set".equalsIgnoreCase(collectionType)) {
			collection = new HashSet<String>();
		} else if ("list".equals(collectionType)) {
			collection = new ArrayList<String>();
		}
		if (null == sourceString) {
			return collection;
		}
		for (String str : sourceString.split(splitString)) {
			collection.add(str);
		}
		return collection;
	}

	public static Collection<Long> getLongCollectionByString(
			String collectionType, String sourceString, String splitString) {
		Collection<Long> collection = null;
		if ("set".equalsIgnoreCase(collectionType)) {
			collection = new HashSet<Long>();
		} else if ("list".equals(collectionType)) {
			collection = new ArrayList<Long>();
		}
		for (String str : sourceString.split(splitString)) {
			collection.add(Long.valueOf(str));
		}
		return collection;
	}

}
