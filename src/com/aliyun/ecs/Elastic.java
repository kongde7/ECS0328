package com.aliyun.ecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Elastic
{
	public static void main( String[] args ) throws IOException
	{
		int i = 1;
		int j;
		int k=1;
		int flag=0;
		int temp=1;
		String fileName = "input_vm_" + String.valueOf( k ) + ".csv";
		VM vm = new VM();
		NC nc = new NC();
		Times times = new Times();
		Table table = new Table();
		Price price = new Price();
		Resource res = new Resource();
		Release test = new Release();
		Release rel = new Release();
		
		//读取第一行vm数据
		vm.ReadOne( i, fileName, times, test );
		
		//初始化当前时间
		times.setDate(vm.createDate);
		
		//初始化NC台数，写入ncList表
		ArrayList<NC> ncList = new ArrayList<>();
		nc.Enable( 50, 100, 5, ncList, times, res );
		price.OneCost( 50, 100, 5 );
		
		//构建vmList表，朝里顺序写入
		ArrayList<VM> vmList = new ArrayList<>();
		ArrayList<VM> vmListA = new ArrayList<>();
		ArrayList<VM> vmListB = new ArrayList<>();
		
		while( true )
		{
			String date = times.getDate();
			vmList.addAll( vmListA );
			vmListA.clear();
			vmListB.clear();
			System.out.println( "正在计算" + times.getDate() + "数据" );
			for( i=temp; i<10001; i++ )
			{
				//System.out.println( i );
				times.NextSec();
				//读入之前先看当前秒有没有释放事件
				//System.out.println( i );
				if( rel.sec[i] == 1 && rel.timing[i].contentEquals( times.getDate() ) )
				{
					res.releaseOne( i, rel, vmList, ncList, table, times );
				}
				
				//存入一行vm数据
				vm = new VM();
				if( vm.ReadOne( i, fileName, times, rel ) == 0 )
				{
					temp = i;
					flag=1;
					break;
				}
				else flag=0;
				//System.out.println( "现在获取的ID：" + vm.ReadOne( i, fileName, times, rel) );
				//存表之前先分配
				vm.Assign( ncList, res );
				
				//资源表也要同步写入
				res.Assign( vm, table );
				
				vmList.add( vm );
			}
			
			if( flag==1 )
			{
				//当天结束，写当天NC流水和vm流水
				vm.Write( vmList, times );
				nc.Write( ncList, times );
				
				//当天23点59分，计算收益和成本
				price.Income( table, res );
				price.Cost( table, res );
				//System.out.println( "main成本汇总：" +  price.sumCost );
				
				//当天23点59分，释放当天要释放的资源，A是存量，B是释放
				for ( i = 0; i < vmList.size(); i++ )
				{
					vm = vmList.get(i);
					if( date.contentEquals( vm.releaseDate ) )
					{
						vm.status = "release";
						vmListB.add(vm);
					}
					else
					{
						vmListA.add(vm);
					}
				}
				vmList.clear();
				res.release( vmListB, ncList, table );
				vm.Write( vmListB, times );
				//nc.Write( ncList, times );//试着打印一遍
				times.NextDay();
				flag = 0;
				price.oneCost=0;
				price.dayCost=0;
				price.dayCost=0;
			}
			
			if( i>=10001 )
			{
				if( k<20 )
				{
					k++;
					fileName = "input_vm_" + String.valueOf( k ) + ".csv";
					i = 1;
				}
				else break;
			}
		}
		
		System.out.println( "三个月收益率：" + (( price.sumIncome - price.sumCost ) / price.sumCost * 100) + "%" );//到时候删
	}
}
