package com.newdumai.dumai_data.dm_3rd_interface.special_interface.yimeiruantong;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.newdumai.util.DM;
import com.newdumai.util.JsonToMap;

/**
APPID：ABF112D9WC711W4C68W966CW3B6C978DE09E
AppSecret：42DB4F59LF585L411ELBD57L12CEE033FD26 
SDKKEY：26837E4DH42CDH4611H8BDAH23E6D3B48AD3
**/
public class black_list {
	public static final String APPID = "8C457672WE7EBW48CFW8EC7W36717EF332BC";
	public static final String KEY = "C547EC4CH777EH445BHA0F1H358252F13633";
	public static final String AppSecret = "74EAE467LE196L42C0L8ED7L30B58761A67D";
	public static String username = "Sdmkj";
	public static String userpassowrd = "a88888888";
	public static long Tokentimestamp = 0;
	public static String strAccess_Token = "";
	public static long TokenEffective = 0;

	public static String getAccess_Token(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
		long nowtimestamp = new Date().getTime();// nowtimestamp -
													// Tokentimestamp > (7000 *
													// 1000) ||
		if (nowtimestamp > (TokenEffective + 5000) || strAccess_Token.equals("")) {
			TokenEffective = nowtimestamp;
			String endpoint = "http://opensdk.emay.cn:9080/HD_GetAccess_Token.asmx";
			Call call;
			String res = " ";
			String vDateEffective = "";
			Service service = new Service();
			try {
				call = (Call) service.createCall();
				call.setTargetEndpointAddress(new java.net.URL(endpoint));
				call.setOperationName(new QName("http://tempuri.org/", "GetACCESS_TOKEN"));
				call.addParameter(new QName("http://tempuri.org/", "AppID"),
						org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
				call.addParameter(new QName("http://tempuri.org/", "AppSecret"),
						org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN);
				call.addParameter(new QName("http://tempuri.org/", "Key"), org.apache.axis.encoding.XMLType.XSD_STRING,
						ParameterMode.IN);
				call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
				call.setUseSOAPAction(true);
				call.setSOAPActionURI("http://tempuri.org/GetACCESS_TOKEN");
				// APPID,KEY,AppSecret username userpassowrd
				res = (String) call.invoke(new Object[] { APPID, AppSecret, KEY });
				strAccess_Token = DM.NullToStr(JsonToMap.gson2Map(res).get("access_token"));// {"access_token":"812D92C112644348A614CEFDF82D9CBF5686A708E8BAEB770FCC400B6B32725EA9B1AF742A98476089425201C5A3851A","Effective":"2017/7/3
																							// 13:39:53"}
				if (!res.equals("")) {
					vDateEffective = DM.NullToStr(JsonToMap.gson2Map(res).get("Effective"));
					if (!vDateEffective.equals("") && DM.IsdateTime(vDateEffective)) {
						TokenEffective = DM.toDateLong(vDateEffective);
					}
				}
				if (res.indexOf("errcode") > 0) {// {"errcode":-10002,"errmsg":"IP授权失败"}
					System.out.println("getAccess_Token 发生意外:" + res);
				}
				// System.err.println("==11=>"+res);
			} catch (ServiceException e) {
				DM.getErrorMsg(e, "3", "getAccess_Token", " 发生意外:" + strAccess_Token, true);
			} catch (MalformedURLException e) {
				DM.getErrorMsg(e, "3", "getAccess_Token", " 发生意外:" + strAccess_Token, true);
			} catch (RemoteException e) {
				DM.getErrorMsg(e, "3", "getAccess_Token", " 发生意外:" + strAccess_Token, true);
			} catch (Exception e) {
				DM.getErrorMsg(e, "3", "getAccess_Token", " 发生意外:" + strAccess_Token, true);
				// e.printStackTrace();
			} finally {
			}
			return strAccess_Token;
		}
		return strAccess_Token;
	}

	public static String getBlack_List(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
		return IS_Be_Overdue_Black(dm_3rd_interfaceMap, in_para);
	}

	public static String IS_Be_Overdue_Black(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
		Map<String, Object> params = new HashMap<String, Object>();
		String Phone = DM.NullToStr(in_para.get("Phone"));
		getAccess_Token(dm_3rd_interfaceMap, in_para);
		String endpoint = "http://opensdk.emay.cn:9080/HD_Check/User_CheckService.asmx"; // 换下相应接口地址
		Call call;
		String res = "";
		Service service = new Service();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://tempuri.org/", "IS_Be_Overdue_Black")); // 接口名称

			call.addParameter(new QName("http://tempuri.org/", "Phone"), org.apache.axis.encoding.XMLType.XSD_STRING,
					ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "ACCESS_TOKEN"),
					org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN); // 参数

			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("http://tempuri.org/IS_Be_Overdue_Black"); //// 接口名称

			res = (String) call.invoke(new Object[] { Phone, strAccess_Token });
			// System.err.println("==11=>"+res);
		} catch (ServiceException e) {
			DM.getErrorMsg(e, "3", "", "IS_Be_Overdue_Black 函数， " + Phone + "用户 发生意外:" + res, true);
		} catch (MalformedURLException e) {
			DM.getErrorMsg(e, "3", "", "IS_Be_Overdue_Black 函数， " + Phone + " 发生意外:" + res, true);
		} catch (RemoteException e) {
			DM.getErrorMsg(e, "3", "", "IS_Be_Overdue_Black 函数， " + Phone + " 发生意外:" + res, true);
		} catch (Exception e) {
			DM.getErrorMsg(e, "3", "", "IS_Be_Overdue_Black 函数， " + Phone + " 发生意外:" + res, true);
		} finally {
		}
		return res;
	}

	public static String GetEmda_xdzh(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
		String vAccess_Token = getAccess_Token(dm_3rd_interfaceMap, in_para);
		String endpoint = "http://opensdk.emay.cn:9080/MADE_API/emda_xdzh.asmx"; // 换下相应接口地址
		Call call;
		String res = " ";
		String Phone = "";
		Service service = new Service();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://tempuri.org/", "GetEmda_xdzh")); // 接口名称
			call.addParameter(new QName("http://tempuri.org/", "Phone"), org.apache.axis.encoding.XMLType.XSD_STRING,
					ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "cycle"), org.apache.axis.encoding.XMLType.XSD_STRING,
					ParameterMode.IN);
			call.addParameter(new QName("http://tempuri.org/", "ACCESS_TOKEN"),
					org.apache.axis.encoding.XMLType.XSD_STRING, ParameterMode.IN); // 参数
			call.addParameter(new QName("http://tempuri.org/", "Platform"), org.apache.axis.encoding.XMLType.XSD_STRING,
					ParameterMode.IN);
			/*
			 * Phone 手机号码 cycle 时间段(1,3,6,9,12,24)单位:月 ACCESS_TOKEN 有效凭证
			 * Platform 平台类型(0 全部 1 银行 2 非银行)
			 */
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
			call.setUseSOAPAction(true);
			call.setSOAPActionURI("http://tempuri.org/GetEmda_xdzh"); //// 接口名称
			Phone = DM.NullToStr(in_para.get("Phone"));
			String cycle = DM.NullToStr(in_para.get("cycle"));// "24";
			String Platform = DM.NullToStr(in_para.get("Platform"), "0");
			;
			res = (String) call.invoke(new Object[] { Phone, cycle, strAccess_Token, Platform });
			if (res.indexOf("CODE") < 0 || res.indexOf("200") < 0) {
				System.out.println("发生意外情况：GetEmda_xdzh（" + Phone + "）的结果如下 " + res);
			}
		} catch (ServiceException e) {
			DM.getErrorMsg(e, "3", "GetEmda_xdzh", " 发生意外: " + Phone + " 用户数据如下 " + res, true);
		} catch (MalformedURLException e) {
			DM.getErrorMsg(e, "3", "GetEmda_xdzh", " 发生意外:  " + Phone + " 用户数据如下 " + res, true);
		} catch (RemoteException e) {
			DM.getErrorMsg(e, "3", "GetEmda_xdzh", " 发生意外:  " + Phone + " 用户数据如下 " + res, true);
		} catch (Exception e) {
			DM.getErrorMsg(e, "3", "GetEmda_xdzh", " 发生意外:  " + Phone + " 用户数据如下 " + res, true);
		} finally {
		}
		return res;

	}

	/*
	 * Iterator iter = params.entrySet().iterator(); while(iter.hasNext())
	 * //第二种迭代方式取键值 { Map.Entry entry = (Map.Entry)iter.next(); vUrl=vUrl+
	 * (!vUrl.equals("") ? "&" : "") +entry.getKey()+"="+ entry.getValue();
	 * 
	 * }
	 */

}
