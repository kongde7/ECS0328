package com.aliyun.ecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Elastic
{
	public static void main( String[] args ) throws IOException
	{
		int i = 1;
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
		String date;
		
		//调优表测试
		Adjust adjust = new Adjust();
		adjust.Index();
		
		//读取第一行vm数据
		vm.ReadOne( i, fileName, times, test );
		
		//初始化当前时间
		times.setDate(vm.createDate);
		
		//初始化NC台数，写入ncList表
		ArrayList<NC> ncList = new ArrayList<>();
		ArrayList<NC> ncListNew = new ArrayList<>();
		
		nc.Add( 4, 5, 6, ncList, times, res );
		price.OneCost( 4, 5, 6 );
		
		//构建vmList表，朝里顺序写入
		ArrayList<VM> vmList = new ArrayList<>();
		ArrayList<VM> vmListA = new ArrayList<>();
		ArrayList<VM> vmListB = new ArrayList<>();
		ArrayList<VM> vmListC = new ArrayList<>();
		
		System.out.println( "开始遍历input_vm目录下的csv文件，这个过程大概需要3分钟..." );
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
					nc.Enable( ncList, ncListNew, times );
				}
				
				//System.out.println( i );
				times.NextSec();
				//读入之前先看当前秒有没有释放事件
				//System.out.println( i );
				//if( rel.sec[i] == 1 && rel.timing[i].contentEquals( times.getDate() ) )
				//{
				//	res.releaseOne( i, rel, vmList, ncList, table, times );
				//}
				
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
			
			if( i<10001 )
			{
				if( flag==1 )
				{
					//当天结束，写当天NC流水和vm流水
					//vm.Write( vmList, times );
					nc.Write( "nc1.csv", ncList, times );
					
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
					

					//报备测试
					nc.Report( 1, 2, 3, ncListNew, times );
					nc.Write( "new_nc1.csv", ncListNew, times );
					
					
					times.NextDay();
					flag = 0;
					price.oneCost=0;
					price.dayCost=0;
					price.dayCost=0;
				}
			}
			

			
			else if( i>=10001 )
			{
				if( k<20 )
				{
					k++;
					fileName = "input_vm_" + String.valueOf( k ) + ".csv";
					temp = 1;
				}
				else
				{
					//以下块为重复
					//当天结束，写当天NC流水和vm流水
					//vm.Write( vmList, times );
					nc.Write( "nc1.csv", ncList, times );
					
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
					

					//报备测试
					nc.Report( 1, 2, 3, ncListNew, times );
					nc.Write( "new_nc1.csv", ncListNew, times );
					
					
					times.NextDay();
					flag = 0;
					price.oneCost=0;
					price.dayCost=0;
					price.dayCost=0;
					//本块跟上面重复
					
					break;
				}
			}
		}
		
		System.out.println( "三个月收益率：" + (( price.sumIncome - price.sumCost ) / price.sumCost * 100) + "%" );//到时候删
	}
}
