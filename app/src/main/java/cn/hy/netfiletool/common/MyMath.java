package cn.hy.netfiletool.common;

import cn.hy.netfiletool.box.ConstStrings;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MyMath {

	/**
	 * 精确除法计算
	 * @param divisor1
	 * @param divisor2
	 * @param format
	 * @return
	 */
	public static String divide(long divisor1,long divisor2,String format){
		BigDecimal b1 = new BigDecimal(Long.toString(divisor1));
		BigDecimal b2 = BigDecimal.valueOf(divisor2);
		DecimalFormat df = new DecimalFormat(format);
		return df.format(b1.divide(b2));
	}

	public static void main(String[]arg){
		System.out.print(MyMath.divide(136823, 1024, ConstStrings.DivideFormat));
	}
	/**
	 * 精确计算占比 以100为最大值
	 * @param current
	 * @param max
	 * @return
	 */
	public static int divideMax100(long current,long max){
		BigDecimal b1 = new BigDecimal(Long.toString(current));
		BigDecimal b2 = BigDecimal.valueOf(max);
		BigDecimal b3 = BigDecimal.valueOf(100);
		MathContext mc = new MathContext(2, RoundingMode.DOWN);
		return b1.divide(b2,mc).multiply(b3).intValue();
	}
	
	/*public static void main(String[] args) {
		System.out.println(divideMax100(654654,54585));
	}*/
	
	/**
	 * 精确加法计算
	 * @param divisor1
	 * @param divisor2
	 * @return
	 */
	public static long add(long divisor1,long divisor2){
		BigDecimal b1 = new BigDecimal(Long.toString(divisor1));
		BigDecimal b2 = BigDecimal.valueOf(divisor2);
		return b1.add(b2).longValue();
	}
	
	/**
	 * 精确减法计算
	 * @param divisor1
	 * @param divisor2
	 * @return
	 */
	public static long subtract(long divisor1,long divisor2){
		BigDecimal b1 = new BigDecimal(Long.toString(divisor1));
		BigDecimal b2 = BigDecimal.valueOf(divisor2);
		return b1.subtract(b2).longValue();
	}
}
