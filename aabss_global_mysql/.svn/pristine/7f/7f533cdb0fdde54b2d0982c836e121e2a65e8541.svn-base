package com.newdumai.dumai_data.dm_3rd_interface.special_interface.pingAn.in_interface;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.newdumai.dumai_data.dm_3rd_interface.special_interface.pingAn.util.PingAnHttpHelper;

public class PingAnIn_Interface {
	
	
	/**
	 * 
	 * @param dm_3rd_interfaceMap
	 * @param in_para
	 * @return
	 */
	public static Map<String,Object> getIn_para(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
		Map<String, Object> sendData = setParams(in_para);
		PingAnHttpHelper dsh = new PingAnHttpHelper();
		try {
			dsh.getBaseParams(sendData);
		} catch (NoSuchAlgorithmException e) {
			//此处不会异常
			return null;
		}
		return sendData;
	}
	
	/**
	 * 过滤出我想要的发送参数，其他的参数随意放，但是会过滤掉无用的
	 * @param params
	 * @return
	 */
	private static Map<String,Object> setParams(Map<String,Object>params){
		Map<String,Object> sendData = new HashMap<String,Object>();
		if(params.containsKey("name")){
			sendData.put("name", (String) params.get("name"));
		}
		if(params.containsKey("idCard")){
			sendData.put("idCard", params.get("idCard").toString());
		}
		if(params.containsKey("phone")){
			sendData.put("phone", params.get("phone").toString());
		}
		if(params.containsKey("contactMain")){
			if (StringUtils.isNotEmpty((String)params.get("contactMain"))) {
				sendData.put("contactMain",(String)params.get("contactMain")+",,");
			}
			if (StringUtils.isNotEmpty((String)params.get("contactPhone1"))) {
				sendData.put("contacts",(String)params.get("contactPhone1")+",,");
			}
			if (StringUtils.isNotEmpty((String)params.get("contactPhone2"))) {
				sendData.put("contacts",sendData.get("contacts")+","+(String)params.get("contactPhone2")+",,");
			}
		}
		return sendData;
	}
}
