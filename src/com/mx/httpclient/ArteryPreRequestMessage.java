package com.mx.httpclient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 该类为所有的如参额request形式，其中根据paramesField为不同的具体消息类
 *
 * @author gaofeng
 */
public class ArteryPreRequestMessage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1554236792198227944L;
    //消息版本号
    private String msgVersion;
    //校验域
    private String macField;
    //签名域
    private String signField;
    //参数域
    private String paramesField;
    //前置域
    private String preField;


    public void setMsgVersion(String msgVersion) {
        this.msgVersion = msgVersion;
    }


    public void setMacField(String macField) {
        this.macField = macField;
    }


    public void setSignField(String signField) {
        this.signField = signField;
    }

    public void setPerField(String preField){
        this.preField = preField;
    }



    public void setParamesField(String paramesField) {
        this.paramesField = paramesField;
    }


    @Override
    public String toString() {
        return "BaseMassage [msgVersion=" + msgVersion + ", macField="
                + macField + ", signField=" + signField + ", paramesField="
                + paramesField + "]";
    }

    public String getJsonString() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgVersion", msgVersion);
        jsonObject.put("macField", macField);
        jsonObject.put("signField", signField);
        jsonObject.put("paramesField", paramesField);
        jsonObject.put("preField",preField);
        return jsonObject.toString();
    }

    public boolean verifyMac() {
        if (macField.equals("0000001")) {
            return true;
        }
        return false;
    }

    public boolean verifySign() {
        if (signField.equals("000001")) {
            return true;
        }
        return false;
    }


}
