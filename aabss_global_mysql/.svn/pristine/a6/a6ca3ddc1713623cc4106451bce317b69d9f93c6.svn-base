package com.newdumai.dumai_data.dm_external_interface.auditOrder.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.newdumai.dumai_data.dm.Dm_sourceService;
import com.newdumai.dumai_data.dm_external_interface.auditOrder.AuditOrderService;
import com.newdumai.global.service.impl.Dumai_sourceBaseServiceImpl;
import com.newdumai.util.IdCardUtils;

@Service(value="auditOrderService")
public class AuditOrderServiceImpl extends Dumai_sourceBaseServiceImpl implements AuditOrderService {

	@Autowired
	Dm_sourceService dm_sourceService;
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAuditOrderResult(Map<String, Object> params) {
		Map<String,Object>returnMap = new HashMap<String,Object>();
		Map<String,Object>orderMap = (Map<String, Object>) params.get("orderMap");
		List<String> dm_source_codeList = null ;
		if(params.get("dmSourceCodeSet") instanceof Set){
			dm_source_codeList = new ArrayList<String>() ;
			Set<String>dm_source_codeSet = (Set<String>) params.get("dmSourceCodeSet");
			for(String dm_source_code :dm_source_codeSet){
				dm_source_codeList.add(dm_source_code);
			}
		}else if(params.get("dmSourceCodeSet") instanceof List){
			dm_source_codeList = (List<String>) params.get("dmSourceCodeSet");
		}
		for(String dm_source_code : dm_source_codeList){
			Map<String,Object>inParams = this.transInParams(orderMap, dm_source_code);
			String result = null;
			if("0ae94bce-a6d3-4580-a999-6a7e85e1e692".equals(dm_source_code)){
				result = this.getDataFromOrder(orderMap);
			}else{
				try {
					result = dm_sourceService.testDM((String)orderMap.get("code"),(String)orderMap.get("sub_entity_id"), dm_source_code, inParams);
				} catch (Exception e) {
					result = null ;
				}
			}
			returnMap.put(dm_source_code, result);
		}
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getAuditOrderResult(Map<String, Object> orderMap,Set<String>dm_source_codeSet) {
		Map<String,Object>returnMap = new HashMap<String,Object>();
		for(String dm_source_code : dm_source_codeSet){
			Map<String,Object>inParams = this.transInParams(orderMap, dm_source_code);
			String result = null;
			if("0ae94bce-a6d3-4580-a999-6a7e85e1e692".equals(dm_source_code)){
				result = this.getDataFromOrder(orderMap);
			}else{
				try {
					result = dm_sourceService.testDM((String)orderMap.get("code"),(String)orderMap.get("sub_entity_id"), dm_source_code, inParams);
				} catch (Exception e) {
					result = null ;
				}
			}
			returnMap.put(dm_source_code, result);
		}
		return returnMap;
	}

	private String getDataFromOrder(Map<String,Object>orderMap){
		String result = null ;
		try {
			Map<String,Object> resultMap = new HashMap<String,Object>();
			String card_num = (String)orderMap.get("card_num");
			String age = String.valueOf(IdCardUtils.getAgeByIdCard(card_num));
			String sex = "M".equals(IdCardUtils.getGenderByIdCard(card_num))?"男":"女";
			resultMap.put("sex", sex);
			resultMap.put("age", age);
			resultMap.put("sqje",  String.valueOf(orderMap.get("sqje")));
			resultMap.put("Jkqx",  String.valueOf(orderMap.get("Jkqx")));
			resultMap.put("hkfs", "等额本息");
			result = new Gson().toJson(resultMap);
		} catch (Exception e) {
			result = null ;
		}
		return result ;
	}
	
	private Map<String,Object> transInParams(Map<String,Object> orderMap,String dm_source_code){
		Map<String,Object>inParams = new HashMap<String,Object>();
		List<Map<String, Object>> nameTransList = dumai_sourceBaseDao.queryForList(this.getTransNameSql(dm_source_code));
		for(Map<String,Object> nameMap:nameTransList){
			String ds_name = (String) nameMap.get("ds_name");
			String order_name = (String) nameMap.get("order_name");
			if(StringUtils.isEmpty(order_name)){
				inParams.put(ds_name, orderMap.get(ds_name));
			}else{
				inParams.put(ds_name, orderMap.get(order_name));
			}
		}
		return inParams;
	}
	
	private String getTransNameSql(String dm_source_code) {
		String sql = " SELECT ";
		sql += " dm_source_para.name as ds_name , ";
		sql += " dm_source_para.fk_orderinfo_name as order_name ";
		sql += " FROM ";
		sql += " dm_source_para ";
		sql += " WHERE ";
		sql += " 1=1 ";
		sql += " and dm_source_para.type = '0' ";
		sql	+= " and dm_source_para.dm_source_code = '"+dm_source_code+"' ";
		return sql;
	}
}
