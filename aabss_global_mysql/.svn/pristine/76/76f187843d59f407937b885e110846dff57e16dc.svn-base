package com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.in_interface;

import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.util.RC4_128_V2;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.util.RSA_1024_V2;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.YinXinConsts;

public class YixXinIn_Interface {
	/*public static final String userId = "iqianbang_testusr";
	public static final String password = "e6c013750508fff2";*/
	/*public static final String userId = "iqianbang";
	public static final String password = "0ce4945e802f16bb";*/
										   

	

	public static Map<String, Object> getIn_para(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.putAll(in_para);
		params.put("idType", "101");
		params.put("queryReason", "10");
		try {
			String encryptedParams = RC4_128_V2.encode(URLEncoder.encode(new Gson().toJson(params), "utf-8"), YinXinConsts.password);
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("userid", getEncryptUserId());
			paraMap.put("params", encryptedParams);
			return paraMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	private static String getEncryptUserId() {
		RSAPublicKey rsaPublicKey = RSA_1024_V2.gainRSAPublicKeyFromCrtFile(YixXinIn_Interface.class.getResource("../util/ZC_PublicKey_V2.crt").getFile());
		String encryptedUserID = RSA_1024_V2.encodeByPublicKey(rsaPublicKey, YinXinConsts.userId);
		return encryptedUserID;
	}
}
