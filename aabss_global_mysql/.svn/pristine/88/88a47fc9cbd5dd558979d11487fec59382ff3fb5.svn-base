package com.newdumai.dumai_data.dm_3rd_interface.special_interface.baidu_finance;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.CollectionUtils;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.newdumai.util.Base64Utils;
import com.newdumai.util.DM;
import com.newdumai.util.EncryptUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class finance_rock {
	 public static final String APPID = "1300000010";
	 public static final String KEY = "";
	 public static final String SERVICE_ID = "1001"; 
	 public static final String REQID = "123456";
	 public static final String APIKEY = "7bd2f21783669daa7a923af64516dba814d79c70";
	 /*
	  百度金融磐石系统多头查询API接入文档.pdf
	百度金融磐石系统风险名单查询API接入文档.pdf
	百度金融磐石系统黑产查询API接入文档.pdf
	APPID：1300000010
	apikey:7bd2f21783669daa7a923af64516dba814d79c70
	  * */
	    /**
	    https://jrws.baidu.com/risk/api/info/search?sp_no=1000000001&service_id=1001&datetime=1231231231231&reqid=123
	    &sign_type=1&sign=5dddfd39d92564adf577c298280b7b30&name=%E7%8E%8B%E6%B5%B7%E4%BA%AE&identity=410184198512067694
	    &phone=18600943675
	     * @param dm_3rd_interfaceMap
	     * @param in_para
	     * @return
	     */
	    public static Map<String, Object> getRisk_List(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
	        Map<String, Object> params = new HashMap<String, Object>();
	       String service_id=DM.NullToStr(in_para.get("service_id"));
	       String vTime=String.valueOf(new Date().getTime());
	       String vName=DM.NullToStr(in_para.get("name"),"");
	        params.put("sp_no", APPID);
	        params.put("service_id",service_id);//, DM.NullToStr(in_para.get("service_id")
	        params.put("name",vName);//用户名
	        params.put("identity", DM.NullToStr(in_para.get("identity"),""));//用户身份证号
	        params.put("reqid", REQID);
	        params.put("datetime", vTime);
	        params.put("phone", DM.NullToStr(in_para.get("phone"),""));//用户手机号
	        params.put("sign_type", "1");//签名类型，1-MD5
	       // params.put("key", APIKEY);//签名类型，1-MD5
	        
	         String vUrl="datetime="+ vTime;
	           vUrl =vUrl+"&identity="+ DM.NullToStr(in_para.get("identity"),"")+"&name="+vName;
	           vUrl =vUrl+"&phone="+DM.NullToStr(in_para.get("phone"),"");
	           vUrl=vUrl+"&reqid="+ REQID+"&service_id="+service_id+"&sign_type=1&sp_no="+APPID+"&key="+APIKEY;
	           System.out.println(vUrl);
	        String strSign="";
			try {//vUrl="datetime=1498805354050&identity=43282419740821647X&name=真实人名&phone=15201015156&reqid=123456&service_id=1001&sign_type=1&sp_no=1300000010&key=7bd2f21783669daa7a923af64516dba814d79c70";
				strSign = EncryptUtils.md5(vUrl);
			} catch (Exception e) {
			}
	        params.put("sign", strSign);
	        return params;
	    }
	    public static Map<String, Object> getMutil_Search(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
	    	 Map<String, Object> params = new HashMap<String, Object>();
		        params.put("Appid", APPID);
		        return params;
	    }
	    public static Map<String, Object> getBlack_Industry(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
	    	 Map<String, Object> params = new HashMap<String, Object>();
		        params.put("Appid", APPID);
		        return params;
	    }
	    
	      /*
        Iterator iter = params.entrySet().iterator(); 
			  while(iter.hasNext())  //第二种迭代方式取键值  
			  {
			    Map.Entry entry = (Map.Entry)iter.next(); 
			    vUrl=vUrl+ (!vUrl.equals("") ? "&" : "") +entry.getKey()+"="+ entry.getValue();
			  
			  }*/
	    
}
