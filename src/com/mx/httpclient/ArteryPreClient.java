package com.mx.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.res.AssetManager;

import com.mx.util.exception.MXParameterNullException;

public class ArteryPreClient {

    private ArteryPreTrans mHttpTransMit;

    private static ArteryPreClient mJwsHttpClient;

    private ArteryPreClient(){
        mHttpTransMit = ArteryPreTrans.getInstance();
    }
    
    public static String getVersion() {
		return "1.0.1";
	}

    public static ArteryPreClient getInstance(){
        if (mJwsHttpClient == null){
            mJwsHttpClient = new ArteryPreClient();
        }
         
        return mJwsHttpClient;
    }

    public void connect() {
        //预留函数
    }

    public void disconnect() {
        //预留函数
    }
    
    public void initEnvironment(Activity activity,
			ArteryPreTrans.ClientMode mode) throws IOException,
			MXParameterNullException {

		if (activity == null) {
			throw new MXParameterNullException("acticity is null");
		}

		ArteryPreClient arteryPreClient = ArteryPreClient.getInstance();
		if (mode == ArteryPreTrans.ClientMode.PRO) {
			AssetManager am = activity.getAssets();
			InputStream ins = am.open("mxproca.bks");
			arteryPreClient.setClientCert(ins, ArteryPreDef.MODE_PRO);
			ins.close();
		} else if (mode == ArteryPreTrans.ClientMode.TEST) {
			AssetManager am = activity.getAssets();
			InputStream ins = am.open("mxtestca.bks");
			arteryPreClient.setClientCert(ins, ArteryPreDef.MODE_TEST);
			ins.close();
		}
	}

    /**
     *
     * @return
     */
    public boolean setClientCert(InputStream certStream,int nMode) {
        // 读取证书
        return mHttpTransMit.setHttpsMothed(certStream,nMode);

    }
    
    public Map<String, Object> tradeInit(Map<String, Object> paramesField,Map<String, Object> preField) {
    	return mHttpTransMit.tradeInit(paramesField, preField);
    }
    
    public Map<String, Object> applyOrder(Map<String, Object> paramesField,Map<String, Object> preField) {
    	return mHttpTransMit.applyOrder(paramesField, preField);
    }
    
    public Map<String, Object> payInit(Map<String, Object> paramesField,Map<String, Object> preField) {
    	return mHttpTransMit.payInit(paramesField, preField);
    }

    public Map<String, Object> queryPayStatus(Map<String, Object> paramesField,Map<String, Object> preField) {
    	return mHttpTransMit.queryPayStatus(paramesField, preField);
    }
    
    public Map<String, Object> merchantDo(Map<String, Object> paramesField,Map<String, Object> preField) {
    	return mHttpTransMit.merChantDo(paramesField, preField);
    }
    
    public Map<String, Object> ctticDoLoadAction(Map<String, Object> paramesField,Map<String, Object> preField) {
    	return mHttpTransMit.ctticDoLoadAction(paramesField, preField);
    }
    
    public Map<String, Object> queryTradeStatus(String tradeId) {
    	Map<String, Object> paramesField = new HashMap<String, Object>();
    	paramesField.put(ArteryPreDef.FIELD_tradeIdList, tradeId);
    	Map<String, Object> preField = new HashMap<String, Object>();
    	preField.put("merId", "200002");
    	preField.put("method", "queryTradeStatus");
    	return mHttpTransMit.assignMethod(paramesField, preField);
    }
    
    public Map<String, Object> queryRate(String merId,String payId) {
    	Map<String, Object> paramesField = new HashMap<String, Object>();
    	paramesField.put("payId", payId);
    	Map<String, Object> preField = new HashMap<String, Object>();
    	preField.put("merId", merId);
    	preField.put("method", "queryRate");
    	return mHttpTransMit.assignMethod(paramesField, preField);
    }
}
