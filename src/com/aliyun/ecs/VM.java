package com.aliyun.ecs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class VM
{
	int ncId;
	int cpu;
	int memory;
	
	String vmId;
	String vmType;
	String status;
	
	String createDate;
	String createTime;
	String releaseDate;
	String releaseTime;
	
	//VM分配到物理机，成功返回1，空间不足返回0
	public int Assign( ArrayList<NC> ncList, Resource res, Price price )
	{
		int i;
		NC nc = null;
		Table table = new Table();
		//判断那种类型的VM
		if( vmType.contentEquals(table.nameC1_l) || vmType.contentEquals(table.nameC1_xl) || vmType.contentEquals(table.nameC1_2xl) )
		{
			for( i=0; i<ncList.size(); i++ )
			{
				nc = ncList.get(i);
				//先判断物理机类型是不是符合要求
				if( nc.machineType.contentEquals( table.nameN1 ) || nc.machineType.contentEquals( table.nameN2 ) )
				{
					//再判断符合类型要求的物理机空间够不够
					if( nc.totalCpu - nc.usedCpu >= cpu )
					{
						if( nc.totalMemory - nc.usedMemory >= memory )
						{
							//循环找到可以分配的物理机，写入值
							nc.usedCpu = nc.usedCpu + cpu;
							nc.usedMemory = nc.usedMemory + memory;
							ncList.set( i, nc );
							//把物理机id写入虚拟机对象
							ncId = i + 1;
							break;
						}
					}
				}
			}
		}
		else if( vmType.contentEquals(table.nameR1_l) || vmType.contentEquals(table.nameR1_xl) || vmType.contentEquals(table.nameR1_2xl) )
		{
			for( i=ncList.size()-1; i>=0; i-- )
			{
				nc = ncList.get(i);
				//先判断物理机类型是不是符合要求
				if( nc.machineType.contentEquals( table.nameN3 ) || nc.machineType.contentEquals( table.nameN2 ) )
				{
					//再判断符合类型要求的物理机空间够不够
					if( nc.totalCpu - nc.usedCpu >= cpu )
					{
						if( nc.totalMemory - nc.usedMemory >= memory )
						{
							//循环找到可以分配的物理机，写入值
							nc.usedCpu = nc.usedCpu + cpu;
							nc.usedMemory = nc.usedMemory + memory;
							ncList.set( i, nc );
							//把物理机id写入虚拟机对象
							ncId = i + 1;
							break;
						}
					}
				}
			}
		}
		else
		{
			for( i=0; i<ncList.size(); i++ )
			{
				nc = ncList.get(i);
				//先判断物理机类型是不是符合要求
				if( nc.machineType.contentEquals( table.nameN2 ) )
				{
					//再判断符合类型要求的物理机空间够不够
					if( nc.totalCpu - nc.usedCpu >= cpu )
					{
						if( nc.totalMemory - nc.usedMemory >= memory )
						{
							//循环找到可以分配的物理机，写入值
							nc.usedCpu = nc.usedCpu + cpu;
							nc.usedMemory = nc.usedMemory + memory;
							ncList.set( i, nc );
							//把物理机id写入虚拟机对象
							ncId = i + 1;
							break;
						}
					}
				}
			}
		}
		if( ncId==0 )
		{
			//没有分配到物理机空间，断供情况
			price.duangong = price.duangong + 24*cpu;
			//System.out.println( "断供1次" );
			return 0;
		}
		else return 1;
	}
	
	public int ReadOne( int num, String fileName, Times times ) throws IOException
	{
		Table table = new Table();
		int i = -1;
		String line;
		String cut[] = new String[4];
		
		BufferedReader read = new BufferedReader( new FileReader( fileName ) );
		while( (line=read.readLine()) != null )
		{
			i++;
			if( i==num )
        	{
				cut = line.split( "," );
				vmId = cut[0].replace( "\"", "" );
				vmType = cut[1].replace( "\"", "" );
				createDate = cut[2].replace( "\"", "" );
				releaseDate = cut[3].replace( "\"", "" );
				
				//确保获取的是当天数据再进行程序
				if( createDate.contentEquals( times.getDate() ) )
				{
					//暂时先定义状态都为运行
					status = "running";
					
					//获取当前时分秒作为创建时分秒
					createTime = createDate + " " + times.getTime();
					
					//检测是不是无限期释放
					if( releaseDate.contentEquals( "\\N" ) )
					{
						releaseTime = releaseDate;
					}
					else
					{
						releaseTime = releaseDate + " " + "23:59:59";
					}

					
					//判断vm类型，给对象的cpu和内存成员赋值
					if( vmType.contentEquals( table.nameC1_l ) )
					{
						cpu = table.cpuC1_l;
						memory = table.memoryC1_l;
					}
					else if( vmType.contentEquals( table.nameC1_xl ) )
					{
						cpu = table.cpuC1_xl;
						memory = table.memoryC1_xl;
					}
					else if( vmType.contentEquals( table.nameC1_2xl ) )
					{
						cpu = table.cpuC1_2xl;
						memory = table.memoryC1_2xl;
					}
					else if( vmType.contentEquals( table.nameG1_l ) )
					{
						cpu = table.cpuG1_l;
						memory = table.memoryG1_l;
					}
					else if( vmType.contentEquals( table.nameG1_xl ) )
					{
						cpu = table.cpuG1_xl;
						memory = table.memoryG1_xl;
					}
					else if( vmType.contentEquals( table.nameG1_2xl ) )
					{
						cpu = table.cpuG1_2xl;
						memory = table.memoryG1_2xl;
					}
					else if( vmType.contentEquals( table.nameR1_l ) )
					{
						cpu = table.cpuR1_l;
						memory = table.memoryR1_l;
					}
					else if( vmType.contentEquals( table.nameR1_xl ) )
					{
						cpu = table.cpuR1_xl;
						memory = table.memoryR1_xl;
					}
					else if( vmType.contentEquals( table.nameR1_2xl ) )
					{
						cpu = table.cpuR1_2xl;
						memory = table.memoryR1_2xl;
					}
					else System.out.println( "出现了一个错误：遇到无法识别的VMType！" );
					break;
				}
				else
				{
					read.close();
					return 0;
				}
				
        	}
        }
        read.close();
        return 1;
	}
	
	public void Write( ArrayList<VM> vmList, Times times ) throws IOException
	{
		String outputDate;
		String vmId;
		String status;
		String ncId;
		String vmType;
		String cpu;
		String memory;
		String createTime;
		String releaseTime;
		
		int i;
		String line;
		VM vm = null;
		
		BufferedWriter bw = new BufferedWriter( new FileWriter( "vm.csv", true ) );
		for ( i = 0; i < vmList.size(); i++ )
		{
			vm = vmList.get(i);
			//全部转换成字符串
			outputDate = "\"" + times.getDate() + "\"" + ",";
			vmId = "\"" + vm.vmId + "\"" + ",";
			status = "\"" + vm.status + "\"" + ",";
			ncId = "\"nc_" + String.valueOf( vm.ncId ) + "\"" + ",";
			vmType = "\"" + vm.vmType + "\"" + ",";
			cpu = "\"" + String.valueOf( vm.cpu ) + "\"" + ",";
			memory = "\"" + String.valueOf( vm.memory ) + "\"" + ",";
			createTime = "\"" + vm.createTime + "\"" + ",";
			releaseTime = "\"" + vm.releaseTime + "\"";
			
			line = outputDate + vmId + status + ncId + vmType + cpu + memory + createTime + releaseTime;
			bw.write( line );
			bw.newLine();
		}
		bw.close();
	}
	
}