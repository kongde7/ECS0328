package com.aliyun.ecs;

public class Times
{
	int year;
	int month;
	int day;
	int hour;
	int min;
	int sec;
	
	public void NextDay()
	{
		int howMany[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
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
		//不考虑天进位，因为目前数据没有那么多
	}
	
	public void setDate( String date )
	{
		String cut[] = new String[3];
		cut = date.split( "-" );
		year = Integer.parseInt( cut[0] );
		month = Integer.parseInt( cut[1] );
		day = Integer.parseInt( cut[2] );
		System.out.println( "times方法创建："+year +"年"+month+"月"+day );//到时候删
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
