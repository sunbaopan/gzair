package com.gz.gzair.util;

import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/****
 * 用的注册、登陆等和web进行交换的将在这个类里面进行
 * @author sbp
 *
 */
public class WebServiceUtil {

    // 命名空间
    private static final String NAMESPACE = "http://wl.yitoa.com/";
    // WebService地址
    private static String URL = "http://60.173.247.120:82/yitoawl/webservices/reservationService?wsdl";

    private static final String METHOD_NAME = "getNetIp";

    private static String SOAP_ACTION = NAMESPACE + METHOD_NAME;
    
    
    private static final String WIP_METHOD_NAME = "getWIP";

    private static String WIP_SOAP_ACTION = NAMESPACE + WIP_METHOD_NAME;
    
    
    private static final String LOGIN_METHOD_NAME = "login";

    private static String LOGIN_SOAP_ACTION = NAMESPACE + LOGIN_METHOD_NAME;
    
    
    private static final String QUERYSB_METHOD_NAME = "querySbByUserId";

    private static String QUERYSB__SOAP_ACTION = NAMESPACE + QUERYSB_METHOD_NAME;
  
    private static final String VER_METHOD_NAME = "getVersion";

    private static String VER__SOAP_ACTION = NAMESPACE + VER_METHOD_NAME;
    
    private static final String NET_METHOD_NAME = "areaNetWork";

    private static String NET__SOAP_ACTION = NAMESPACE + NET_METHOD_NAME;
    
    
    private static  final String ADD_IP_NAME="addISIP";
    
    private static String ADDIP__SOAP_ACTION=NAMESPACE + ADD_IP_NAME;
    
    
    /***
     * 在添加设备的时候根据设备序列号和userid来判断是否需要添加
     * @param wlsn
     * @param userid
     * @return
     */
    public String addISIP(String wlsn,String userid) {
        String ss = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, ADD_IP_NAME);
            rpc.addProperty("wlsn", wlsn);
            rpc.addProperty("userid", userid);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            // envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(ADDIP__SOAP_ACTION, envelope);
            ss = envelope.getResponse().toString();
        } catch (Exception e) {
            return null;
        }
        return ss;
    }
    
    
    
    /***
     * 获取版本号
     * @param ver
     * @return
     */
    public String getVersion(String ver) {
        String ss = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, VER_METHOD_NAME);
            rpc.addProperty("ver", ver);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            // envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(VER__SOAP_ACTION, envelope);
            ss = envelope.getResponse().toString();
        } catch (Exception e) {
            return null;
        }
        return ss;
    }
    
 
    public String getNetIp(String str) {

        String ss = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("testStr", str);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            // envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(SOAP_ACTION, envelope);
            ss = envelope.getResponse().toString();
        } catch (Exception e) {
            return null;
        }
        return ss;
    }
    
    
    
    
    /***
     * 获取访问设备的外网ip
     * @return
     */
    public String getWIP() {

        String ss = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, WIP_METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            // envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(WIP_SOAP_ACTION, envelope);
            ss = envelope.getResponse().toString();
        } catch (Exception e) {
            return null;
        }
        return ss;
    }
    
   
    
    public String login(String username,String password){
        String ss = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, LOGIN_METHOD_NAME);
            rpc.addProperty("username", username);
            rpc.addProperty("password", password);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            // envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(LOGIN_SOAP_ACTION, envelope);
            ss = envelope.getResponse().toString();
        } catch (Exception e) {
            return null;
        }
        return ss;
    }
    
    
    
    
    public String querySbByUserId(String userId){
        String ss = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, QUERYSB_METHOD_NAME);
            rpc.addProperty("userId", userId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            // envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(QUERYSB__SOAP_ACTION, envelope);
            ss =  envelope.getResponse().toString();
        } catch (Exception e) {
            return null;
        }
        return ss;
    }
    
    /****
     * 判断手机和设备是否在同一局域网
     * @param wlsn
     * @param phoneMac
     * @param userId
     * @return
     */
    public String areaNetWork(String wlsn,String phoneMac,String userId){
        String ss = null;
        try {
            SoapObject rpc = new SoapObject(NAMESPACE, NET_METHOD_NAME);
            rpc.addProperty("wlsn", wlsn);
            rpc.addProperty("phoneMac", phoneMac);
            rpc.addProperty("userId", userId);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            // envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(URL);
            ht.debug = true;
            ht.call(NET__SOAP_ACTION, envelope);
            ss =  envelope.getResponse().toString();
        } catch (Exception e) {
            return null;
        }
        return ss;
    }
    
}
