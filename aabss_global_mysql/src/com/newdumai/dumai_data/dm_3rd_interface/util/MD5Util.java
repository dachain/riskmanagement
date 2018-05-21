package com.newdumai.dumai_data.dm_3rd_interface.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


public class MD5Util {
	
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	/**
	 * md5加密并转换为十六进制字符串
	 * @param s 加密字符串
	 * @param charset 字符编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static final String toHex(String s,String charset) throws UnsupportedEncodingException {
		return toHex(s.getBytes(charset));
	}
	
	/**
	 * md5加密并转换为十六进制字符串
	 * @param s 加密字符串
	 * @return
	 */
	public static final String toHex(String s ){
		try {
			return toHex(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * md5加密并转换为十六进制字符串
	 * @param btInput
	 * @return
	 */
	public static final String toHex(byte[] btInput) {
		byte[] md = toBytes(btInput);
		// 把密文转换成十六进制的字符串形式
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte bytei = md[i];
			str[k++] = hexDigits[bytei >>> 4 & 0xf];
			str[k++] = hexDigits[bytei & 0xf];
		}
		return new String(str);
	}
	
	/**
	 * md5加密
	 * @param btInput 需要加密的字节数组
	 * @return 加密后的字节数组
	 */
	public static final byte[] toBytes(byte[] btInput){
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			return md;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}