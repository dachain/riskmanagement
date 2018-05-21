package com.newdumai.dumai_data.dm_3rd_interface.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newdumai.global.dao.Dumai_newBaseDao;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.SpringApplicationContextHolder;

/**
 * 检查第三方接口返回数据，并更新返回状态是否成功
 * 
 * @author zgl
 * @datetime Dec 15, 2016 11:46:41 AM
 */
public class CommonUtil {
	
	/**
	 * 
	 * @param result JSON字符串
	 * @param expression 表达式
	 * @param outterData 外部数据
	 * @return
	 */
	public static boolean evalExpression(String result, String expression,Map<String,Object> outterData) {
		boolean rs = false;
		try {
			if (StringUtils.isNotEmpty(result)) {
				Map<String, Object> resultMap = JsonToMap.gson2Map(result);
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
				Bindings bindings = jsEngine.createBindings();
				bindings.putAll(resultMap);
				bindings.put("$", outterData);
				rs = (Boolean) jsEngine.eval(expression, bindings);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * 
	 * @param result
	 * @param base_condition
	 * @return
	 */
	public static boolean checkBaseCondition(String result, String base_condition) {
		boolean rs = false;
		try {
			if (StringUtils.isNotEmpty(result)) {
				Map<String, Object> resultMap = JsonToMap.gson2Map(result);
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
				Bindings bindings = jsEngine.createBindings();
				bindings.putAll(resultMap);
				rs = (Boolean) jsEngine.eval(base_condition, bindings);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * @update:[dm_3rd_interface_detail,dm_3rd_interface]
	 * @param result
	 * @param base_condition
	 * @param dm_3rd_interface_code
	 * @param dm_3rd_interface_detail_code
	 * @return
	 * @zgl Dec 15, 2016 11:49:22 AM
	 */
	@Deprecated
	public static String checkBaseCondition_old(String result, String base_condition, String dm_3rd_interface_code, String dm_3rd_interface_detail_code) {
		Dumai_sourceBaseDao mysqlSpringJdbcBaseDao = (Dumai_sourceBaseDao) SpringApplicationContextHolder.getBean("dumai_sourceBaseDao");
		try {
			if (StringUtils.isNotEmpty(result)) {
				boolean rs = false;
				Map<String, Object> resultMap = JsonToMap.gson2Map(result);
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
				Bindings bind = jsEngine.createBindings();
				bind.putAll(resultMap);
				rs = (Boolean) jsEngine.eval(base_condition, bind);
				// 第三方接口请求返回结果
				if (rs) {
					mysqlSpringJdbcBaseDao.update("update dm_3rd_interface_detail set succ_status = '1' where code = ? ", dm_3rd_interface_detail_code);
					return result;
				} else {
					mysqlSpringJdbcBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
					return null;
				}
			} else {
				mysqlSpringJdbcBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			mysqlSpringJdbcBaseDao.update("update dm_3rd_interface set error = error + 1 where code = ? ", dm_3rd_interface_code);
			return null;
		}
	}
}
