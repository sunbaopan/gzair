
package com.gz.gzair.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
/****
 * �¶�api
 * @author sbp
 *
 */
public class TempUtil {

    /***
     * ������ҳ��URLץȡ����
     */
    public String getTemp(String tempurl) {
        URLConnection conn = null;
        String str = null;
        try
        {
            URL url = new URL(tempurl);
            // ��URL����
            conn = url.openConnection();
            // ʹ��InputStream����URLConnection��ȡ����
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            // ��ByteArrayBuffer����
            ByteArrayBuffer baf = new ByteArrayBuffer(102400);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            str = EncodingUtils.getString(baf.toByteArray(), "gbk");
            int index = str.indexOf("<span id="+"\"t_temp\""+">");
            if (index != -1) {
                str = str.substring(index + 15, index + 80).trim();
                int  firstpos=str.indexOf("��");
                int  lastpos=str.lastIndexOf("��");
                String firstTemp=str.substring(firstpos-2, firstpos);
                String lastTemp=str.substring(lastpos-2, lastpos);
                String firstMath = firstTemp.substring(0, 1);// ȡ��һ���ַ����ж��Ƿ�Ϊ����
                if(!isNumber(firstMath)){//�����֣��Ǿ�Ҫ���ж�һ��������2��λ�ÿ�������
                    String lastMath=str.substring(2,3);
                    if(!isNumber(lastMath)){//���һ����������,�жϵڶ�Ϊ�ǲ�������
                        String secondMath=str.substring(1,2);
                        if(isNumber(secondMath)) {
                            str=str.substring(0, 2);//ȡǰ2λ
                        } else{
                           str=firstMath;//ȡ��һλ
                        }
                    }
                    
                }else{//���������Ǿ�˵����ȡ��aqi�Ѿ��Ǵ������������
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
    
    //�ж��Ƿ�Ϊ����
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
