package com.aliyun.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Adjust
{
	int[][] sortList = new int[3][6224];
	
	public void Index()
	{
		double[][] arr = new double[4][6224];
		int i, j, k, sumCpu, sumMemory, n=0;
		double income, temp;
		
		for( i=0; i<=48; i++ )
		{
			for( j=0; j<=32; j++ )
			{
				for( k=0; k<=16; k++ )
				{
					sumCpu = i*2 + j*2 + k*2;
					sumMemory = i*4 + j*8 + k*16;
					income = 0.39*i + 0.5*j + 0.66*k;
					
					// 满足条件的N2机型分配方法
					if( sumCpu<=96 && sumMemory<=256 )
					{
						arr[0][n] = i;
						arr[1][n] = j;
						arr[2][n] = k;
						arr[3][n] = income;
						n++;
					}
				}
			}
		}
		
		//对结果排序
		for( i=0; i<6224-1; i++ )
		{
			for( j=i+1; j<6224; j++ )
			{
				if( arr[3][j] > arr[3][i] )
				{
					temp = arr[0][j];
					arr[0][j] = arr[0][i];
					arr[0][i] = temp;
					
					temp = arr[1][j];
					arr[1][j] = arr[1][i];
					arr[1][i] = temp;
					
					temp = arr[2][j];
					arr[2][j] = arr[2][i];
					arr[2][i] = temp;
					
					temp = arr[3][j];
					arr[3][j] = arr[3][i];
					arr[3][i] = temp;
				}
			}
		}
		
		for( i=0; i<6224; i++ )
		{
			sortList[0][i] = (int) arr[0][i];
			sortList[1][i] = (int) arr[1][i];
			sortList[2][i] = (int) arr[2][i];
		}
	}
	
	public void Optimize( ArrayList<VM> vmList, ArrayList<NC> ncList, Resource res, Times times )
	{
		Table table = new Table();
		VM vm = null;
		NC nc = null;
		//需要的VM份数
		int c1 = res.numC1_l + res.numC1_xl * 2 + res.numC1_2xl * 4;
		int g1 = res.numG1_l + res.numG1_xl * 2 + res.numG1_2xl * 4;
		int r1 = res.numR1_l + res.numR1_xl * 2 + res.numR1_2xl * 4;
		
		//可分配的物理机数
		int n1 = res.numN1;
		int n2 = res.numN2;
		int n3 = res.numN3;
		
		int i, x, y, z, temp;
		
		//先把NC表全部物理机全部清除VM，等下重新分配
		for ( i = 0; i < ncList.size(); i++ )
		{
			nc = ncList.get(i);
			nc.usedCpu = 0;
			nc.usedMemory = 0;
		}
		
		//先填N1
		x = c1 / 32;
		temp = c1;
		if( n1 >= x )
		{
			//遍历VM表，更改ncid
			for ( i = 0; i < vmList.size(); i++ )
			{
				if( temp <= c1 - 32 * x )
				{
					break;
				}
				else
				{
					vm = vmList.get(i);
					
					//如果是2xl型号
					if( vm.vmType.contentEquals( table.nameC1_2xl ) )
					{
						//遍历NC表，填写使用的内存和CPU，并把新的ncId给n1
						for ( i = 0; i < ncList.size(); i++ )
						{
							nc = ncList.get(i);
							if( nc.machineType.contentEquals( table.nameN1 ) )
							{
								if( nc.totalCpu - nc.usedCpu >= 8 )
								{
									if( nc.totalMemory - nc.usedMemory >= 16 )
									{
										nc.usedCpu = nc.usedCpu + 8;
										nc.usedMemory = nc.usedMemory + 16;
										vm.ncId = nc.ncId;
										temp = temp - 4;
										break;
									}
								}
							}
						}
					}
					
					//如果是xl型号
					if( vm.vmType.contentEquals( table.nameC1_xl ) )
					{
						//遍历NC表，填写使用的内存和CPU，并把新的ncId给n1
						for ( i = 0; i < ncList.size(); i++ )
						{
							nc = ncList.get(i);
							if( nc.machineType.contentEquals( table.nameN1 ) )
							{
								if( nc.totalCpu - nc.usedCpu >= 4 )
								{
									if( nc.totalMemory - nc.usedMemory >= 8 )
									{
										nc.usedCpu = nc.usedCpu + 4;
										nc.usedMemory = nc.usedMemory + 8;
										vm.ncId = nc.ncId;
										temp = temp - 2;
										break;
									}
								}
							}
						}
					}
					
					//如果是l型号
					if( vm.vmType.contentEquals( table.nameC1_l ) )
					{
						//遍历NC表，填写使用的内存和CPU，并把新的ncId给n1
						for ( i = 0; i < ncList.size(); i++ )
						{
							nc = ncList.get(i);
							if( nc.machineType.contentEquals( table.nameN1 ) )
							{
								if( nc.totalCpu - nc.usedCpu >= 2 )
								{
									if( nc.totalMemory - nc.usedMemory >= 4 )
									{
										nc.usedCpu = nc.usedCpu + 2;
										nc.usedMemory = nc.usedMemory + 4;
										vm.ncId = nc.ncId;
										temp = temp - 1;
										break;
									}
								}
							}
						}
					}
				}
				
			}
			//调优完成，扣除需要c1的数量
			//c1 = c1 - 32 * x;
			c1 = temp;
		}
		else
		{
			c1 = c1 - 32 * n1;
		}
		
		//再填N4
		z = r1 / 32;
		if( n3 >= z )
		{
			r1 = r1 - 32 * z;
		}
		else
		{
			r1 = r1 - 32 * n3;
		}
		
		for( i=0; i<sortList.length; i++ )
		{
			if( c1>=sortList[0][i] && g1>=sortList[1][i] && r1>=sortList[2][i] )
			{
				c1 = c1 - sortList[0][i];
				g1 = g1 - sortList[1][i];
				r1 = r1 - sortList[2][i];
			}
		}
	}
}
