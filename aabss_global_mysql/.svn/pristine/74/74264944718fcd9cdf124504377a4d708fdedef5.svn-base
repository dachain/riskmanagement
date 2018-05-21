package com.newdumai.dumai_data.dm_3rd_interface.special_interface.rongshu;

import com.newdumai.util.Base64Utils;
import com.newdumai.util.EncryptUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang on 2017/6/22.
 */
public class RongShuInterface {

    public static final String APPID = "iqianbang";
    public static final String KEY = "d4f3ca69bce6acda667448a4057cf8bd";

    /**
     * 两张照片比对:人脸无源比对接口
     *
     * @param dm_3rd_interfaceMap
     * @param in_para
     * @return
     */
    public static Map<String, Object> comparePhoto(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("Appid", APPID);
        String photoPath = ((String) in_para.get("Photo")).replace("\\", "/");
        String sourcePhotoPath = ((String) in_para.get("SourcePhoto")).replace("\\", "/");
        String photo = Base64Utils.getImageStr(photoPath);
        String sourcePhoto = Base64Utils.getImageStr(sourcePhotoPath);
        params.put("Photo", photo);
        params.put("SourcePhoto", sourcePhoto);
        String sign = EncryptUtils.md5(photo + sourcePhoto + KEY);
        params.put("Sign", sign);
        return params;
    }

    /**
     * 到照片库进行比对：融API-照库比对接口文档
     *
     * @param dm_3rd_interfaceMap
     * @param in_para
     * @return
     */
    public static Map<String, Object> photochk(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
        Map<String, Object> params = new HashMap<String, Object>();
        String photoBase64 = "";
        String photoPath = (String) in_para.get("Photo");
        if (photoPath.startsWith("/9j/")) {
            photoBase64 = photoPath;
        } else {
            photoPath = photoPath.replace("\\", "/");
            photoBase64 = Base64Utils.getImageStr(photoPath);
        }

        String name = (String) in_para.get("Name");
        String idCode = (String) in_para.get("IdCode");
        String merge = idCode + name + photoBase64 + KEY;
        String sign = EncryptUtils.md5(merge);

        params.put("Name", name);
        params.put("IdCode", idCode);
        params.put("Photo", photoBase64);
        params.put("Appid", APPID);
        params.put("Sign", sign);
        return params;
    }

    /**
     * 
     * @param dm_3rd_interfaceMap
     * @param in_para
     * @return
     */
    public static Map<String, Object>bankthrelem(Map<String, Object> dm_3rd_interfaceMap, Map<String, Object> in_para) {
        in_para.put("Appid", APPID);
        String Name = (String) in_para.get("Name");
        String IdCode = (String) in_para.get("IdCode");
        String CardNo = (String) in_para.get("CardNo");
        String sign = EncryptUtils.md5(CardNo + IdCode + Name + KEY);
        in_para.put("Sign", sign);
        return in_para;
    }
}
