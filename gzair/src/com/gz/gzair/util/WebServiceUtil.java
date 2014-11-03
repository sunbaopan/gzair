package com.gz.gzair.util;

import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/****
 * �õ�ע�ᡢ��½�Ⱥ�web���н����Ľ���������������
 * @author sbp
 *
 */
public class WebServiceUtil {

    // �����ռ�
    private static final String NAMESPACE = "http://wl.yitoa.com/";
    // WebService��ַ
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
     * ������豸��ʱ������豸���кź�userid���ж��Ƿ���Ҫ���
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
     * ��ȡ�汾��
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
     * ��ȡ�����豸������ip
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
     * �ж��ֻ����豸�Ƿ���ͬһ������
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
