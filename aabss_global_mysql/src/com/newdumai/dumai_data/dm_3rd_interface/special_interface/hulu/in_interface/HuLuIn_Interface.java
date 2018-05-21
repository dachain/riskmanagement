package com.newdumai.dumai_data.dm_3rd_interface.special_interface.hulu.in_interface;

import java.util.HashMap;
import java.util.Map;

import com.newdumai.util.HttpClientUtil;
import com.newdumai.util.JsonToMap;

public class HuLuIn_Interface {
	
	/**
	 * 获取access_token的url
	 */
	private static final String ACCESS_TOKEN_URL = "https://data.hulushuju.com/api/companies/iqianbang_SAURON/access_token?signature=8b74d060c40848dbab0ecd22c249107a";
			
	
	/**
	 * 输入参数生成
	 * @param dm_3rd_interfaceMap
	 * @param in_para
	 * @return
	 */
	public static Map<String, Object> getIn_para(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.putAll(in_para);
		inParams.put("accessToken",getAccessToken());
		return inParams;
	}
	
	/**
	 * 获取登录令牌
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static String getAccessToken() {
	
		String rs = HttpClientUtil.exec(ACCESS_TOKEN_URL, "0", "0", null);
		if (rs == null) {
			return null;
		}
		try {
			Map<String, Object> rsMap = JsonToMap.gson2Map(rs);
			int code = ((Number) rsMap.get("code")).intValue();
			if (code == 0) {
				Map<String, Object> data = (Map<String, Object>) rsMap.get("data");
				if (data != null) {
					return (String) data.get("access_token");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
