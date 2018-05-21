package com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.util.RC4_128_V2;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.yixin.util.RSA_1024_V2;
import com.newdumai.dumai_data.dm_3rd_interface.util.CommonUtil;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.SpringApplicationContextHolder;

public class YiXin {
	/*@Test
	public void doTest() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name","井庆生");//去除传入的空格
		params.put("idNo","320506197107277790");//去除传入的空格
		params.put("idType", "101");
		params.put("queryReason","10");
		params.put("url","http://www.zhichengcredit.com/CreditPortal/api/queryLoan/v2");
		params.put("userId","iqianbang_testusr");
		params.put("password","e6c013750508fff2");
		String s = QueryYiXin(params);
		System.out.println(s);
	}*/
	
	/*public static String QueryYiXin(Map<String,Object> params){
		params.put("idType", "101");
		params.put("queryReason","10");
		
		params.put("userId","iqianbang_testusr");
		params.put("password","e6c013750508fff2");
		
		params.put("userId","iqianbang");
		params.put("password","0ce4945e802f16bb");
		MultiValueMap<String, String> paraMap = new LinkedMultiValueMap<String, String>();
		String encryptedParams = null;
		try {
			encryptedParams = RC4_128_V2.encode(URLEncoder.encode(new Gson().toJson(params), "utf-8"), (String) params.get("password"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		paraMap.add("userid",  getEncryptUserId((String) params.get("userId")));
		paraMap.add("params", encryptedParams);
		//1、借款信息查询
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject((String) params.get("url"), paraMap,String.class);
		return decodeResult(result,(String) params.get("password"));
	}*/
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public static String QueryYiXin(Map<String,Object> params){
		
		params.put("idType", "101");
		params.put("queryReason","10");
		params.put("userId","iqianbang_testusr");
		params.put("password","e6c013750508fff2");
		
		/*params.put("userId","iqianbang");
		params.put("password","0ce4945e802f16bb");*/
		
		Map<String,Object> in_para = (Map<String, Object>) params.get("in_para");
		Map<String,Object> in_paraFull = new HashMap<String, Object>();
		in_paraFull.putAll(in_para);
		in_paraFull.put("idType", "101");
		in_paraFull.put("queryReason","10");
		
		String interface_code = (String) params.get("interface_source_code");
		
		Dumai_sourceBaseDao mysqlSpringJdbcBaseDao = (Dumai_sourceBaseDao) SpringApplicationContextHolder.getBean("dumai_sourceBaseDao");
		String dm_3rd_interface_detail_code = null;
		try {
			String encryptedParams  = RC4_128_V2.encode(URLEncoder.encode(new Gson().toJson(in_paraFull), "utf-8"), (String) params.get("password"));
			
			MultiValueMap<String, String> paraMap = new LinkedMultiValueMap<String, String>();
			paraMap.add("userid",  getEncryptUserId((String) params.get("userId")));
			paraMap.add("params", encryptedParams);
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.postForObject((String) params.get("url"), paraMap,String.class);
			//String result = FileUtils.readFileToString(new java.io.File("D:/result.txt"),"UTF-8");//伪造数据方便调试
			
			result = decodeResult(result,(String) params.get("password"));
			String insertSql = "INSERT INTO dm_3rd_interface_detail(code, dm_3rd_interface_code, in_para, result, base_condition) VALUES (?,?,?,?,?)";
			String base_condition = (String) params.get("base_condition");
			dm_3rd_interface_detail_code = UUID.randomUUID().toString();
			mysqlSpringJdbcBaseDao.insert(insertSql, dm_3rd_interface_detail_code,interface_code,new Gson().toJson(in_para),result,base_condition);
			
			result = CommonUtil.checkBaseCondition_old(result, base_condition, dm_3rd_interface_detail_code, dm_3rd_interface_detail_code);
			if (result == null){
				return null;//checkBaseCondition不通过，直接返回，checkBaseCondition内部已经有错误值+1和修改succ_status操作
			}
			
			String newResult = null;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("result",result);
			if ("c2f2edc1-f5cb-40ab-8f43-5c2bbfceb98a".equals(interface_code)) {
				newResult = YiXinOutParamFormatter.formatLoan(map);
			}
			else if ("9b02a329-66b0-400e-b0b6-e5437d55e150".equals(interface_code)) { //黑名单
				newResult = YiXinOutParamFormatter.formatBlackList(map);
			}
			String succ_status = newResult == null ? "0" : "1";
			mysqlSpringJdbcBaseDao.update("update dm_3rd_interface_detail set result2=? ,succ_status=? where code=?",newResult, succ_status ,dm_3rd_interface_detail_code);
			return newResult;
		} catch (Exception e) {
			e.printStackTrace();
			//更新succ_status为0
			//dm_3rd_interface_detail_code=null或则对应记录不存在时执行update不会影响数据库
			mysqlSpringJdbcBaseDao.update("update dm_3rd_interface_detail set succ_status=? where code=?", "0" ,dm_3rd_interface_detail_code);
			mysqlSpringJdbcBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", interface_code);//error+1
			return null;
		}
	}
	private static String decodeResult(String result,String password){
		Map<String,Object> json = JsonToMap.jsonToMap(result);
		if (json.containsKey("errorcode") && "0000".equals(json.get("errorcode"))) {
			// 解密解码返回结果
			String decryptResult = RC4_128_V2.decode((String) json.get("data"), password);
			try {
				decryptResult = URLDecoder.decode(decryptResult, "utf-8");
				json.put("data", JsonToMap.jsonToMap(decryptResult));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return new Gson().toJson(json);
	}
	private static String getEncryptUserId(String userId){
		RSAPublicKey rsaPublicKey = RSA_1024_V2.gainRSAPublicKeyFromCrtFile(YiXin.class.getResource("util/ZC_PublicKey_V2.crt").getFile());
		String encryptedUserID = RSA_1024_V2.encodeByPublicKey(rsaPublicKey, userId);
		return encryptedUserID;
	}
	
	/**
	 * 制作假数据
	 */
	public static void main(String[] args) throws IOException {
		String data = FileUtils.readFileToString(new java.io.File("D:/data.txt"),"UTF-8");
		String encryptedParams  = RC4_128_V2.encode(URLEncoder.encode(data, "utf-8"),YinXinConsts.password);
		/*{
		    "message": "查询成功",
		    "data":"11nOjAa2Us1rn96w0CNoBMKDEUkfJviT7uUh4X6nceJCBnFzwIRFBU7WuEIYX9B46JulB7XtNhVEnpIX8M3pgUB+NVJrVRjiiPLhnTe6/rlkssVdhMaEau64FaQiUua2kJTIt+m/jJewC05VQkaDXhCOEgqbMIDNF3ymuJea727jz0ItBryzIoA770kV19a/0ToDWWI92/62LDTPPJOAblrX0MN++YbfqprJGueiXNK81hpNPQxx/JjRvrz13YjcwwJZc3PHbB//r6X8VeNP/WGPtNhlu1NrNzJKdTo4ld9tzT8WqbnOhyjX42afpCjBU5XxfvQd+acdrP2xobSy3xTnuLjGOmVaNky7OuE/5DDeFMeGvnQAYP0GDRodZtuq79mbylpoAAZW/aF/LaCBCp0q/qBtEQOjpGx1sR+nUK3hP5eEqPMF6eUPqmQARIrnXMjkWl6kT6HVOO4CIiiCvqvlqU5WxC9nVCigwczCTZ8VU1BR6DYj31Y19XJajLh90xPWwvInacDyE6iRuqtKuqAtagD86p6wKSk16thV/dPX07Urc9LX8FUp9F7fYF/SiZWtIO5MWASCOflJ7McXej5tPO2J0TjMsm/Z1rHmF8tEwtvoJDIGjp3nsdLQNRJ8cCdRtX07FUvBRoO5ByiBKxzvac8i13kNnfPkMq2XEcB2qNql5BckubSwtrozT7y5NrK4LpttrRmzLNbxsCH8nz+SvySTKemXS/cOD7/BKz44leuzDvI49i4Jtub0o+E9aOjnOUowTGxeBhbumWZ9bbINzHy95ff116Tm55O1yXoIxopNlkGHm9phZyo00B0nFd+z47uwuvg7bUTLILUI5jibaXpb0iFnVw1hh9+l0O35fIN2d46i+92hWf+auLHlMpsx9mMYu3pxuA4HmB28u0qQOXyuAeG4q2uMrJtC2c+zo7vd0kPobzyA0BwpALTax1nPWLabxilacQZHafc8fwa8hM4lWoNZyCTMVECoE1QZff0kcmf8BjQIcVUttTzgYL0VGdxFdRADvJSN4HEHbSgHqMKsJfNua4AjhcaBa35VQyu94qaz9FZner4oeSJfffrrq+QJhBxemGmIPKBgtxM=",
			"errorcode": "0000"
		}*/
		System.out.println("{");
		System.out.println("\t\"message\": \"查询成功\",");
		System.out.println("\t\"data\":\""+encryptedParams+"\",");
		System.out.println("\t\"errorcode\": \"0000\"");
		System.out.print("}");
	}
}
