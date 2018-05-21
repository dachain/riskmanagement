package com.newdumai.dumai_data.dm_3rd_interface.special_interface.pingAn.out_interface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class PingAnLogStatisticsOut_Interface {
	
	/**
	 * 3.4 其它机构查询情况
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static String getLogStatistics_outPara(Map<String, Object> dm_3rd_interfaceMap, String result) throws Exception {
		
		Map<String, Object> result2 = new HashMap<String, Object>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(result);
		JsonNode dataResult = root.get("result");
		if (0==dataResult.asInt()) {
			JsonNode data = root.get("data");
			Iterator<String> fieldNames = data.fieldNames();
			String maxKey = null;
			while(fieldNames.hasNext()){
				String key = fieldNames.next();
				maxKey = maxKey == null ? key : maxKey.compareTo(key) > 0 ? maxKey : key;
			}
			JsonNode node = data.get(maxKey);
			JsonNode queryNumsNode = node.get("queryNums");
			Integer queryNums = queryNumsNode==null?null:queryNumsNode.asInt();
			result2.put("orgQueryNums", queryNums);
		}
		return new Gson().toJson(result2);
	}
}
