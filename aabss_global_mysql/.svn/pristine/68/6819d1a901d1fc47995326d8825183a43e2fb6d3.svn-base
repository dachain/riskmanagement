package com.newdumai.dumai_data.dm_3rd_interface.special_interface.youfen.out_interface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.TimeHelper;

public class YouFen_Interface {

	@SuppressWarnings("unchecked")
	public static String getCohabitersOut_para(Map<String, Object> dm_3rd_interfaceMap, String result) {

		try {
			Map<String, Object> map = JsonToMap.jsonToMap(result);
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			Map<String, Object> resultNode = (Map<String, Object>) data.get("result");
			Map<String, Object> detail = (Map<String, Object>) resultNode.get("detail");

			String birthday = (String) detail.get("birthday");
			map.put("birthday_year", birthday.substring(0, 4));
			
			return new Gson().toJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String getCrimeOut_para(Map<String, Object> dm_3rd_interfaceMap, String result) {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			map = JsonToMap.jsonToMap(result);
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			List<Map<String, Object>> resultNode =  (List<Map<String, Object>>) data.get("result");
			map.put("crime_count",resultNode==null?0:resultNode.size());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("crime_count",0);
		}
		return new Gson().toJson(map);
	}
	
	@SuppressWarnings("unchecked")
	public static String getYearsToToday(Map<String, Object> dm_3rd_interfaceMap, String result) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		try {
			returnMap = JsonToMap.jsonToMap(result);   
			Map<String, Object> data = (Map<String, Object>) returnMap.get("data");
			Map<String, Object> resultMap =  (Map<String, Object>) data.get("result");
			resultMap = getYearsToToday(resultMap, new String[]{"S0135","S0506"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Gson().toJson(returnMap);
	}
	
	
	@SuppressWarnings("unchecked")
	public static String getMobileAgeOut_para(Map<String, Object> dm_3rd_interfaceMap, String result) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = JsonToMap.jsonToMap(result);
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			Map<String, Object> resultNode = (Map<String, Object>) data.get("result");
			if (resultNode != null) {
				String minNum = (String) resultNode.get("min");
				String maxNum = (String) resultNode.get("max");
				String unit = (String) resultNode.get("unit");

				String mobileAgeRange = (minNum == null ? "" : minNum) + "-" + (maxNum == null ? "" : maxNum) + (unit == null ? "" : unit);
				if ("+".equals(maxNum) || "999".equals(maxNum)) {
					mobileAgeRange = minNum + unit + "以上";
				}
				if ("1".equals(maxNum)) {
					mobileAgeRange = "当月入网";
				}
				if ("2".equals(maxNum)) {
					mobileAgeRange = "上月入网";
				}
				map.put("mobileAgeRange", mobileAgeRange);
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("crime_count", 0);
		}
		return new Gson().toJson(map);
	}
	
	//把时间转换成距离今天的年数
	private static Map<String,Object>getYearsToToday(Map<String,Object>result,String ... names){
		if(ArrayUtils.isNotEmpty(names)){
			for(String name : names){
				String datetime = (String)result.get(name);
				String years = null;
				if(StringUtils.isNotEmpty(datetime)){
					years = TimeHelper.getYearsToToday(datetime, "yyyyMMdd");
				}
				result.put(name, years);
			}
		}
		return result;
	}
	
	
}
