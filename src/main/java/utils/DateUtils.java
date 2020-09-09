/** 
 * @Package contract 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月4日 下午3:37:39 
 * @version V1.0 
 */ 
package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月4日 下午3:37:39 
 */
public class DateUtils {
	 /** 
	  * 时间戳转换成日期格式字符串 
	 * @Description 
	 * @param seconds 精确到秒的字符串
	 * @param format
	 * @return 
	 * @return String  
	 * @author shuxin
	 * @date 2018年1月18日 下午3:05:28 
	 */ 
	public static String timeStamp2Date(String seconds,String format) {  
		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
			return "";  
		}  
		if(format == null || format.isEmpty()){
			format = "yyyy-MM-dd HH:mm:ss";
		}   
		SimpleDateFormat sdf = new SimpleDateFormat(format);  
		return sdf.format(new Date(Long.valueOf(seconds+"000")));  
	}  
	
}
