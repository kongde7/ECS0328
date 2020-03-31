package com.aliyun.ecs;

public class Price
{
	double oneCost;
	double sumIncome;
	double dayIncome;
	double sumCost;
	double dayCost;
	String date;
	
	public double Income( Table table, Resource res )
	{
		//单价乘以数量乘以24小时
		double sumC1 = table.perC1_l * res.totalC1_l + table.perC1_xl * res.totalC1_xl + table.perC1_2xl * res.totalC1_2xl;
		double sumG1 = table.perG1_l * res.totalG1_l + table.perG1_xl * res.totalG1_xl + table.perG1_2xl * res.totalG1_2xl;
		double sumR1 = table.perR1_l * res.totalR1_l + table.perR1_xl * res.totalR1_xl + table.perR1_2xl * res.totalR1_2xl;
		
		dayIncome = ( sumC1 + sumG1 + sumR1 ) * 24;
		sumIncome = sumIncome + dayIncome;
		System.out.println( "当天收益计算："+dayIncome);//到时候删
		return dayIncome;
	}
	
	public double Cost( Table table, Resource res )
	{
		double sy;
		double xn = 3.6;
		
		double sumN1 = xn * res.totalCpuN1;
		double sumN2 = xn * res.totalCpuN2;
		double sumN3 = xn * res.totalCpuN3;
		
		dayCost = sumN1 + sumN2 + sumN3 + oneCost;
		sumCost = sumCost + dayCost;
		System.out.println( "当天成本计算："+ dayCost );//到时候删
		
		sy = ( dayIncome - dayCost ) / dayCost * 100;
		
		System.out.println( "当天收益率：" + sy + "%" );//到时候删
		
		return dayCost;
	}
	
	public double OneCost( int numN1, int numN2, int numN3 )
	{
		double perN1 = 20000;
		double perN2 = 23500;
		double perN3 = 30000;
		
		double sumN1 = perN1 * numN1;
		double sumN2 = perN2 * numN2;
		double sumN3 = perN3 * numN3;
		
		oneCost = sumN1 + sumN2 + sumN3;
		sumCost = sumCost + oneCost;
		//System.out.println( "当天一次性成本计算："+ oneCost );//到时候删
		return oneCost;
	}
}
