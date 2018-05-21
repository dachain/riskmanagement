package com.newdumai.dumai_data.dm_3rd_interface.para.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.dm_3rd_interface.para.Dm_3rd_interface_paraService;
import com.newdumai.global.service.impl.Dumai_sourceBaseServiceImpl;


@Service("dm_3rd_interface_paraService")
public class Dm_3rd_interface_paraServiceImpl extends Dumai_sourceBaseServiceImpl implements Dm_3rd_interface_paraService {
	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
	}

	@Override
	public int add_dm_3rd_interface_para(Map<String, Object> para) {
		return add(para,"dm_3rd_interface_para");
	}

	@Override
	public String para_toUpdate(String interface_source_code) {
		return super.dumai_sourceBaseDao.executeSelectSql(gen_para_toUpdate(interface_source_code));
	}

	@Override
	public int upadte_dm_3rd_interface_para(Map<String, Object> para) {
		Map<String, Object> where=new HashMap<String, Object>();
		where.put("code", para.remove("code"));
		return Update(para, "dm_3rd_interface_para", where);
	}
	
	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM dm_3rd_interface_para WHERE 1=1 "+condition;
	}
	
	private String gen_list_2(String condition,String limit) {
		return "SELECT * FROM dm_3rd_interface_para WHERE 1=1 "+condition+ " order by type,opttime desc"+limit;
	}
	
	private Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		String code = (String)map.get("code");
		sb.append(" and dm_3rd_interface_code=?");
		list.add(code);
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	private String gen_para_toUpdate(String dm_3rd_interface_code) {
		return "SELECT * FROM dm_3rd_interface_para WHERE code='"+dm_3rd_interface_code+"'";
	}

}
