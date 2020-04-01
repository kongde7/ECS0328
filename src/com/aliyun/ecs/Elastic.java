package com.aliyun.ecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Elastic
{
	public static void main( String[] args ) throws IOException
	{
		int a, b, c;
		int i = 1;
		int j;
		int k=1;
		int flag=0;
		int temp=1;
		String fileName = "input_vm\\input_vm_" + String.valueOf( k ) + ".csv";
		VM vm = new VM();
		NC nc = new NC();
		Times times = new Times();
		Table table = new Table();
		Price price = new Price();
		Resource res = new Resource();
		String date;
		
		//调优表测试
		Adjust adjust = new Adjust();
		adjust.Index();
		
		//读取第一行vm数据
		vm.ReadOne( i, fileName, times );
		
		//初始化当前时间
		times.setDate(vm.createDate);
		times.count = 1;
		
		//初始化NC台数，写入ncList表
		ArrayList<NC> ncList = new ArrayList<>();
		ArrayList<NC> ncListNew = new ArrayList<>();
		
		nc.Add( 30, 50, 10, ncList, times, res );
		price.OneCost( 30, 50, 10 );
		
		//构建vmList表，朝里顺序写入
		ArrayList<VM> vmList = new ArrayList<>();
		ArrayList<VM> vmListA = new ArrayList<>();
		ArrayList<VM> vmListB = new ArrayList<>();
		ArrayList<VM> vmListC = new ArrayList<>();
		
		System.out.println( "开始遍历input_vm目录下的csv文件，这个过程大概需要5分钟..." );
		while( true )
		{
			date = times.getDate();
			if( temp!=1 )
			{
				vmList.addAll( vmListA );
				vmListA.clear();
				vmListB.clear();
				System.out.println( "正在计算" + times.getDate() + "数据..." );
			}
			for( i=temp; i<10001; i++ )
			{
				//判断是不是第0秒，是的话使能物理机
				if( times.getTime().contentEquals( "00:00:00" ) )
				{
					nc.Enable( ncList, ncListNew, times, res );
				}
				
				times.NextSec();
				
				//存入一行vm数据
				vm = new VM();
				if( vm.ReadOne( i, fileName, times ) == 0 )
				{
					temp = i;
					flag=1;
					break;
				}
				else flag=0;
				
				//存表之前先分配
				vm.Assign( ncList, res, price );
				
				//资源表也要同步写入
				res.Assign( vm, table );
				
				vmList.add( vm );
			}
			
			if( i<10001 )
			{
				if( flag==1 )
				{
					//当天结束，写当天NC流水和vm流水
					//vm.Write( vmList, times );
					nc.Write( "nc.csv", ncList, times );
					
					for ( i = 0; i < vmList.size(); i++ )
					{
						vm = vmList.get(i);
						if( date.contentEquals( vm.createDate ) )
						{
							vmListC.add(vm);
						}
					}
					vm.Write( vmListC, times );
					vmListC.clear();
					
					//当天23点59分，计算收益和成本
					price.Income( table, res );
					
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
					
					
					//调优流程,来不及了就不调优了
					//adjust.Optimize( vmListA, ncList, res, times );
					
					//计算要报备多少台NC
					res.usedCpuN1 = 0;
					res.usedCpuN2 = 0;
					res.usedCpuN3 = 0;
					res.usedMemoryN1 = 0;
					res.usedMemoryN2 = 0;
					res.usedMemoryN3 = 0;
					for ( i = 0; i < ncList.size(); i++ )
					{
						nc = ncList.get(i);
						if( nc.machineType.contentEquals( table.nameN1 ) )
						{
							res.usedCpuN1 = res.usedCpuN1 + nc.usedCpu;
							res.usedMemoryN1 = res.usedMemoryN1 + nc.usedMemory;
						}
						else if( nc.machineType.contentEquals( table.nameN2 ) )
						{
							res.usedCpuN2 = res.usedCpuN2 + nc.usedCpu;
							res.usedMemoryN2 = res.usedMemoryN2 + nc.usedMemory;
						}
						else if( nc.machineType.contentEquals( table.nameN3 ) )
						{
							res.usedCpuN3 = res.usedCpuN3 + nc.usedCpu;
							res.usedMemoryN3 = res.usedMemoryN3 + nc.usedMemory;
						}
					}
					
					//如果够，不补货，不够了才补货
					if( res.usedCpuN1 / times.count * 10 > res.totalCpuN1 - res.usedCpuN1 )
					{
						a = res.usedCpuN1 / times.count / 64;
					}
					else a = 0;
					if( res.usedCpuN2 / times.count * 10 > res.totalCpuN2 - res.usedCpuN2 )
					{
						b = res.usedCpuN2 / times.count / 64;
					}
					else b = 0;
					if( res.usedCpuN3 / times.count * 10 > res.totalCpuN3 - res.usedCpuN3 )
					{
						c = res.usedCpuN3 / times.count / 64;
					}
					else c = 0;
					
						
					
					//报备测试
					nc.Report( a, b, c, ncListNew, times, price );
					nc.Write( "new_nc.csv", ncListNew, times );
					//报备成本
					price.OneCost( 0, 0, 0 );
					price.Cost( table, res );
					
					times.NextDay();
					flag = 0;
					price.oneCost=0;
					price.dayCost=0;
					price.dayCost=0;
					price.buhuo = 0;
					price.duangong =0;
				}
			}
			

			
			else if( i>=10001 )
			{
				if( k<20 )
				{
					k++;
					fileName = "input_vm\\input_vm_" + String.valueOf( k ) + ".csv";
					temp = 1;
				}
				else
				{
					//以下块为重复，因为是最后一天，单独加一个循环
					//当天结束，写当天NC流水和vm流水
					//vm.Write( vmList, times );
					nc.Write( "nc.csv", ncList, times );
					
					for ( i = 0; i < vmList.size(); i++ )
					{
						vm = vmList.get(i);
						if( date.contentEquals( vm.createDate ) )
						{
							vmListC.add(vm);
						}
					}
					vm.Write( vmListC, times );
					vmListC.clear();
					
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
					price.buhuo =0;
					price.duangong=0;
					//本块跟上面重复
					
					break;
				}
			}
		}
		
		System.out.println( "三个月收益率：" + (( price.sumIncome - price.sumCost ) / price.sumCost * 100) + "%" );//到时候删
	}
}
