package cn.hy.netfiletool.common;

import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class WifiUtil {

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

	private final static int INADDRSZ = 4;

	/**
	 * 把IP地址转化为字节数组
	 * 
	 * @param ipAddr
	 * @return byte[]
	 */
	public static byte[] ipToBytesByInet(String ipAddr) {
		try {
			return InetAddress.getByName(ipAddr).getAddress();
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static byte[] ipToBytesByReg(String ipAddr) {
		byte[] ret = new byte[4];
		try {
			String[] ipArr = ipAddr.split("\\.");
			ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
			return ret;
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}

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

	/**
	 * 把IP地址转化为int
	 * 
	 * @param ipAddr
	 * @return int
	 */
	public static int ipToInt(String ipAddr) {
		try {
			return bytesToInt(ipToBytesByInet(ipAddr));
		} catch (Exception e) {
			throw new IllegalArgumentException(ipAddr + " is invalid IP");
		}
	}

	/**
	 * ipInt -> byte[]
	 * 
	 * @param ipInt
	 * @return byte[]
	 */
	public static byte[] intToBytes(int ipInt) {
		byte[] ipAddr = new byte[INADDRSZ];
		ipAddr[0] = (byte) ((ipInt >>> 24) & 0xFF);
		ipAddr[1] = (byte) ((ipInt >>> 16) & 0xFF);
		ipAddr[2] = (byte) ((ipInt >>> 8) & 0xFF);
		ipAddr[3] = (byte) (ipInt & 0xFF);
		return ipAddr;
	}

	/**
	 * 把int->ip地址
	 * 
	 * @param ipInt
	 * @return String
	 */
	public static String intToIp(int ipInt) {
		return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
				.append((ipInt >> 16) & 0xff).append('.')
				.append((ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
				.toString();
	}

	/**
	 * 把192.168.1.1/24 转化为int数组范围
	 * 
	 * @param ipAndMask
	 * @return int[]
	 */
	public static int[] getIPIntScope(String ipAndMask) {

		String[] ipArr = ipAndMask.split("/");
		if (ipArr.length != 2) {
			throw new IllegalArgumentException("invalid ipAndMask with: "
					+ ipAndMask);
		}
		int netMask = Integer.valueOf(ipArr[1].trim());
		if (netMask < 0 || netMask > 31) {
			throw new IllegalArgumentException("invalid ipAndMask with: "
					+ ipAndMask);
		}
		int ipInt = ipToInt(ipArr[0]);
		int netIP = ipInt & (0xFFFFFFFF << (32 - netMask));
		int hostScope = (0xFFFFFFFF >>> netMask);
		return new int[] { netIP, netIP + hostScope };

	}

	/**
	 * 把192.168.1.1/24 转化为IP数组范围
	 * 
	 * @param ipAndMask
	 * @return String[]
	 */
	public static String[] getIPAddrScope(String ipAndMask) {
		int[] ipIntArr = getIPIntScope(ipAndMask);
		return new String[] { intToIp(ipIntArr[0]), intToIp(ipIntArr[0]) };
	}

	/**
	 * 根据IP 子网掩码（192.168.1.1 255.255.255.0）转化为IP段
	 * 
	 * @param ipAddr
	 *            ipAddr
	 * @param mask
	 *            mask
	 * @return int[]
	 */
	public static int[] getIPIntScope(String ipAddr, String mask) {

		int ipInt;
		int netMaskInt = 0, ipcount = 0;
		try {
			ipInt = ipToInt(ipAddr);
			if (null == mask || "".equals(mask)) {
				return new int[] { ipInt, ipInt };
			}
			netMaskInt = ipToInt(mask);
			ipcount = ipToInt("255.255.255.255") - netMaskInt;
			int netIP = ipInt & netMaskInt;
			int hostScope = netIP + ipcount;
			return new int[] { netIP, hostScope };
		} catch (Exception e) {
			throw new IllegalArgumentException("invalid ip scope express  ip:"
					+ ipAddr + "  mask:" + mask);
		}

	}

	/**
	 * 根据IP 子网掩码（192.168.1.1 255.255.255.0）转化为IP段
	 * 
	 * @param ipAddr
	 *            ipAddr
	 * @param mask
	 *            mask
	 * @return String[]
	 */
	public static String[] getIPStrScope(String ipAddr, String mask) {
		int[] ipIntArr = getIPIntScope(ipAddr, mask);
		return new String[] { intToIp(ipIntArr[0]), intToIp(ipIntArr[0]) };
	}

	/**
	 * 将整数形式的ip地址转换为字符串形式
	 * 
	 * @param ipInBigInt
	 *            整数形式的ip地址
	 * @return 字符串形式的ip地址
	 */
	public static String bigIntToString(BigInteger ipInBigInt) {
		byte[] bytes = ipInBigInt.toByteArray();
		byte[] unsignedBytes = Arrays.copyOfRange(bytes, 1, bytes.length);
		if (bytes.length == 4 || bytes.length == 16) {
			unsignedBytes = bytes;
		}
		// 去除符号位
		try {
			String ip = InetAddress.getByAddress(unsignedBytes).toString();
			return ip.substring(ip.indexOf('/') + 1).trim();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 对比两个ip是否处于同一网段
	 * @param gateWay
	 * @param serverIp
	 * @return
	 */
	public static boolean isSameSegment(String gateWay,String serverIp){
		String[] gate = gateWay.split("\\.");
		String[] server = serverIp.split("\\.");
		if(server.length<3){
			Log.w("serverIp", serverIp);
			return false;
		}
		for(int i=0;i<3;i++){
			if(!gate[i].equals(server[i])){
				return false;
			}
		}
		return true;
	}
}