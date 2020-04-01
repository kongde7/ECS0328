package com.aliyun.ecs;

import java.util.ArrayList;

//Log是每天需要CPU数量的记录
//和Resource不同，它还包括了断供的情况
public class Log
{
	String date;
	
	int cpuN1Today;
	int cpuN2Today;
	int cpuN3Today;
	
	int cpuN1Stop;
	int cpuN2Stop;
	int cpuN3Stop;
	
	//统计各型号断供的CPU数量，和今天本来需要的CPU数量，便于之后报备
	public void Count( ArrayList<VM> vmList, ArrayList<VM> vmListStop, Times times, Resource res )
	{
		int cpuN1OK=0, cpuN2OK=0, cpuN3OK=0;
		int i;
		VM vm = null;
		Table table = new Table();
		String date = times.getDate();
		
		//遍历断供表，统计今日断供的CPU数量
		for ( i = 0; i < vmListStop.size(); i++ )
		{
			vm = vmListStop.get(i);
			if( date.contentEquals( vm.createDate ) )
			{
				if( vm.vmType.contentEquals(table.nameC1_l) || vm.vmType.contentEquals(table.nameC1_xl) || vm.vmType.contentEquals(table.nameC1_2xl) )
				{
					//C系列统一记为N1型号
					cpuN1Stop = cpuN1Stop + vm.cpu;
				}
				else if( vm.vmType.contentEquals(table.nameG1_l) || vm.vmType.contentEquals(table.nameG1_xl) || vm.vmType.contentEquals(table.nameG1_2xl) )
				{
					//G系列统一记为N2型号
					cpuN2Stop = cpuN2Stop + vm.cpu;
				}
				else
				{
					//R系列统一记为N3型号
					cpuN3Stop = cpuN3Stop + vm.cpu;
				}
			}
		}
		
		//遍历VM分配表
		for ( i = 0; i < vmList.size(); i++ )
		{
			vm = vmList.get(i);
			if( vm.ncId != 0 )
			{
				if( date.contentEquals( vm.createDate ) )
				{
					if( vm.vmType.contentEquals(table.nameC1_l) || vm.vmType.contentEquals(table.nameC1_xl) || vm.vmType.contentEquals(table.nameC1_2xl) )
					{
						//C系列统一记为N1型号
						cpuN1OK = cpuN1OK + vm.cpu;
					}
					else if( vm.vmType.contentEquals(table.nameG1_l) || vm.vmType.contentEquals(table.nameG1_xl) || vm.vmType.contentEquals(table.nameG1_2xl) )
					{
						//G系列统一记为N2型号
						cpuN2OK = cpuN2OK + vm.cpu;
					}
					else
					{
						//R系列统一记为N3型号
						cpuN3OK = cpuN3OK + vm.cpu;
					}
				}
			}
		}
		
		//今天需要的CPU数=断供的CPU数+已分配的CPU数
		cpuN1Today = cpuN1Stop + cpuN1OK;
		cpuN2Today = cpuN2Stop + cpuN2OK;
		cpuN3Today = cpuN3Stop + cpuN3OK;
		this.date = times.getDate();
	}
}
