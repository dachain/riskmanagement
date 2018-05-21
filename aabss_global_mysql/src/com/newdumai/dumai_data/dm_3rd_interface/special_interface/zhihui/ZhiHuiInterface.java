package com.newdumai.dumai_data.dm_3rd_interface.special_interface.zhihui;

import com.newdumai.util.EncryptUtils;
import com.newdumai.util.HttpClientUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 智汇接口
 * Created by zhang on 2017/6/21.
 */
public class ZhiHuiInterface {

    //智汇apikey
    public static final String apikey = "45cb6252-03e7-4762-ad05-04dcb12e6967";
    //智汇密钥
    public static final String password = "10293847";

    /**
     * 查询理赔记录信息
     *
     * @param dm_3rd_interfaceMap
     * @param in_para
     * @return
     */
    public static String getRecord(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
        String result = null;
        Map<String, Object> params = new HashMap<String, Object>();

        //保单号
        String policynumber = (String) in_para.get("policynumber");
        //身份证号
        String cardno = (String) in_para.get("cardno");
        //保险公司 ID
        String insurancecompanyid = (String) in_para.get("insurancecompanyid");

        params.put("apikey", EncryptUtils.encrypt(apikey, password));
        params.put("policynumber", EncryptUtils.encrypt(policynumber, password));
        params.put("cardno", EncryptUtils.encrypt(cardno, password));
        params.put("insurancecompanyid", EncryptUtils.encrypt(insurancecompanyid, password));

        String url = (String) dm_3rd_interfaceMap.get("url");
        String is_post = (String) dm_3rd_interfaceMap.get("is_post");
        String is_json = (String) dm_3rd_interfaceMap.get("in_type");
        String str = HttpClientUtil.exec(url, is_post, is_json, params);
        if (StringUtils.isNotEmpty(str)) {
            result = EncryptUtils.decrypt(str, password);
        }
        System.out.println("查询理赔记录：" + result);
        return result;
    }

    /**
     * 保险公司ID查询
     *
     * @param dm_3rd_interfaceMap
     * @param inPara
     * @return
     */
    public static String getCompanyId(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> inPara) {
        String result = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("apikey", EncryptUtils.encrypt(apikey, password));
        String url = (String) dm_3rd_interfaceMap.get("url");
        String is_post = (String) dm_3rd_interfaceMap.get("is_post");
        String is_json = (String) dm_3rd_interfaceMap.get("in_type");
        String str = HttpClientUtil.exec(url, is_post, is_json, params);
        if (StringUtils.isNotEmpty(str)) {
            result = EncryptUtils.decrypt(str, password);
        }
        System.out.println("智汇请求公司ID：" + result);
        return result;
    }

    /**
     * 车辆维保查询
     *
     * @param dm_3rd_interfaceMap
     * @param inPara
     * @return
     */
    public static String carMrBigKing(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> inPara) {
        String result = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("apikey", EncryptUtils.encrypt(apikey, password));
        String vin = (String) inPara.get("vin");
        params.put("vin", EncryptUtils.encrypt(vin, password));

        String url = (String) dm_3rd_interfaceMap.get("url");
        String is_post = (String) dm_3rd_interfaceMap.get("is_post");
        String is_json = (String) dm_3rd_interfaceMap.get("in_type");
        String str = HttpClientUtil.exec(url, is_post, is_json, params);
        if (StringUtils.isNotEmpty(str)) {
            result = EncryptUtils.decrypt(str, password);
        }
        System.out.println("查询维保信息：" + result);
        return result;
    }

    /**
     * 车辆在保查询
     *
     * @param dm_3rd_interfaceMap
     * @param in_para
     * @return
     */
    public static String xqInsuranceRecord(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
        String result = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("apikey", EncryptUtils.encrypt(apikey, password));

        //保单号
        String policynumber = (String) in_para.get("policynumber");
        //身份证号
        String cardno = (String) in_para.get("cardno");
        //保险公司 ID
        String insurancecompanyid = (String) in_para.get("insurancecompanyid");

        params.put("apikey", EncryptUtils.encrypt(apikey, password));
        params.put("policynumber", EncryptUtils.encrypt(policynumber, password));
        params.put("cardno", EncryptUtils.encrypt(cardno, password));
        params.put("insurancecompanyid", EncryptUtils.encrypt(insurancecompanyid, password));

        String url = (String) dm_3rd_interfaceMap.get("url");
        String is_post = (String) dm_3rd_interfaceMap.get("is_post");
        String is_json = (String) dm_3rd_interfaceMap.get("in_type");
        String str = HttpClientUtil.exec(url, is_post, is_json, params);
        if (StringUtils.isNotEmpty(str)) {
            result = EncryptUtils.decrypt(str, password);
        }
        System.out.println("查询在保信息：" + result);
        return result;
    }


    /**
     * 法人对外投资
     *
     * @param dm_3rd_interfaceMap
     * @param inPara
     * @return
     */
    public static String businessPersonQuery(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> inPara) {
        String result = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("apikey", EncryptUtils.encrypt(apikey, password));
        String idcard = (String) inPara.get("idcard");
        params.put("key", EncryptUtils.encrypt(idcard, password));
        params.put("keytype", EncryptUtils.encrypt("4", password));

        String url = (String) dm_3rd_interfaceMap.get("url");
        String is_post = (String) dm_3rd_interfaceMap.get("is_post");
        String is_json = (String) dm_3rd_interfaceMap.get("in_type");
        String str = HttpClientUtil.exec(url, is_post, is_json, params);
        if (StringUtils.isNotEmpty(str)) {
            result = EncryptUtils.decrypt(str, password);
        }
        System.out.println("法人对外投资：" + result);
        return result;
    }

}
