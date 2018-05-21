package com.newdumai.dumai_data.dm_3rd_interface.special_interface.pingAn.out_interface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.newdumai.util.TimeHelper;

/**
 * 
 * @author 岳晓
 * 
 */
public class PingAnOut_Interface {

	private static final String JSON_RESULT = "result";
	private static final String JSON_DATA = "data";
	public static final int API_PHONE_INFO = 1;
	public static final int API_LOAN_CLASSIFY = 2;
	public static final int API_PHONE_TAG = 3;
	public static final int API_BLACK_LIST = 4;
	public static final int API_lOG_STATISTICS = 5;
	public static final int API_ID_CHECK = 6;
	public static final int API_PHONE_MATCH = 7;
	public static final int API_OVERDUE_CLASSIFY = 8;

	public static final String RULE_PHONE_INFO_GRAYSCALE = "grayscale";
	public static final String RULE_BLACK_LIST = "blacklist";
	public static final String RULE_OVER_DUE_CLASSIFY = "overdueClassify";

	public static final String API_TYPE_PHONE_TAGS = "tags";
	public static final String API_TYPE_PHONE_DIRECT_CALL = "directCall";
	public static final String API_TYPE_PHONE_COMMON_CONTACTS = "commonContacts";
	public static final String API_TYPE_PHONE_INTER_COMMONS = "interCommons";
	public static final String API_TYPE_PHONE_GRAY_SCALE = "grayscale";

	public static final String DATE_SCOPE_M0 = "M0";
	public static final String DATE_SCOPE_M1 = "M1";
	public static final String DATE_SCOPE_M2 = "M2";
	public static final String DATE_SCOPE_M3 = "M3";
	public static final String DATE_SCOPE_M4 = "M4";
	public static final String DATE_SCOPE_M5 = "M5";
	public static final String DATE_SCOPE_M6 = "M6";
	public static final String DATE_SCOPE_M9 = "M9";
	public static final String DATE_SCOPE_M12 = "M12";
	public static final String DATE_SCOPE_M24 = "M24";

	public static final String ORG_TYPE_BANK_CREDIT = "bankCredit";
	public static final String ORG_TYPE_BANK_LOAN = "bankLoan";
	public static final String ORG_TYPE_OTHER_CREDIT = "otherCredit";
	public static final String ORG_TYPE_OTHER_LOAN = "otherLoan";

	public static final String ORG_TYPE_NBFI = "NBFI";
	public static final String ORG_TYPE_BANK = "bank";
	public static final String ORG_TYPE_COLLECTION_AGENCY = "collectionAgency";

	public static final Integer DATA_RESULT_HIT = 0;
	public static final String DATA_RESULT_NO_HIT = "2";

	/**
	 * 2 电话号码相关查询：联系人相关
	 * 
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static String getContacts_outPara(Map<String, Object> dm_3rd_interfaceMap, String result) throws Exception {
		Map<String, Object> result2 = new HashMap<String, Object>();
		result2.put("contactTimes", getContactTimes(result));
		result2.put("orgNums", getOrgNums(result));
		// result2.put("latestTime", getLatestTime(result));

		Map<String, Object> map = getOutPara(result);
		result2.put("hourStatMorning", map.get("hourStatMorning"));
		result2.put("periodicity", map.get("periodicity"));
		result2.put("dailyCallTimes", map.get("dailyCallTimes"));
		result2.put("operator", map.get("operator"));
		result2.put("label", map.get("label"));
		result2.put("latestTime", map.get("latestTime"));
		return new Gson().toJson(result2);
	}

	/**
	 * 从结果中获取联系人相关输出
	 */
	private static Map<String, Object> getOutPara(String result) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 0 点到6 点的通话次数
		Integer hourStatMorning = 0;
		// 是否周期性联系
		boolean flag = false;

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(result);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			// 直接联系人
			JsonNode directCall = data.get(API_TYPE_PHONE_DIRECT_CALL);
			Iterator<String> fields = directCall.fieldNames();
			while (fields.hasNext()) {
				JsonNode mx = directCall.get(fields.next());

				Iterator<String> mxfields = mx.fieldNames();
				while (mxfields.hasNext()) {
					String mxfield = mxfields.next();
					if (DATE_SCOPE_M0.equals(mxfield)) {
						JsonNode content = mx.get(mxfield);
						if (content.get("hourStat") != null) {
							JsonNode orgNode = content.get("hourStat");
							hourStatMorning += orgNode.get("morning").asInt();
						}
						Boolean periodicity = content.get("periodicity").asBoolean();
						if (periodicity) {
							flag = true;
						}

					}
				}
			}
			map.put("hourStatMorning", hourStatMorning);
			map.put("periodicity", flag);

