package com.aliyun.ecs;

//Times�ǣ����ڰѱ��ʱ��ͳ���ʱ�����ȶԣ��ָ�ÿһ��
//����Ӧ�ĳ�static
//TenDay������ʮ�������ڣ����������ʱ�õ�
public class Times
{
	int year;
	int month;
	int day;
	int hour;
	int min;
	int sec;
	static int count;
	
	//����ʮ�������ڣ����������ʱ�õ�
	public String TenDay()
	{
		int howMany[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int years = year;
		int months = month;
		int days = day;
		int i;
		
		//�����ж�
        if( year%4==0 && year%100!=0 || year%400==0 )
        	howMany[1] = 29;
		
        //�ս�λ�ж�����ֹ����32��
		for( i=1; i<11; i++ )
		{
			days++;
			if( days>howMany[month-1] )
			{
				days = 1;
				months++;
			}
			if( months>12 )
			{
				months = 1;
				years++;
			}
		}
		return String.format( "%04d-%02d-%02d", years, months, days );
	}
	
	//��һ��
	public void NextDay()
	{
		int howMany[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		count++;
		day++;
		hour = 0;
		min = 0;
		sec = 0;
        if( year%4==0 && year%100!=0 || year%400==0 )
        	howMany[1] = 29;
		if( day>howMany[month-1] )
		{
			day = 1;
			month++;
		}
		if( month>12 )
		{
			month = 1;
			year++;
		}
	}
	
	public void NextSec()
	{
		sec++;
		if( sec>59 )
		{
			sec = 0;
			min++;
		}
		if( min>59 )
		{
			min = 0;
			hour++;
		}
		if( hour>23 )
		{
			hour = 0;
			min = 0;
			sec = 0;
		}
		//���������λ����ΪĿǰ����û����ô��
	}
	
	public void setDate( String date )
	{
		String cut[] = new String[3];
		cut = date.split( "-" );
		year = Integer.parseInt( cut[0] );
		month = Integer.parseInt( cut[1] );
		day = Integer.parseInt( cut[2] );
		System.out.println( "��ʼ���ɹ�����ǰ���ڣ�" + date );//��ʱ��ɾ
	}
	
	public String getDate()
	{
		String date = String.format( "%04d-%02d-%02d", year, month, day );
		return date;
	}
	
	public String getTime()
	{
		String time = String.format( "%02d:%02d:%02d", hour, min, sec );
		return time;
	}
	
	public int getNum()
	{
		int num = hour*60*60 + min*60 + sec;
		return num;
	}
}
