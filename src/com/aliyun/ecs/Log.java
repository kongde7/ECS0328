package com.aliyun.ecs;

import java.util.ArrayList;

//Log��ÿ����ҪCPU�����ļ�¼
//��Resource��ͬ�����������˶Ϲ������
public class Log
{
	String date;
	
	int cpuN1Today;
	int cpuN2Today;
	int cpuN3Today;
	
	int cpuN1Stop;
	int cpuN2Stop;
	int cpuN3Stop;
	
	//ͳ�Ƹ��ͺŶϹ���CPU�������ͽ��챾����Ҫ��CPU����������֮�󱨱�
	public void Count( ArrayList<VM> vmList, ArrayList<VM> vmListStop, Times times, Resource res )
	{
		int cpuN1OK=0, cpuN2OK=0, cpuN3OK=0;
		int i;
		VM vm = null;
		Table table = new Table();
		String date = times.getDate();
		
		//�����Ϲ���ͳ�ƽ��նϹ���CPU����
		for ( i = 0; i < vmListStop.size(); i++ )
		{
			vm = vmListStop.get(i);
			if( date.contentEquals( vm.createDate ) )
			{
				if( vm.vmType.contentEquals(table.nameC1_l) || vm.vmType.contentEquals(table.nameC1_xl) || vm.vmType.contentEquals(table.nameC1_2xl) )
				{
					//Cϵ��ͳһ��ΪN1�ͺ�
					cpuN1Stop = cpuN1Stop + vm.cpu;
				}
				else if( vm.vmType.contentEquals(table.nameG1_l) || vm.vmType.contentEquals(table.nameG1_xl) || vm.vmType.contentEquals(table.nameG1_2xl) )
				{
					//Gϵ��ͳһ��ΪN2�ͺ�
					cpuN2Stop = cpuN2Stop + vm.cpu;
				}
				else
				{
					//Rϵ��ͳһ��ΪN3�ͺ�
					cpuN3Stop = cpuN3Stop + vm.cpu;
				}
			}
		}
		
		//����VM�����
		for ( i = 0; i < vmList.size(); i++ )
		{
			vm = vmList.get(i);
			if( vm.ncId != 0 )
			{
				if( date.contentEquals( vm.createDate ) )
				{
					if( vm.vmType.contentEquals(table.nameC1_l) || vm.vmType.contentEquals(table.nameC1_xl) || vm.vmType.contentEquals(table.nameC1_2xl) )
					{
						//Cϵ��ͳһ��ΪN1�ͺ�
						cpuN1OK = cpuN1OK + vm.cpu;
					}
					else if( vm.vmType.contentEquals(table.nameG1_l) || vm.vmType.contentEquals(table.nameG1_xl) || vm.vmType.contentEquals(table.nameG1_2xl) )
					{
						//Gϵ��ͳһ��ΪN2�ͺ�
						cpuN2OK = cpuN2OK + vm.cpu;
					}
					else
					{
						//Rϵ��ͳһ��ΪN3�ͺ�
						cpuN3OK = cpuN3OK + vm.cpu;
					}
				}
			}
		}
		
		//������Ҫ��CPU��=�Ϲ���CPU��+�ѷ����CPU��
		cpuN1Today = cpuN1Stop + cpuN1OK;
		cpuN2Today = cpuN2Stop + cpuN2OK;
		cpuN3Today = cpuN3Stop + cpuN3OK;
		this.date = times.getDate();
	}
}
