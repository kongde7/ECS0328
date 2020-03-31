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
	static int count;
	
	public void Enable( int n1, int n2, int n3, ArrayList<NC> ncList, Times times, Resource res )
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
	
	public void Write( ArrayList<NC> ncList, Times times ) throws IOException
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
		
		BufferedWriter bw = new BufferedWriter( new FileWriter( "nc1.csv", true ) );
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
