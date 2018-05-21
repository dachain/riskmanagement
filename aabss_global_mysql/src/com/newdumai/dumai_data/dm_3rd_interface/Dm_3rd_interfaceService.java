package com.newdumai.dumai_data.dm_3rd_interface;

import java.util.Map;

import com.newdumai.global.service.Dumai_sourceBaseService;

public interface Dm_3rd_interfaceService extends Dumai_sourceBaseService {
	//public String list(Map<String, Object> para);
	public int add_dm_3rd_interface(Map<String, Object> para);
	public int upadte_dm_3rd_interface(Map<String, Object> para);
	public String toUpdate(String dm_3rd_interface);
	
	public float getInterfacesCost(String interfacesCodes);
	public String getDm_3rd_interface_companyList();
	
	public String toTestDS(String code);
	public String testDS(String dm_3rd_interface_code, Map<String, Object> map);
	public String get3rd_interfaces_by_interface_company_code(String interface_company_code);
	public String getDm_3rd_interface_para(String Interface_source_code, String inOrOut);
	public Map<String, Object> validateInputOrder(Map<String, Object> orderInfo, String... validateIndentifies);
	public Map<String, Object> validateInputOrderAll(Map<String, Object> order);
	public String testDSRaw(String dm_3rd_interface_code, Map<String, Object> inPara);
	public String testDS2(String dm_3rd_interface_code, Map<String, Object> map);
	String testDS0(String dm_3rd_interface_code, Map<String, Object> map);
	//对外接口，不判断base_condition条件
	String out(String dm_3rd_interface_code, Map<String, Object> map);
}
