package com.aliyun.ecs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Elastic
{
	public static void main( String[] args ) throws IOException
	{
		int max;
		int reNC=0;
		int newN1=0, newN2=0, newN3=0;
		int logN1, logN2, logN3;
		int a, b, c;
		int i = 1;
		int k=1;
		int flag=0;
		int temp=1, test=0;
		String fileName = "input_vm" + File.separator + "input_vm_" + String.valueOf( k ) + ".csv";
		VM vm = new VM();
		NC nc = new NC();
		Times times = new Times();
		Table table = new Table();
		Price price = new Price();
		Resource res = new Resource();
		String date;
		Log log = null;
		
		ArrayList<NC> ncList = new ArrayList<>();
		ArrayList<NC> ncListNew = new ArrayList<>();
		ArrayList<VM> vmList = new ArrayList<>();
		ArrayList<VM> vmListA = new ArrayList<>();
		ArrayList<VM> vmListB = new ArrayList<>();
		ArrayList<VM> vmListC = new ArrayList<>();
		ArrayList<VM> vmListStop = new ArrayList<>();
		ArrayList<Log> logList = new ArrayList<>();
		
		//调优表测试
		Adjust adjust = new Adjust();
		adjust.Index();
		
		//把目录下旧输出文件去掉
		Files files = new Files();
		files.First();
		
		//读取第一行VM数据
		vm.ReadOne( i, fileName, times );
		
		//初始化当前时间
		times.setDate( vm.createDate );
		times.count = 1;
		
		//初始化NC台数，写入ncList表
		nc.Add( 50, 100, 10, ncList, times, res );
		price.OneCost( 50, 100, 10 );
		
		//构建vmList表，朝里顺序写入
		System.out.println( "开始遍历input_vm目录下的csv文件，这个过程大概需要5分钟..." );
		while( true )
		{
			date = times.getDate();
			if( temp!=1 || test==0 )
			{
				vmList.addAll( vmListA );
				vmListA.clear();
				vmListB.clear();
				System.out.println( "=======================================================================" );
				System.out.println( "正在计算" + times.getDate() + "数据..." );
				test = 1;
			}
			for( i=temp; i<10001; i++ )
			{
				//判断是不是第0秒，是的话使能物理机
				if( times.getTime().contentEquals( "00:00:00" ) )
				{
					reNC = reNC - nc.Enable( ncList, ncListNew, times, res, price );
					//System.out.println( "NC使能" + nc.Enable( ncList, ncListNew, times, res, price ) + "QQQ" );
				}
				
				times.NextSec();
				
				//存入一行VM数据
				vm = new VM();
				//flag=1表示一个表示当天读完了，要换读下一天了
				if( vm.ReadOne( i, fileName, times ) == 0 )
				{
					temp = i;
					flag=1;
					break;
				}
				else flag=0;
				
				//存表之前先分配
				if( vm.Assign( ncList, res, price ) == 0 )
				{
					//如果返回0表示物理机已满，填写断供表
					vmListStop.add(vm);
				}
				else
				{
					//如果可以正常分配，资源表也要同步扣除资源数字
					res.Assign( vm, table );
				}
				//把vm对象添加到表中，稍后输出csv文件
				vmList.add( vm );
			}
			
			if( i>=10001 && k>20 || i<10001 )
			{
				//flag=1表示当天结束，写当天NC流水和VM流水
				if( i>=10001 && k>20 || flag==1 )
				{
					nc.Write( "nc.csv", ncList, times );
					//遍历vm表，只写当天创建的行
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
					
					//全新加入流程，报备新版
					logN1 = 0;
					logN2 = 0;
					logN3 = 0;
					log = new Log();
					log.Count( vmList, vmListStop, times, res );
					logList.add( log );
					max = 0;
					for ( i = 0; i < logList.size(); i++ )
					{
						log = logList.get(i);
						if( log.cpuN1Today + log.cpuN2Today + log.cpuN3Today > max )
							max = log.cpuN1Today + log.cpuN2Today + log.cpuN3Today;
						logN1 = logN1 + log.cpuN1Today;
						logN2 = logN2 + log.cpuN2Today;
						logN3 = logN3 + log.cpuN3Today;
						if( times.getDate().contentEquals( log.date ) )
						{
							for ( i = 0; i < logList.size(); i++ )
							{
								log = logList.get(i);
								if( log.cpuN1Today + log.cpuN2Today + log.cpuN3Today == max )
								{
									logN1 = logN1 - log.cpuN1Today;
									logN2 = logN2 - log.cpuN2Today;
									logN3 = logN3 - log.cpuN3Today;
									break;
								}
							}
							//把CPU需求数除以64转换成物理机需求数，其中N1最多能分配64核，N2最多能分配96核，N3最多能分配64核
							logN1 = logN1 / ( i + 1 ) / 64;
							logN2 = logN2 / ( i + 1 ) / 96;
							logN3 = logN3 / ( i + 1 ) / 64;
							break;
						}
					}
					if( logN1 > res.numN1 + reNC )
					{
						newN1 = logN1 - res.numN1 - reNC;
					}
					if( logN2 > res.numN2 + reNC )
					{
						newN2 = logN2 - res.numN2 - reNC;
					}
					if( logN3 > res.numN3 + reNC )
					{
						newN3 = logN3 - res.numN3 - reNC;
					}
					reNC = newN1 + newN2 + newN3;
					//System.out.println( "N1、N2、N3需求量：" + logN1 +" "+ logN2 +" "+ logN3 );//到时候删
					//System.out.println( "N1、N2、N3报备：" + newN1 +" "+ newN2 +" "+ newN3 );//到时候删
					
					//当天23点59分，计算收益
					price.Income( table, res );
					
					//调优流程,来不及了就不调优了
					//adjust.Optimize( vmListA, ncList, res, times );
					
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
					/*
					System.out.println( "N1物理机CPU总数：" + res.totalCpuN1 + "个" );//到时候删
					System.out.println( "N2物理机CPU总数：" + res.totalCpuN2 + "个" );//到时候删
					System.out.println( "N3物理机CPU总数：" + res.totalCpuN3 + "个" );//到时候删
					System.out.println( "N1物理机使用到CPU：" + res.usedCpuN1 + "个" );//到时候删
					System.out.println( "N2物理机使用到CPU：" + res.usedCpuN2 + "个" );//到时候删
					System.out.println( "N3物理机使用到CPU：" + res.usedCpuN3 + "个" );//到时候删
					*/
					
					//这里存在一个误区，剩余物理机的CPU多并不能代表可用的多，因为内存先满
					
					//如果够，不补货，不够了才补货
					if( res.usedCpuN1 / times.count * 10 > res.numN1 * 64 - res.usedCpuN1 )
					//if( res.usedCpuN1 / times.count * 10 > res.totalCpuN1 - res.usedCpuN1 || 0.5 * res.totalCpuN1 < res.usedCpuN1 )
					{
						a = res.usedCpuN1 / times.count / 64;
					}
					else a = 0;
					if( res.usedCpuN2 / times.count * 10 > res.numN2 * 96 - res.usedCpuN2 )
					//if( res.usedCpuN2 / times.count * 10 > res.totalCpuN2 - res.usedCpuN2 || 0.5 * res.totalCpuN2 < res.usedCpuN2 )
					{
						b = res.usedCpuN2 / times.count / 96;
					}
					else b = 0;
					if( res.usedCpuN3 / times.count * 10 > res.numN3 * 64 - res.usedCpuN3 )
					//if( res.usedCpuN3 / times.count * 10 > res.totalCpuN3 - res.usedCpuN3 || 0.5 * res.totalCpuN3 < res.usedCpuN3 )
					{
						c = res.usedCpuN3 / times.count / 64;
					}
					else c = 0;
					/*
					System.out.println( "当天用了N1物理机：" + res.usedCpuN1/64 + "台" );//到时候删
					System.out.println( "当天用了N2物理机：" + res.usedCpuN2/96 + "台" );//到时候删
					System.out.println( "当天用了N3物理机：" + res.usedCpuN3/64 + "台" );//到时候删
					System.out.println( "当天剩余N1物理机：" + (res.numN1 * 64 - res.usedCpuN1)/64 + "台" );//到时候删
					System.out.println( "当天剩余N2物理机：" + (res.numN2 * 96 - res.usedCpuN2)/96 + "台" );//到时候删
					System.out.println( "当天剩余N3物理机：" + (res.numN3 * 64 - res.usedCpuN3)/64 + "台" );//到时候删
					
					if( a!=0 )
						System.out.println( "报备N1物理机：" + a + "台" );//到时候删
					if( b!=0 )
						System.out.println( "报备N2物理机：" + b + "台" );//到时候删
					if( c!=0 )
						System.out.println( "报备N3物理机：" + c + "台" );//到时候删
					*/
					
					//报备测试
					
					nc.Report( 0, newN1+newN2+newN3, 0, ncListNew, times, price );
					newN1 = 0;
					newN2 = 0;
					newN3 = 0;
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
			
			//该情况为日期跨文件
			else if( i>=10001 )
			{
				if( k<20 )
				{
					k++;
					fileName = "input_vm" + File.separator + "input_vm_" + String.valueOf( k ) + ".csv";
					temp = 1;
				}
				else
				{
					System.out.println( "总收益：" + String.format( "%.2f", price.sumIncome ) + "元" );
					System.out.println( "总成本：" + String.format( "%.2f", price.sumCost ) + "元" );
					break;
				}
			}
		}
		
		System.out.println( "三个月收益率：" + String.format( "%.2f", ( ( price.sumIncome - price.sumCost ) / price.sumCost * 100) ) + "%" );
	}
}
