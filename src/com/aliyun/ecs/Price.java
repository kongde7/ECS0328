package com.aliyun.ecs;

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
	//
	public double Income( Table table, Resource res )
	{
		//���۳�����������24Сʱ
		double sumC1 = table.perC1_l * res.numC1_l + table.perC1_xl * res.numC1_xl + table.perC1_2xl * res.numC1_2xl;
		double sumG1 = table.perG1_l * res.numG1_l + table.perG1_xl * res.numG1_xl + table.perG1_2xl * res.numG1_2xl;
		double sumR1 = table.perR1_l * res.numR1_l + table.perR1_xl * res.numR1_xl + table.perR1_2xl * res.numR1_2xl;
		
		dayIncome = ( sumC1 + sumG1 + sumR1 ) * 24;
		sumIncome = sumIncome + dayIncome;
		System.out.println( "����������㣺"+dayIncome);//��ʱ��ɾ
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
		System.out.println( "����ɱ����㣺"+ dayCost );//��ʱ��ɾ
		
		sy = ( dayIncome - dayCost ) / dayCost * 100;
		
		System.out.println( "���������ʣ�" + sy + "%" );//��ʱ��ɾ
		
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
		
		oneCost = sumN1 + sumN2 + sumN3+buhuo+duangong;
		sumCost = sumCost + oneCost;
		//System.out.println( "����һ���Գɱ����㣺"+ oneCost );//��ʱ��ɾ
		return oneCost;
	}
}
