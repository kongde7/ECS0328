package com.aliyun.ecs;

import java.io.IOException;
import java.util.ArrayList;

//Resource��������Դ���ñ�ȫ�ֹ���
public class Resource
{
	int numC1_l;
	int numC1_xl;
	int numC1_2xl;
	int numG1_l;
	int numG1_xl;
	int numG1_2xl;
	int numR1_l;
	int numR1_xl;
	int numR1_2xl;
	
	int totalC1_l;
	int totalC1_xl;
	int totalC1_2xl;
	int totalG1_l;
	int totalG1_xl;
	int totalG1_2xl;
	int totalR1_l;
	int totalR1_xl;
	int totalR1_2xl;
	
	int numN1;
	int numN2;
	int numN3;
	
	int totalCpu;
	int totalCpuN1;
	int totalCpuN2;
	int totalCpuN3;
	
	//�ÿ�����������ж��Ƿ񲹻���������˶Ϲ��������˲�������
	int usedCpu;
	int usedCpuN1;
	int usedCpuN2;
	int usedCpuN3;
	
	int totalMemory;
	int totalMemoryN1;
	int totalMemoryN2;
	int totalMemoryN3;
	
	//�ÿ�����������ж��Ƿ񲹻���������˶Ϲ��������˲�������
	int usedMemory;
	int usedMemoryN1;
	int usedMemoryN2;
	int usedMemoryN3;
	
	public void Add( NC nc )
	{
		Table table = new Table();
		
		//��NC��Դ
		totalCpu = totalCpu + nc.totalCpu;
		totalMemory = totalMemory + nc.totalMemory;
		
		//��������NC��Դ
		if( nc.machineType.contentEquals( table.nameN1 ) )
		{
			numN1++;
			totalCpuN1 = totalCpuN1 + nc.totalCpu;
			totalMemoryN1 = totalMemoryN1 + nc.totalMemory;
		}
		else if( nc.machineType.contentEquals( table.nameN2 ) )
		{
			numN2++;
			totalCpuN2 = totalCpuN2 + nc.totalCpu;
			totalMemoryN2 = totalMemoryN2 + nc.totalMemory;
		}
		else
		{
			numN3++;
			totalCpuN3 = totalCpuN3 + nc.totalCpu;
			totalMemoryN3 = totalMemoryN3 + nc.totalMemory;
		}
	}
	
	public void Assign( VM vm, Table table )
	{
		//��ȷ����û�з���ɹ���û���䣨���������ʱ������д��Դ��
		if( vm.ncId!=0 )
		{
			//�����������Դ
			totalCpu = totalCpu - vm.cpu;
			totalMemory = totalMemory - vm.memory;
			
			//�������������
			if( vm.vmType.contentEquals( table.nameC1_l ) )
			{
				numC1_l++;
				totalC1_l = totalC1_l + table.cpuC1_l;
			}
			else if( vm.vmType.contentEquals( table.nameC1_xl ) )
			{
				numC1_xl++;
				totalC1_xl = totalC1_xl + table.cpuC1_xl;
			}
			else if( vm.vmType.contentEquals( table.nameC1_2xl ) )
			{
				numC1_2xl++;
				totalC1_2xl = totalC1_2xl + table.cpuC1_2xl;
				
			}
			else if( vm.vmType.contentEquals( table.nameG1_l ) )
			{
				numG1_l++;
				totalG1_l = totalG1_l + table.cpuG1_l;
			}
			else if( vm.vmType.contentEquals( table.nameG1_xl ) )
			{
				numG1_xl++;
				totalG1_xl = totalG1_xl + table.cpuG1_xl;
			}
			else if( vm.vmType.contentEquals( table.nameG1_2xl ) )
			{
				numG1_2xl++;
				totalG1_2xl = totalG1_2xl + table.cpuG1_2xl;
			}
			else if( vm.vmType.contentEquals( table.nameR1_l ) )
			{
				numR1_l++;
				totalR1_l = totalR1_l + table.cpuR1_l;
			}
			else if( vm.vmType.contentEquals( table.nameR1_xl ) )
			{
				numR1_xl++;
				totalR1_xl = totalR1_xl + table.cpuR1_xl;
			}
			else if( vm.vmType.contentEquals( table.nameR1_2xl ) )
			{
				numR1_2xl++;
				totalR1_2xl = totalR1_2xl + table.cpuR1_2xl;
			}
		}
	}
	
	public void release( ArrayList<VM> vmListB, ArrayList<NC> ncList, Table table )
	{
		NC nc = null;
		VM vm = null;
		int i, j;
		
		for( i = 0; i < vmListB.size(); i++ )
		{
			vm = vmListB.get(i);
			if( vm.ncId != 0 )
			{
				//��NC���з���CPU���ڴ�
				for ( j = 0; j < ncList.size(); j++ )
				{
					nc = ncList.get(j);
					if( j == vm.ncId-1 )
					{
						nc.usedCpu = nc.usedCpu - vm.cpu;
						nc.usedMemory = nc.usedMemory - vm.memory;
						break;
					}
				}
				//����Դ���з���VM��Դ
				if( vm.vmType.contentEquals( table.nameC1_l ) )
				{
					numC1_l--;
					totalC1_l = totalC1_l - table.cpuC1_l;
				}
				else if( vm.vmType.contentEquals( table.nameC1_xl ) )
				{
					numC1_xl--;
					totalC1_xl = totalC1_xl - table.cpuC1_xl;
				}
				else if( vm.vmType.contentEquals( table.nameC1_2xl ) )
				{
					numC1_2xl--;
					totalC1_2xl = totalC1_2xl - table.cpuC1_2xl;
					
				}
				else if( vm.vmType.contentEquals( table.nameG1_l ) )
				{
					numG1_l--;
					totalG1_l = totalG1_l - table.cpuG1_l;
				}
				else if( vm.vmType.contentEquals( table.nameG1_xl ) )
				{
					numG1_xl--;
					totalG1_xl = totalG1_xl - table.cpuG1_xl;
				}
				else if( vm.vmType.contentEquals( table.nameG1_2xl ) )
				{
					numG1_2xl--;
					totalG1_2xl = totalG1_2xl - table.cpuG1_2xl;
				}
				else if( vm.vmType.contentEquals( table.nameR1_l ) )
				{
					numR1_l--;
					totalR1_l = totalR1_l - table.cpuR1_l;
				}
				else if( vm.vmType.contentEquals( table.nameR1_xl ) )
				{
					numR1_xl--;
					totalR1_xl = totalR1_xl - table.cpuR1_xl;
				}
				else if( vm.vmType.contentEquals( table.nameR1_2xl ) )
				{
					numR1_2xl--;
					totalR1_2xl = totalR1_2xl - table.cpuR1_2xl;
				}
			}
		}
	}
}
