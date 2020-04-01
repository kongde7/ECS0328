package com.aliyun.ecs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class NC
{
	int ncId;
	int totalCpu;
	int totalMemory;
	String status;
	String machineType;
	int usedCpu;
	int usedMemory;
	String createTime;
	String enableTime;
	static int count;
	
	public void Enable( ArrayList<NC> ncList, ArrayList<NC> ncListNew, Times times, Resource res )
	{
		int i;
		NC nc = null;
		String date = times.getDate();
		
		for ( i = 0; i < ncListNew.size(); i++ )
		{
			nc = ncListNew.get(i);
			
			//如果日期符合今天，就说明今天有新增的报备好的物理机
			if( date.contentEquals( nc.enableTime ) )
			{
				nc.status = "free";
				ncList.add( nc );
				res.Add(nc);
				ncListNew.remove(i);
				i--;
			}
		}
	}
	
	//报备
	public void Report( int n1, int n2, int n3, ArrayList<NC> ncListNew, Times times, Price price )
	{
		int i;
		NC nc = null;
		Times timePlus = new Times();
		
		for( i=1; i<=n1; i++ )
		{
			count++;
			nc = new NC();
			nc.ncId = nc.ncId + count;
			nc.status = "init";
			nc.totalCpu = 64;
			nc.totalMemory = 128;
			nc.machineType = "NT-1-2";
			nc.enableTime = times.TenDay();
			nc.createTime = times.getDate();
			ncListNew.add( nc );
		}
		for( i=1; i<=n2; i++ )
		{
			count++;
			nc = new NC();
			nc.ncId = nc.ncId + count;
			nc.status = "init";
			nc.totalCpu = 96;
			nc.totalMemory = 256;
			nc.machineType = "NT-1-4";
			nc.enableTime = times.TenDay();
			nc.createTime = times.getDate();
			ncListNew.add( nc );
		}
		for( i=1; i<=n3; i++ )
		{
			count++;
			nc = new NC();
			nc.ncId = nc.ncId + count;
			nc.status = "init";
			nc.totalCpu = 104;
			nc.totalMemory = 516;
			nc.machineType = "NT-1-8";
			nc.enableTime = times.TenDay();
			nc.createTime = times.getDate();
			ncListNew.add( nc );
		}
		price.buhuo = 16*(n1+n2+n3);
	}
	
	public void Add( int n1, int n2, int n3, ArrayList<NC> ncList, Times times, Resource res )
	{
		int i;
		NC nc = null;
		
		for( i=1; i<=n1; i++ )
		{
			count++;
			nc = new NC();
			nc.ncId = nc.ncId + count;
			nc.status = "free";
			nc.totalCpu = 64;
			nc.totalMemory = 128;
			nc.machineType = "NT-1-2";
			nc.createTime = times.getDate();
			ncList.add( nc );
			res.Add( nc );
		}
		for( i=1; i<=n2; i++ )
		{
			count++;
			nc = new NC();
			nc.ncId = nc.ncId + count;
			nc.status = "free";
			nc.totalCpu = 96;
			nc.totalMemory = 256;
			nc.machineType = "NT-1-4";
			nc.createTime = times.getDate();
			ncList.add( nc );
			res.Add( nc );
		}
		for( i=1; i<=n3; i++ )
		{
			count++;
			nc = new NC();
			nc.ncId = nc.ncId + count;
			nc.status = "free";
			nc.totalCpu = 104;
			nc.totalMemory = 516;
			nc.machineType = "NT-1-8";
			nc.createTime = times.getDate();
			ncList.add( nc );
			res.Add( nc );
		}
	}
	
	public void Write( String fileName, ArrayList<NC> ncList, Times times ) throws IOException
	{
		String outputDate;
		String ncId;
		String status;
		String totalCpu;
		String totalMemory;
		String machineType;
		String usedCpu;
		String usedMemory;
		String createTime;
		
		String line;
		int i;
		NC nc = null;
		
		BufferedWriter bw = new BufferedWriter( new FileWriter( fileName, true ) );
		for ( i = 0; i < ncList.size(); i++ )
		{
			nc = ncList.get(i);
			//全部转换成字符串
			outputDate = "\"" + times.getDate() + "\"" + ",";
			ncId = "\"nc_" + String.valueOf( ncList.get(i).ncId ) + "\"" + ",";
			status = "\"" + ncList.get(i).status + "\"" + ",";
			totalCpu = "\"" + String.valueOf( ncList.get(i).totalCpu ) + "\"" + ",";
			totalMemory = "\"" + String.valueOf( ncList.get(i).totalMemory ) + "\"" + ",";
			machineType = "\"" + ncList.get(i).machineType + "\"" + ",";
			usedCpu = "\"" + ncList.get(i).usedCpu + "\"" + ",";
			usedMemory = "\"" + ncList.get(i).usedMemory + "\"" + ",";
			createTime = "\"" + ncList.get(i).createTime + "\"";
			line = outputDate + ncId + status + totalCpu + totalMemory + machineType + usedCpu + usedMemory + createTime;
			bw.write( line );
			bw.newLine();
		}
		bw.close();
	}
}
