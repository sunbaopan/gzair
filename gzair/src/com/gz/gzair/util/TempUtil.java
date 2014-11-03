
package com.gz.gzair.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
/****
 * 温度api
 * @author sbp
 *
 */
public class TempUtil {

    /***
     * 根据网页的URL抓取数据
     */
    public String getTemp(String tempurl) {
        URLConnection conn = null;
        String str = null;
        try
        {
            URL url = new URL(tempurl);
            // 打开URL链接
            conn = url.openConnection();
            // 使用InputStream，从URLConnection读取数据
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            // 用ByteArrayBuffer缓存
            ByteArrayBuffer baf = new ByteArrayBuffer(102400);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            str = EncodingUtils.getString(baf.toByteArray(), "gbk");
            int index = str.indexOf("<span id="+"\"t_temp\""+">");
            if (index != -1) {
                str = str.substring(index + 15, index + 80).trim();
                int  firstpos=str.indexOf("℃");
                int  lastpos=str.lastIndexOf("℃");
                String firstTemp=str.substring(firstpos-2, firstpos);
                String lastTemp=str.substring(lastpos-2, lastpos);
                String firstMath = firstTemp.substring(0, 1);// 取第一个字符，判断是否为数字
                if(!isNumber(firstMath)){//是数字，那就要来判断一下其他的2个位置可是数字
                    String lastMath=str.substring(2,3);
                    if(!isNumber(lastMath)){//最后一个不是数字,判断第二为是不是数字
                        String secondMath=str.substring(1,2);
                        if(isNumber(secondMath)) {
                            str=str.substring(0, 2);//取前2位
                        } else{
                           str=firstMath;//取第一位
                        }
                    }
                    
                }else{//不是数字那就说明获取的aqi已经是错误的抛弃掉吧
                    str = null;
                }
            } else {
                str = null;
            }
            bis.close();
            is.close();
        } catch (Exception ee)
        {
            System.out.print("ee:" + ee.getMessage());
        }
        return str;
    }
    
    //判读是否为数字
    public boolean isNumber(String str)
    {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
