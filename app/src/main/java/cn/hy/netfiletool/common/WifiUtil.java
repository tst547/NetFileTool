package cn.hy.netfiletool.common;

import java.util.ArrayList;
import java.util.List;

public class WifiUtil {

	/**
	 * long型IP地址转字符串格式（例：192.168.1.1）
	 * @param ip
	 * @return
	 */
	public static String long2ip(long ip) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf((int) ((ip >> 24) & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 16) & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 8) & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) (ip & 0xff)));
		return sb.toString();
	}

	/**
	 * 字符串IP转long型IP
	 * @param ip
	 * @return
	 */
	public static long ip2long(String ip) {
		String[] ipArray = ip.split("\\.");
		List ipNums = new ArrayList();
		for (int i = 0; i < 4; ++i) {
			ipNums.add(Long.valueOf(Long.parseLong(ipArray[i].trim())));
		}
		long ZhongIPNumTotal = ((Long) ipNums.get(0)).longValue() * 256L * 256L * 256L
				+ ((Long) ipNums.get(1)).longValue() * 256L * 256L + ((Long) ipNums.get(2)).longValue() * 256L
				+ ((Long) ipNums.get(3)).longValue();

		return ZhongIPNumTotal;
	}

	/**
	 * long型IP地址转字符串格式
	 * android api返回的IP地址四个字节是反向的
	 * 普通转换会出现问题（例：1.1.168.192）
	 * 所以对其进行特殊处理（反向转换）
	 * @param ip
	 * @return
	 */
	public static String androidLong2ip(long ip) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf((int) (ip & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 8) & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 16) & 0xff)));
		sb.append('.');
		sb.append(String.valueOf((int) ((ip >> 24) & 0xff)));
		return sb.toString();
	}

	/**
	 * 字符串IP转long型IP
	 * 相反与正常转换 该方法将对字符串地址取反
	 * 适用于字符串IP反向转为正向
	 * 例：1.1.168.192(字符串) 转 192.168.1.1(long值)
	 * @param ip
	 * @return
	 */
	public static long androidIp2long(String ip) {
		String[] ipArray = ip.split("\\.");
		List ipNums = new ArrayList();
		for (int i = 0; i < 4; ++i) {
			ipNums.add(Long.valueOf(Long.parseLong(ipArray[i].trim())));
		}
		long ZhongIPNumTotal = ((Long) ipNums.get(3)).longValue() * 256L * 256L * 256L
				+ ((Long) ipNums.get(2)).longValue() * 256L * 256L + ((Long) ipNums.get(1)).longValue() * 256L
				+ ((Long) ipNums.get(0)).longValue();

		return ZhongIPNumTotal;
	}

	/**
	 * 字节数组转化为IP
	 *
	 * @param bytes
	 * @return int
	 */
	public static String bytesToIp(byte[] bytes) {
		return new StringBuffer().append(bytes[0] & 0xFF).append('.')
				.append(bytes[1] & 0xFF).append('.').append(bytes[2] & 0xFF)
				.append('.').append(bytes[3] & 0xFF).toString();
	}

	/**
	 * 根据位运算把 byte[] -> int
	 *
	 * @param bytes
	 * @return int
	 */
	public static int bytesToInt(byte[] bytes) {
		int addr = bytes[3] & 0xFF;
		addr |= ((bytes[2] << 8) & 0xFF00);
		addr |= ((bytes[1] << 16) & 0xFF0000);
		addr |= ((bytes[0] << 24) & 0xFF000000);
		return addr;
	}

}
