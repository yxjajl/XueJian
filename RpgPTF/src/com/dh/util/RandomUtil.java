package com.dh.util;

import java.util.Random;

public class RandomUtil {

	private static final Random rd = new Random();
	private final static int RANDOM_MAX = 10000;
	
	
	/**
	 * 返回0~x以内的随机整数，包含0不包含x
	 *  
	 * @return int
	 * @author:zqgame 
	 * @date:2012-12-14
	 */
	public static int randomInt(int x){
		return rd.nextInt(x);
	}
	
	/**
	 * 返回0~100以内的随机整数，包含0不包含100
	 *  
	 * @return int
	 * @author:zqgame 
	 * @date:2012-12-14
	 */
	public static int randomInt(){
		return rd.nextInt(RANDOM_MAX);
	}

	/**
	 * @desc return 0~100 pseudorandom,inclusive 0 and exclusive 100
	 * @return int
	 * @author:zqgame 
	 * @date:2012-12-14
	 */
	public static double randomDouble(){
		return rd.nextDouble() * RANDOM_MAX;
	}
	
	/**
	 * 整数范围随机
	 * @param a
	 * @param b
	 * @return
	 */
	public static int roundRandom(int a, int b){
		int temp = 0;
		try {
			if(a > b){
				a++;
				temp = rd.nextInt(a - b);
				return temp + b;
			}
			else if(b > a){
				b++;
				temp = rd.nextInt(b - a);
				return temp + a;
			}
			else{
				return 0;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return temp + a;
	}
	/**
	 * 浮点范围随机数
	 *  
	 * @param a
	 * @param b
	 * @return double
	 * @author:zqgame 
	 * @date:2012-12-14
	 */
	public static double roundRandom(double a,double b){
		double temp = 0.0;
		int nextInt = 0;
		try {
			if(a > b){
				temp = rd.nextDouble();
				nextInt = rd.nextInt((int)(a - 1- b));
				return temp + b + nextInt;
			}
			else if(b > a){
				temp = rd.nextDouble();
				nextInt = rd.nextInt((int)(b - 1-a));
				double sum = temp + a + nextInt;
				if(sum > b){
					System.out.println(temp + " " + nextInt + " " + a);
				}
				return sum;
			}
			else{
				return 0;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return temp + a;
	}
	
	public static void main(String[] args) {
		System.out.println(roundRandom(1, 3));
	}
}
