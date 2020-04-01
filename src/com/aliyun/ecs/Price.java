package com.aliyun.ecs;

//Price包含3个计算收益和成本的方法：
//1、Income方法：计算虚拟机收益
//2、Cost方法：计算每日花费，如物理机每日维护的电话
//3、OneCost方法：计算一次性花费：如物理机购买费、补货运费、断供的钱

public class Price
{
	double buhuo;
	double duangong;
	double oneCost;
	double sumIncome;
	double dayIncome;
	double sumCost;
	double dayCost;
	String date;
	
	//Income方法：计算虚拟机收益
	public double Income( Table table, Resource res )
	{
		//单价乘以对应类型VM数量乘以24小时
		double sumC1 = table.perC1_l * res.numC1_l + table.perC1_xl * res.numC1_xl + table.perC1_2xl * res.numC1_2xl;
		double sumG1 = table.perG1_l * res.numG1_l + table.perG1_xl * res.numG1_xl + table.perG1_2xl * res.numG1_2xl;
		double sumR1 = table.perR1_l * res.numR1_l + table.perR1_xl * res.numR1_xl + table.perR1_2xl * res.numR1_2xl;
		
		dayIncome = ( sumC1 + sumG1 + sumR1 ) * 24;
		sumIncome = sumIncome + dayIncome;
		System.out.println( "当天收益计算：" + String.format( "%.2f", dayIncome ) );
		return dayIncome;
	}
	
	//Cost方法：计算每日花费，如物理机每日维护的电话
	public double Cost( Table table, Resource res )
	{
		double sy;
		double xn = 3.6;
		
		double sumN1 = xn * res.totalCpuN1;
		double sumN2 = xn * res.totalCpuN2;
		double sumN3 = xn * res.totalCpuN3;
		
		dayCost = sumN1 + sumN2 + sumN3 + oneCost;
		sumCost = sumCost + dayCost;
		System.out.println( "当天物理机电费为：" + (dayCost-oneCost) );
		System.out.println( "当天总成本为：" + String.format( "%.2f", dayCost ) );
		
		sy = ( dayIncome - dayCost ) / dayCost * 100;
		
		System.out.println( "当天收益率：" + String.format( "%.2f", sy ) + "%" );
		
		return dayCost;
	}
	
	//OneCost方法：计算一次性花费：如物理机购买费、补货运费、断供的钱
	public double OneCost( int numN1, int numN2, int numN3 )
	{
		double perN1 = 20000;
		double perN2 = 23500;
		double perN3 = 30000;
		
		double sumN1 = perN1 * numN1;
		double sumN2 = perN2 * numN2;
		double sumN3 = perN3 * numN3;
		
		oneCost = sumN1 + sumN2 + sumN3 + buhuo + duangong;
		sumCost = sumCost + oneCost;
		//System.out.println( "当天报备成本计算："+ buhuo );//到时候删
		//System.out.println( "当天断供成本计算："+ duangong );//到时候删
		//System.out.println( "当天一次性成本计算："+ oneCost );//到时候删
		return oneCost;
	}
}
