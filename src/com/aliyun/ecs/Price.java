package com.aliyun.ecs;

//Price����3����������ͳɱ��ķ�����
//1��Income�������������������
//2��Cost����������ÿ�ջ��ѣ��������ÿ��ά���ĵ绰
//3��OneCost����������һ���Ի��ѣ������������ѡ������˷ѡ��Ϲ���Ǯ

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
	
	//Income�������������������
	public double Income( Table table, Resource res )
	{
		//���۳��Զ�Ӧ����VM��������24Сʱ
		double sumC1 = table.perC1_l * res.numC1_l + table.perC1_xl * res.numC1_xl + table.perC1_2xl * res.numC1_2xl;
		double sumG1 = table.perG1_l * res.numG1_l + table.perG1_xl * res.numG1_xl + table.perG1_2xl * res.numG1_2xl;
		double sumR1 = table.perR1_l * res.numR1_l + table.perR1_xl * res.numR1_xl + table.perR1_2xl * res.numR1_2xl;
		
		dayIncome = ( sumC1 + sumG1 + sumR1 ) * 24;
		sumIncome = sumIncome + dayIncome;
		System.out.println( "����������㣺" + String.format( "%.2f", dayIncome ) );
		return dayIncome;
	}
	
	//Cost����������ÿ�ջ��ѣ��������ÿ��ά���ĵ绰
	public double Cost( Table table, Resource res )
	{
		double sy;
		double xn = 3.6;
		
		double sumN1 = xn * res.totalCpuN1;
		double sumN2 = xn * res.totalCpuN2;
		double sumN3 = xn * res.totalCpuN3;
		
		dayCost = sumN1 + sumN2 + sumN3;
		sumCost = sumCost + dayCost;
		System.out.println( "�����������ѳɱ���" + String.format( "%.2f", dayCost ) + "     �����ܳɱ�Ϊ��" + String.format( "%.2f", (dayCost+oneCost) ) );
		dayCost = dayCost + oneCost;
		
		sy = ( dayIncome - dayCost ) / dayCost * 100;
		
		System.out.println( "���������ʣ�" + String.format( "%.2f", sy ) + "%" );
		System.out.println( "�ϼƳɱ���" + sumCost + "     �ϼ����棺"+sumIncome );
		
		return dayCost;
	}
	
	//OneCost����������һ���Ի��ѣ������������ѡ������˷ѡ��Ϲ���Ǯ
	public double OneCost( int numN1, int numN2, int numN3 )
	{
		double perN1 = 20000;
		double perN2 = 23500;
		double perN3 = 30000;
		
		double sumN1 = perN1 * numN1;
		double sumN2 = perN2 * numN2;
		double sumN3 = perN3 * numN3;
		
		//�в������룬��ʾ�¹������������ֻ�Ǵ�ӡ���֣���չʾ
		if( numN1!=0 || numN2!=0 || numN3!=0 )
		{
			oneCost = sumN1 + sumN2 + sumN3;
			sumCost = sumCost + oneCost;
		}
		else
		{
			System.out.println( "�������������ɱ���" + oneCost + "     ���챨���ɱ���" + buhuo + "     ����Ϲ��ɱ���" + duangong );
			oneCost = oneCost + buhuo + duangong;
		}
		return oneCost;
	}
}
