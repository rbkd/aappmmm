package apm.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensitiveWordFilter {
	
	private static Pattern pattern = null;
	
	public static void main(String[] args){
		System.out.println(doFilter("强歼强h奸乱交色友婊子"));
	}

	static {
		StringBuffer patternBuf = new StringBuffer();
		try {
			InputStream in = SensitiveWordFilter.class.getClassLoader().getResourceAsStream("mgc.properties");
			Properties properties = new Properties();
			properties.load(in);

			Enumeration<?> enu = properties.propertyNames();
			while (enu.hasMoreElements()) {
				patternBuf.append((String) enu.nextElement() + "|"); // 读取所有properties里的词，以 | 分隔
			}

			patternBuf.deleteCharAt(patternBuf.length() - 1);

			// 默认下，properties文件读取编码： ISO8859-1
			pattern = Pattern.compile(new String(patternBuf.toString().getBytes("UTF-8"), "UTF-8"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String doFilter(String str) {
		try {
			Matcher m = pattern.matcher(str);
			str = m.replaceAll("**");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}