package com.newdumai.dumai_data.dm_3rd_interface.special_interface.pingAn;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm_3rd_interface.special_interface.pingAn.util.PingAnHttpHelper;
import com.newdumai.dumai_data.dm_3rd_interface.util.CommonUtil;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.util.SpringApplicationContextHolder;

public class PingAn {

	
	/*@Test
	public void doTest(){
		Map<String,Object> params = new HashMap<String,Object>();
		
		String name = "井庆生";
		String idCard = "370102197508262532";
		String phone = "13581722270";
		params.put("name", name);
		params.put("idCard", idCard);
		params.put("phone", phone);
		String url = "https://i.trustutn.org/b/blacklist";
		params.put("url", url);
		String result = PingAn.QueryPingAn(params);
		System.out.println(result);
	}*/
	
	
	
	/**
	 * 凭安数据源查询
	 * @param params
	 * @return
	 */
	public static String QueryPingAn(Map<String,Object> params){
		String url = params.get("url").toString();
		String interface_code = (String) params.get("interface_source_code");
		String result = null;
		
		@SuppressWarnings("unchecked")
		Map<String,Object> in_para = (Map<String, Object>) params.get("in_para");
		
		Dumai_sourceBaseDao mysqlSpringJdbcBaseDao = (Dumai_sourceBaseDao) SpringApplicationContextHolder.getBean("dumai_sourceBaseDao");
		String dm_3rd_interface_detail_code = null;
		try {
			Map<String, Object> sendData = setParams(in_para);
			PingAnHttpHelper dsh = new PingAnHttpHelper();
			dsh.getBaseParams(sendData);
			//result = HttpClientUtil.exec(url, "1", "0", sendData);
			
			CloseableHttpClient httpclient = dsh.getHttpClient(url);
			HttpPost post = dsh.postForm(url, sendData);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60000).build();
			post.setConfig(requestConfig);
			HttpResponse response = httpclient.execute(post);
			if(response.getStatusLine().getStatusCode()==200){
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
			httpclient.close();
			//return result;
			
			String insertSql = "INSERT INTO dm_3rd_interface_detail(code, dm_3rd_interface_code, in_para, result, base_condition) VALUES (?,?,?,?,?)";
			String base_condition = (String) params.get("base_condition");
			dm_3rd_interface_detail_code = UUID.randomUUID().toString();
			mysqlSpringJdbcBaseDao.insert(insertSql, dm_3rd_interface_detail_code,interface_code,new Gson().toJson(in_para),result,base_condition);
			
			result = CommonUtil.checkBaseCondition_old(result, base_condition, dm_3rd_interface_detail_code, dm_3rd_interface_detail_code);
			
			if (result == null) {
				return null;
			}
			
			String newResult = null;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("result",result);
			if ("897e2877-1030-48a2-a696-c81abdcdaab8".equals(interface_code)) {
				newResult = PingAnOutParamFormatter.getContacts(map);
			}
			else if ("197e2871-1030-48a2-a696-c81abdcdaab1".equals(interface_code)) {
				newResult = PingAnOutParamFormatter.getBlackList(map);
			}
			else if ("f9e68a53-126f-4c58-bc73-6431f744b4fa".equals(interface_code)) {
				newResult = PingAnOutParamFormatter.getOverdue(map);
			}
			String succ_status = newResult == null ? "0" : "1";
			mysqlSpringJdbcBaseDao.update("update dm_3rd_interface_detail set result2=? ,succ_status=? where code=?",newResult, succ_status ,dm_3rd_interface_detail_code);
			return newResult;
			
		} catch (Exception e) {
			e.printStackTrace();
			//更新succ_status为0
			//dm_3rd_interface_detail_code=null或则对应记录不存在时执行update不会影响数据库
			mysqlSpringJdbcBaseDao.update("update dm_3rd_interface_detail set succ_status=? where code=?", "0" ,dm_3rd_interface_detail_code);
			mysqlSpringJdbcBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", interface_code);
			return null;
		}
		
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
