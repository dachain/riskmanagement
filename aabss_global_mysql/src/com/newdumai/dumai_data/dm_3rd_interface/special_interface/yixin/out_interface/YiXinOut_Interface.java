package com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.out_interface;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.YinXinConsts;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.util.RC4_128_V2;
import com.newdumai.util.JsonToMap;

public class YiXinOut_Interface {

	@SuppressWarnings("unchecked")
	public static String getLoanOut_para(Map<String, Object> dm_3rd_interfaceMap, String result) {
		result = decodeResult(result);// 解密
		Map<String, Object> resultNew = new HashMap<String, Object>();

		Map<String, Object> map = JsonToMap.jsonToMap(result);
		if ("0000".equals(map.get("errorcode"))) {
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			if (data != null) {
				Map<String, Object> overdue = (Map<String, Object>) data.get("overdue");
				Object overdueTimes = overdue.get("overdueTimes");
				if (overdueTimes instanceof Number) {
					resultNew.put("overdueTimes", ((Number) overdueTimes).intValue());
				}
				Object overdueTimes_90 = overdue.get("90overdueTimes");
				Integer overdueTimes_90Int = null;
				if (overdueTimes_90 instanceof Number) {
					overdueTimes_90Int = ((Number) overdueTimes_90).intValue();
					resultNew.put("90overdueTimes", overdueTimes_90Int);
				}
				Object overdueTimes_180 = overdue.get("180overdueTimes");
				Integer overdueTimes_180Int = null;
				if (overdueTimes_180 instanceof Number) {
					overdueTimes_180Int = ((Number) overdueTimes_180).intValue();
					resultNew.put("180overdueTimes", overdueTimes_180Int);
				}
				int overdueInt = 0;
				if (overdueTimes_180Int != null && overdueTimes_180Int > 1) {
					overdueInt = 90;
				}
				else if (overdueTimes_90Int != null && overdueTimes_90Int > 1) {
					overdueInt = 180;
				}
				resultNew.put("overdue", overdueInt);

				String currentStatus = null;
				List<Map<String, Object>> loanRecords = (List<Map<String, Object>>) data.get("loanRecords");
				if (CollectionUtils.isNotEmpty(loanRecords)) {
					currentStatus = "结清";
					for (Map<String, Object> loanRecord : loanRecords) {
						String currentStatusCode = (String) loanRecord.get("currentStatusCode");
						if (!"303".equals(currentStatusCode)) {
							currentStatus = "未结清";
						}
					}
				}
				resultNew.put("currentStatus", currentStatus);
			}
		}
		return new Gson().toJson(resultNew);
	}

	@SuppressWarnings({ "unchecked" })
	public static String getBlackListOut_para(Map<String, Object> dm_3rd_interfaceMap, String jsonResult) {
		jsonResult = decodeResult(jsonResult);// 解密

		Integer blackCounts = null;
		Integer maxRiskTime = null;
		Map<String, Object> map = JsonToMap.jsonToMap(jsonResult);
		String source = "";
		if ("0000".equals(map.get("errorcode"))) {
			Map<String, Object> data = (Map<String, Object>) map.get("data");
			if (data != null) {
				blackCounts = 0;
				List<Map<String, Object>> overdues = (List<Map<String, Object>>) data.get("riskItems");
				if(CollectionUtils.isEmpty(overdues)){
					return null;
				}
				List<String> sourceList = new ArrayList<String>();
				for (Map<String, Object> overdue : overdues) {
					String source_1 = (String) overdue.get("source");
					sourceList.add(source_1);
					String riskType = (String) overdue.get("riskType");
					if (!StringUtils.isEmpty(riskType)) {
						blackCounts++;
					}
					String riskTime = (String) overdue.get("riskTime");
					if (!StringUtils.isEmpty(riskTime)) {
						Integer riskTimeInt = Integer.parseInt(riskTime);
						maxRiskTime = maxRiskTime == null ? riskTimeInt : Math.max(maxRiskTime, riskTimeInt);
					}
				}
				source = StringUtils.join(sourceList,",");
			}
		}
		else {
			return null;
		}

		Map<String, Object> resultNew = new HashMap<String, Object>();
		resultNew.put("blackCounts", blackCounts);
		resultNew.put("riskTime", maxRiskTime);
		resultNew.put("source", source);
		return new Gson().toJson(resultNew);
	}

	/**
	 * 对结果解密
	 * 
	 * @param result
	 *            结果字符串
	 * @param password
	 *            密码
	 * @return
	 */
	private static String decodeResult(String result) {
		Map<String, Object> json = JsonToMap.jsonToMap(result);
		if (json.containsKey("errorcode") && "0000".equals(json.get("errorcode"))) {
			// 解密解码返回结果
			String decryptResult = RC4_128_V2.decode((String) json.get("data"), YinXinConsts.password);
			try {
				decryptResult = URLDecoder.decode(decryptResult, "utf-8");
				json.put("data", JsonToMap.jsonToMap(decryptResult));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new Gson().toJson(json);
	}

	public static void main(String[] args) {
		// {"errorcode":"0000","message":"查询成功","data":"1yvKjHbGGPlIyI2HkDRoRI7DOyVcAbDXj/Ihh2jUZrtvQihjgNUYBUPW9jleH45ttN6CYe/Ndgcy"}
		System.out.println(decodeResult("{\"errorcode\":\"0000\",\"message\":\"查询成功\",\"data\":\"1yvKjHbGGPlIyI2HkDRoRI7DOyVcAbDXj/Ihh2jUZrtvQihjgNUYBUPW9jleH45ttN6CYe/Ndgcy\"}"));
	}
}
