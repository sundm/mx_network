package com.mx.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.mx.util.MXBaseUtil;
import com.mx.util.MXLog;

public class ArteryPreTrans {

	private static final String TAG = "ArteryTrans";

	private static String ARTERY_SRV_URL_HEADER;
	private static final String ARTERY_BASE_URL_TEST = "http://120.26.102.22:8080/mobilePre/";
	private static final String ARTERY_BASE_URL_PRO = "https://120.26.85.211:8443/mobilePre/";
	private static final String ARTERY_TRADEINIT = "tradeInit.json";
	private static final String ARTERY_APPLYORDER = "applyOrder.json";
	private static final String ARTERY_PAYINIT = "payInit.json";
	private static final String ARTERY_PAYDO = "payDo.json";
	private static final String ARTERY_MERCHANTDO = "merchantDo.json";
	private static final String ARTERY_ASSIGNMETHOD = "assignMethod.json";
	private static final String ARTERY_ZJDOLOADACTION = "ZJDoLoadAction.json";

	private static ArteryPreTrans mHttpTransMit;
	private HttpClient mHttpClinet;

	public static enum ClientMode {
		PRO, TEST;
	}
	
	private ArteryPreTrans() {
		mHttpClinet = new DefaultHttpClient();
		mHttpClinet.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); 
		mHttpClinet.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

	}
	
	private static boolean setMode(int nMode) {
		if (nMode == ArteryPreDef.MODE_PRO) {
			ARTERY_SRV_URL_HEADER = ARTERY_BASE_URL_PRO;
		} else if (nMode == ArteryPreDef.MODE_TEST){
			ARTERY_SRV_URL_HEADER = ARTERY_BASE_URL_TEST;
		} else {
			return false;
		}
		return true;
	}

	public static ArteryPreTrans getInstance() {
		if (mHttpTransMit == null) {
			mHttpTransMit = new ArteryPreTrans();
		}
		return mHttpTransMit;
	}

	/**
	 * 设置https的接入方式
	 * 
	 * @param certStream
	 *            服务器证书
	 */
	public boolean setHttpsMothed(InputStream certStream,int nMode) {
		// 读取证书
//		SSLSocketFactory sslSocketFactory = SSLCustomSocketFactory
//				.getSocketFactory(certStream);
//		if (sslSocketFactory == null) {
//			return false;
//		} else {
//			mHttpClinet.getConnectionManager().getSchemeRegistry()
//					.register(new Scheme("https", sslSocketFactory, 7443));
//		}
		return setMode(nMode);
		// 本地测试采用http方式

	}

	public static class SSLCustomSocketFactory extends SSLSocketFactory {

		private static final String TAG = "SSLCustomSocketFactory";
		private static final String KEY_PASS = "mx2016";//"jwsmarts";

		public SSLCustomSocketFactory(KeyStore trustStore) throws Throwable {
			super(trustStore);
		}

		public static SSLSocketFactory getSocketFactory(InputStream certStream) {
			try {
				KeyStore trustStore = KeyStore.getInstance("BKS");
				trustStore.load(certStream, KEY_PASS.toCharArray());
				SSLSocketFactory factory = new SSLCustomSocketFactory(
						trustStore);
				return factory;
			} catch (Throwable e) {
				MXLog.d(TAG, e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public Map<String, Object> tradeInit(Map<String, Object> paramesField,
			Map<String, Object> preField) {
		String url = ARTERY_SRV_URL_HEADER + ARTERY_TRADEINIT;
		return arteryPost(url, paramesField, preField);
	}

	public Map<String, Object> applyOrder(Map<String, Object> paramesField,
			Map<String, Object> preField) {
		String url = ARTERY_SRV_URL_HEADER + ARTERY_APPLYORDER;
		return arteryPost(url, paramesField, preField);
	}

	public Map<String, Object> payInit(Map<String, Object> paramesField,
			Map<String, Object> preField) {
		String url = ARTERY_SRV_URL_HEADER + ARTERY_PAYINIT;
		return arteryPost(url, paramesField, preField);
	}

	public Map<String, Object> queryPayStatus(Map<String, Object> paramesField,
			Map<String, Object> preField) {
		String url = ARTERY_SRV_URL_HEADER + ARTERY_PAYDO;
		preField.put("method", "queryPayStatus");
		return arteryPost(url, paramesField, preField);
	}
	

	public Map<String, Object> merChantDo(Map<String, Object> paramesField,
			Map<String, Object> preField) {
		String url = ARTERY_SRV_URL_HEADER + ARTERY_MERCHANTDO;
		return arteryPost(url, paramesField, preField);
	}
	
	public Map<String, Object> assignMethod(Map<String, Object> paramesField,Map<String, Object> preField) {
		String url = ARTERY_SRV_URL_HEADER + ARTERY_ASSIGNMETHOD;
		return arteryPost(url, paramesField, preField);
	}
	
	public Map<String, Object> ctticDoLoadAction(Map<String, Object> paramesField,Map<String, Object> preField) {
		String url = ARTERY_SRV_URL_HEADER + ARTERY_ZJDOLOADACTION;
		return arteryPost(url, paramesField, preField);
	}

	private synchronized Map<String, Object> arteryPost(String url,
			Map<String, Object> paramesField, Map<String, Object> preField) {
		HttpResponse response = null;
		HttpPost post = new HttpPost(url);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ArteryPreRequestMessage requestMessage = new ArteryPreRequestMessage();
		requestMessage.setMacField("0000001");
		requestMessage.setMsgVersion("0000001");
		try {
			requestMessage.setParamesField(MXBaseUtil.map2Json(paramesField)
					.toString());
		} catch (JSONException e2) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_FROM_CLIENT);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "Json异常错误");
			return resultMap;
		}
		requestMessage.setSignField("0000001");
		try {
			requestMessage
					.setPerField(MXBaseUtil.map2Json(preField).toString());
		} catch (JSONException e2) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_FROM_CLIENT);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "Json异常错误");
			return resultMap;
		}
		// 增加前置需要的域
		//MyLog.i(TAG, "url:" + url);

		StringEntity s = null;

		try {
			s = new StringEntity(requestMessage.getJsonString());
			MXLog.i(TAG, "req:" + requestMessage.getJsonString());
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_FROM_CLIENT);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "请求实例化错误");
			return resultMap;
		}
		s.setContentEncoding("UTF-8");
		s.setContentType("application/json");
		post.setEntity(s);
		
		try {
			response = mHttpClinet.execute(post);
		} catch (ClientProtocolException e) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_INIT);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "客户端网络协议错误，请重启网络后重试");
			return resultMap;
		} catch (IOException e) {
			e.printStackTrace();
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_INIT);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "客户端网络异常，请重启网络后重试");
			return resultMap;
		}
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			MXLog.i("异常错误",
					String.valueOf(response.getStatusLine().getStatusCode()));
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_ERROR);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "网络错误，请检查您的网络状态");
			return resultMap;
		}
		String resopnse;
		try {
			resopnse = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (ParseException e) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_FORMAT_ERROR);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "服务不可用");
			return resultMap;
		} catch (IOException e) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_FORMAT_ERROR);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "服务不可用");
			return resultMap;
		}
		ArteryPreResopnseMessage responseMessage = new ArteryPreResopnseMessage();
		try {
			responseMessage.initFromString(resopnse);
		} catch (JSONException e) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_FORMAT_ERROR);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "服务不可用");
			return resultMap;
		}
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(
					responseMessage.getParamesField());
		} catch (JSONException e) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_FORMAT_ERROR);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "服务不可用");
			return resultMap;
		}
		MXLog.i(TAG, "resp:" + jsonObject.toString());
		try {
			Map<String, Object> result = MXBaseUtil.json2Map(responseMessage.getParamesField());
			//把code 改成int型
			String code = (String)result.get(ArteryPreDef.FIELD_result);
			result.put(ArteryPreDef.FIELD_result, Integer.valueOf(code,16));
			return result;
		} catch (JSONException e) {
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_FORMAT_ERROR);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "服务不可用");
			return resultMap;
		} catch (Exception e) {	
			resultMap.put(ArteryPreDef.FIELD_result,
					ArteryPreDef.ERROR_HTTP_FORMAT_ERROR);
			resultMap.put(ArteryPreDef.FIELD_resultDesc, "服务不可用");
			return resultMap;
		}
	}
}
