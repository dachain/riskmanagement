package com.newdumai.dumai_data.dm_3rd_interface.special_interface.pingAn.out_interface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class PingAnPhonetagOut_Interface {
	/**
	 * 5.1 电话号码标记查询
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static String getPhonetag_outPara(Map<String, Object> dm_3rd_interfaceMap, String result) throws Exception {

		Map<String, Object> result2 = new HashMap<String, Object>();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(result);
		JsonNode dataResult = root.get("result");
		if (0 == dataResult.asInt()) {
			JsonNode data = root.get("data");
			Iterator<JsonNode> datas = data.elements();
			Integer pa_cellphone_label = null;
			while (datas.hasNext()) {
				JsonNode node = datas.next();
				JsonNode tag = node.get("tag");
				if ("广告推销电话".equals(tag.asText())) {
					pa_cellphone_label = pa_cellphone_label == null ? 1 : pa_cellphone_label + 1;
				}
			}
			result2.put("pa_cellphone_label", pa_cellphone_label);
		}
		return new Gson().toJson(result2);
	}
}