			/** 电话标记(tags) */
			JsonNode tags = data.get(API_TYPE_PHONE_TAGS);
			// 日均通话次数
			String dailyCallTimes = "";
			// 手机运营商
			String operator = "";
			String label = "";
			Iterator<String> tagFields = tags.fieldNames();
			if (tagFields.hasNext()) {
				JsonNode mx = tags.get(tagFields.next());
				JsonNode operatorNode = mx.get("operator");
				if (operatorNode != null) {
					operator = operatorNode.asText();;
				}
				JsonNode labelNode = mx.get("label");
				if (labelNode != null) {
					label = labelNode.asText();;
				}
				JsonNode m0Node = mx.get(DATE_SCOPE_M0);
				JsonNode dailyNode = m0Node.get("dailyCallTimes");
				if (null != dailyNode) {
					dailyCallTimes = dailyNode.asText();
				}
			}
			map.put("operator", operator);
			map.put("label", label);
			map.put("dailyCallTimes", dailyCallTimes);
			
			if (NumberUtils.isNumber(dailyCallTimes)) {
				map.put("monthCallTimes", NumberUtils.toDouble(dailyCallTimes) * 30);
			}
			
			/** 染黑度(grayscale) */
			JsonNode grayscale = data.get(API_TYPE_PHONE_GRAY_SCALE);
			// 最后一次催收呼叫到现在的时间
			float latestTime = 0;
			Iterator<String> grayscaleFields = grayscale.fieldNames();
			if (grayscaleFields.hasNext()) {
				JsonNode mx = grayscale.get(grayscaleFields.next());
				JsonNode bank = mx.get(DATE_SCOPE_M0).get("bank");
				String latestTimeStr = bank.get("latestTime").asText();
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M1);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M2);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M3);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M4);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M5);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M6);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M9);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M12);
				}
				if (StringUtils.isEmpty(latestTimeStr)) {
					latestTimeStr = trans(mx, DATE_SCOPE_M24);
				}
				Date latestDate = TimeHelper.parseDate(latestTimeStr);
				latestTime = TimeHelper.getDistanceOfTwoDate(latestDate, new Date());
			}
			map.put("latestTime", latestTime);
		}
		return map;
	}

	private static String trans(JsonNode mx, String flag) {
		String ret = "";
		try {
			ret = mx.get(flag).get("bank").get("latestTime").asText();
		} catch (Exception e) {
		}
		return ret;
	}

	/**
	 * 
	 * @param resultData
	 * @return
	 * @throws Exception
	 */
	private static int getContactTimes(String resultData) throws Exception {
		int contactTimes = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode grayscale = data.get(API_TYPE_PHONE_GRAY_SCALE);
			Iterator<String> fields = grayscale.fieldNames();
			while (fields.hasNext()) {
				JsonNode mx = grayscale.get(fields.next());

				Iterator<String> mxfields = mx.fieldNames();
				while (mxfields.hasNext()) {
					String mxfield = mxfields.next();
					JsonNode content = mx.get(mxfield);

					if (content.get(ORG_TYPE_NBFI) != null && !content.get(ORG_TYPE_NBFI).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_NBFI);
						contactTimes += orgNode.get("contactTimes").asInt();
					}

					if (content.get(ORG_TYPE_BANK) != null && !content.get(ORG_TYPE_BANK).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_BANK);
						contactTimes += orgNode.get("contactTimes").asInt();
					}

					if (content.get(ORG_TYPE_COLLECTION_AGENCY) != null && !content.get(ORG_TYPE_COLLECTION_AGENCY).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_COLLECTION_AGENCY);
						contactTimes += orgNode.get("contactTimes").asInt();
					}
				}
			}

		}
		return contactTimes;
	}

	/**
	 * 
	 * @param resultData
	 * @return
	 * @throws Exception
	 */
	private static int getOrgNums(String resultData) throws Exception {
		int contactTimes = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode grayscale = data.get(API_TYPE_PHONE_GRAY_SCALE);
			Iterator<String> fields = grayscale.fieldNames();
			while (fields.hasNext()) {
				JsonNode mx = grayscale.get(fields.next());

				Iterator<String> mxfields = mx.fieldNames();
				while (mxfields.hasNext()) {
					String mxfield = mxfields.next();
					JsonNode content = mx.get(mxfield);

					if (content.get(ORG_TYPE_NBFI) != null && !content.get(ORG_TYPE_NBFI).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_NBFI);
						contactTimes += orgNode.get("orgNums").asInt();
					}

					if (content.get(ORG_TYPE_BANK) != null && !content.get(ORG_TYPE_BANK).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_BANK);
						contactTimes += orgNode.get("orgNums").asInt();
					}

					if (content.get(ORG_TYPE_COLLECTION_AGENCY) != null && !content.get(ORG_TYPE_COLLECTION_AGENCY).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_COLLECTION_AGENCY);
						contactTimes += orgNode.get("orgNums").asInt();
					}
				}
			}

		}
		return contactTimes;
	}

	/**
	 * 
	 * @param resultData
	 * @return
	 * @throws Exception
	 */
	private static String getLatestTime(String resultData) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode grayscale = data.get(API_TYPE_PHONE_GRAY_SCALE);
			Iterator<String> fields = grayscale.fieldNames();
			while (fields.hasNext()) {
				JsonNode mx = grayscale.get(fields.next());

				Iterator<String> mxfields = mx.fieldNames();
				while (mxfields.hasNext()) {
					String mxfield = mxfields.next();
					JsonNode content = mx.get(mxfield);
					JsonNode orgNode = null;
					if (content.get(ORG_TYPE_NBFI) != null && !content.get(ORG_TYPE_NBFI).asText().equals("null")) {
						orgNode = content.get(ORG_TYPE_NBFI);
					}

					if (content.get(ORG_TYPE_BANK) != null && !content.get(ORG_TYPE_BANK).asText().equals("null")) {
						orgNode = content.get(ORG_TYPE_BANK);
					}

					if (content.get(ORG_TYPE_COLLECTION_AGENCY) != null && !content.get(ORG_TYPE_COLLECTION_AGENCY).asText().equals("null")) {
						orgNode = content.get(ORG_TYPE_COLLECTION_AGENCY);
					}
					if (mxfield.equals("M1") && orgNode != null && StringUtils.isEmpty(orgNode.get("latestTime").asText())) {
						return "有";
					}
				}
			}

		}
		return "无";
	}

	/**
	 * 黑名单数据
	 * @param dm_3rd_interfaceMap
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static String getBlackList_outPara(Map<String, Object> dm_3rd_interfaceMap, String result) throws Exception {
		Map<String, Object> result2 = new HashMap<String, Object>();
		result2.put("orgLostContact", getOrgLostContact(result)+"");
		result2.put("bankLostContact", getBankLostContact(result)+"");
		result2.put("orgBlackList", getOrgBlackList(result)+"");
		result2.put("seriousOverdueTime", getSeriousOverdueTime(result)+"");
		return new Gson().toJson(result2);
	}

	private static String getOrgLostContact(String resultData) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			Iterator<JsonNode> others = data.get("others").elements();
			while (others.hasNext()) {
				JsonNode other = others.next();
				if (StringUtils.isNotEmpty(other.get("orgLostContact").asText())) {
					return "有";
				}
			}
		}
		return "无";
	}

	private static String getOrgBlackList(String resultData) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			Iterator<JsonNode> others = data.get("others").elements();
			while (others.hasNext()) {
				JsonNode other = others.next();
				if (StringUtils.isNotEmpty(other.get("orgBlackList").asText())) {
					return "有";
				}
			}
		}
		return "无";
	}

	private static Integer getSeriousOverdueTime(String resultData) throws Exception {
		
		Integer months = null;
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			Iterator<JsonNode> others = data.get("others").elements();
			if (others.hasNext()) {
				JsonNode other = others.next();
				String seriousOverdueTime = other.get("bankLostContact").asText();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = sdf.parse(seriousOverdueTime);
				months =(int) ((new Date().getTime()-date.getTime())/(1000L*60*60*24*30));
			}
		}
		return months;
	}
	private static String getBankLostContact(String resultData) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			Iterator<JsonNode> others = data.get("others").elements();
			while (others.hasNext()) {
				JsonNode other = others.next();
				if (StringUtils.isNotEmpty((other.get("bankLostContact").asText()))) {
					return "有";
				}
			}
		}
		return "无";
	}

	/**
	 * 逾期接口数据
	 * @param dm_3rd_interfaceMap
	 * @param result
	 * @return
	 * @throws Exception
	 */
	public static String getOverdue_outPara(Map<String, Object> dm_3rd_interfaceMap, String result) throws Exception {
		Map<String, Object> result2 = new HashMap<String, Object>();
		//result2.put("overdueOrgNumbs", getOverdueOrgNumbs(result));
		result2.put("orgRecordNums", getOverdueOrgNumbs(result));
		result2.put("longestDays", getLongestDays(result));
		result2.put("maxAmount", getMaxAmount(result));
		result2.put("maxAmountDate", getMaxAmountDate(result));
		result2.put("recordNums", getRecordNums(result));
		return new Gson().toJson(result2);
	}

	private static int getOverdueOrgNumbs(String dataResult) throws Exception {
		int numbers = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			while (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				while (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);
						JsonNode node = null;
						if (mxContent.get(ORG_TYPE_BANK_CREDIT) != null && !mxContent.get(ORG_TYPE_BANK_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
						}
						if (mxContent.get(ORG_TYPE_OTHER_CREDIT) != null && !mxContent.get(ORG_TYPE_OTHER_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_OTHER_LOAN) != null && !mxContent.get(ORG_TYPE_OTHER_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_LOAN);
						}
						if (node != null) {
							numbers += node.get("orgNums").asInt();
						}
					}
				}
			}
		}
		return numbers;
	}

	private static int getLongestDays(String dataResult) throws Exception {
		int numbers = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			while (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				while (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);

						JsonNode node = null;
						if (mxContent.get(ORG_TYPE_BANK_CREDIT) != null && !mxContent.get(ORG_TYPE_BANK_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
						}
						if (mxContent.get(ORG_TYPE_OTHER_CREDIT) != null && !mxContent.get(ORG_TYPE_OTHER_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_OTHER_LOAN) != null && !mxContent.get(ORG_TYPE_OTHER_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_LOAN);
						}
						if (node != null) {
							numbers += node.get("longestDays").asInt();
						}
					}
				}
			}
		}
		return numbers;
	}

	private static int getMaxAmount(String dataResult) throws Exception {
		int maxAmount = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			while (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				while (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);

						JsonNode node = null;
						if (mxContent.get(ORG_TYPE_BANK_CREDIT) != null && !mxContent.get(ORG_TYPE_BANK_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
						}
						if (mxContent.get(ORG_TYPE_OTHER_CREDIT) != null && !mxContent.get(ORG_TYPE_OTHER_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_OTHER_LOAN) != null && !mxContent.get(ORG_TYPE_OTHER_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_LOAN);
						}
						if (node != null) {
							String maxAmountScope = node.get("maxAmount").asText();
							String maxAmountString = maxAmountScope.substring(1, maxAmountScope.indexOf(","));
							maxAmount += Integer.parseInt(maxAmountString);
						}
					}
				}
			}
		}
		return maxAmount;
	}

	/**
	 * 取返回结果的最大逾期金额的逾期天数
	 */
	private static Integer getMaxAmountDate(String dataResult) throws Exception {
		Integer numbers = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			if (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				if (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);

						JsonNode node = null;
						if (DATE_SCOPE_M3.equals(mxfield) && mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
							numbers = node.get("maxAmountDate").asInt();
							return numbers;
						}
					}
				}
			}
		}
		return numbers;
	}

	/**
	 * 取返回结果的逾期记录的条数
	 */
	private static Integer getRecordNums(String dataResult) throws Exception {
		Integer numbers = null;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			if (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				if (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);

						JsonNode node = null;
						if (DATE_SCOPE_M3.equals(mxfield) && mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
							numbers = node.get("recordNums").asInt();
							return numbers;
						}
					}
				}
			}
		}
		return numbers;
	}
	
	/**
	 * 3.2 贷款信息<br/>
	 * 计算规则:<br/>
	 * $代表根节点<br/>
	 * <pre>
	 * {
	 *     pa_m3_totalAmount:$.result.data.record[0].classification[0].M3.bank.totalAmount,
	 *     pa_repaymentTime:$.result.data.record[0].classification[0].M3.bank(loanAmount/repayAmount),
	 * }
	 * </pre>
	 * @param dm_3rd_interfaceMap
	 * @param result
	 * @return 
	 * @throws Exception
	 */
	public static String getLoan_outPara(Map<String, Object> dm_3rd_interfaceMap, String result) throws Exception {
		Map<String,Object> result2 = new HashMap<String, Object>();
		Integer pa_m3_totalAmount = null;
		Integer pa_repaymentTime = null;
		
		//$代表JSON根节点
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(result);
		JsonNode dataResult = root.get(JSON_RESULT);//$.result
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);//$.result.data
			Iterator<JsonNode> records = data.get("record").elements();
			if (records!=null && records.hasNext()) {//$.result.data.record[0]
				JsonNode record = records.next();
				Iterator<JsonNode>  classifications = record.get("classification").elements();
				if(classifications!=null && classifications.hasNext()){//$...record[0].classification[0]
					JsonNode classification = classifications.next();
					JsonNode m3 = classification.get(DATE_SCOPE_M3);//classification[0].M3
					if (m3 != null) {
						JsonNode bank = m3.get(ORG_TYPE_BANK);// classification[0].M3.bank
						if (bank != null) {
							JsonNode totalAmountNode =  bank.get("totalAmount");
							if (totalAmountNode != null && totalAmountNode.isNumber()) {
								pa_m3_totalAmount = totalAmountNode.asInt();
							}
							JsonNode loanAmountNode =  bank.get("loanAmount");
							JsonNode repayAmountNode =  bank.get("repayAmount");
							if (loanAmountNode != null && loanAmountNode.isNumber()&& repayAmountNode!=null && repayAmountNode.isDouble()) {
								Double loanAmount = loanAmountNode.asDouble();
								Double repayAmount = repayAmountNode.asDouble();
								if (repayAmount != 0) {
									pa_repaymentTime = (int) Math.ceil((loanAmount/repayAmount));
								}
							}
						}
					}
				}
			}
		}
		result2.put("pa_m3_totalAmount", pa_m3_totalAmount);
		result2.put("pa_repaymentTime", pa_repaymentTime);
		return new Gson().toJson(result2);
	}
	
}
