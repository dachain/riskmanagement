
package com.newdumai.dumai_data.dm_external_interface.auditOrder;

import java.util.Map;
import java.util.Set;


public interface AuditOrderService {

	public Map<String,Object> getAuditOrderResult(Map<String,Object>params);

	Map<String, Object> getAuditOrderResult(Map<String, Object> orderMap, Set<String> dm_source_codeSet);
}
