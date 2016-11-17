package com.mx.httpclient;

public interface BaseCodeDef {
	public static final int SUCCESS = 0;
	// 通讯类码，由通讯模块发出
	public static final int TRANSMIT_CODE = 0x50000000;
	// 支付模块码，由支付模块发出
	public static final int PAYMENT_CODE = 0x51000000;
	// 商户模块码，由商户模块发出
	public static final int MERCHANT_CODE = 0x52000000;
	// 设备模块码，由设备模块发出
	public static final int DEVICE_CODE = 0x53000000;
	// 通用模块码，由通用模块发出
	public static final int UTIL_CODE = 0x54000000;
	// 交易模块码，由交易模块发出
	public static final int TRADE_CODE = 0x55000000;
}
