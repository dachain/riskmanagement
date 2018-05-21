package com.newdumai.dumai_data.dm_3rd_interface.para;

import java.util.Map;

import com.newdumai.global.service.Dumai_sourceBaseService;

public interface Dm_3rd_interface_paraService extends Dumai_sourceBaseService {
	public String list(Map<String, Object> para);
	public int add_dm_3rd_interface_para(Map<String, Object> para);
	public int upadte_dm_3rd_interface_para(Map<String, Object> para);

	public String para_toUpdate(String interface_source_code);
}
