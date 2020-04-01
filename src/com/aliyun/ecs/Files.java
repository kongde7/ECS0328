package com.aliyun.ecs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Files
{
	//输出文件初始化，只运行一次
	public void First() throws IOException
	{
		Files files = new Files();
		File file1 = new File( "vm.csv" );
		File file2 = new File( "nc.csv" );
		File file3 = new File( "new_nc.csv" );
		
		//判断文件是否存在，存在的话就删掉，花里胡哨！
	    if( file1.exists() )
	    {
	    	file1.delete();
	        System.out.println( "vm.csv已存在，已经帮你扔掉了，下一个更好！" );
	    }
	    if( file2.exists() )
	    {
	    	file2.delete();
	        System.out.println( "nc.csv已存在，已经帮你扔掉了，下一个更乖！" );
	    }
	    if( file3.exists() )
	    {
	    	file3.delete();
	        System.out.println( "new_nc.csv已存在，已经帮你扔掉了，下一个更听话！" );
	    }
	    
	    files.TitleVM( "vm.csv" );
	    files.TitleNC( "nc.csv" );
	    files.TitleNC( "new_nc.csv" );
	    
	}
	
	//给VM输出的文件加标题，更整齐！
	public void TitleVM( String fileName ) throws IOException
	{
	    BufferedWriter bw = new BufferedWriter( new FileWriter( fileName, true ) );
	    
	    String outputDate = "\"" + "outputDate" + "\"" + ",";
	    String vmId = "\"" + "vmId" + "\"" + ",";
	    String status = "\"" + "status" + "\"" + ",";
	    String ncId = "\"nc_" + "ncId" + "\"" + ",";
	    String vmType = "\"" + "vmType" + "\"" + ",";
	    String cpu = "\"" + "cpu" + "\"" + ",";
	    String memory = "\"" + "memory" + "\"" + ",";
	    String createTime = "\"" + "createTime" + "\"" + ",";
	    String releaseTime = "\"" + "releaseTime" + "\"";
	    
	    String line = outputDate + vmId + status + ncId + vmType + cpu + memory + createTime + releaseTime;
		bw.write( line );
		bw.newLine();
		bw.close();
	}
	
	//给NC输出的文件加标题，更美观！
	public void TitleNC( String fileName ) throws IOException
	{
		BufferedWriter bw = new BufferedWriter( new FileWriter( fileName, true ) );
		
		String outputDate = "\"" + "outputDate" + "\"" + ",";
		String ncId = "\"nc_" + "ncId" + "\"" + ",";
		String status = "\"" + "status" + "\"" + ",";
		String totalCpu = "\"" + "totalCpu" + "\"" + ",";
		String totalMemory = "\"" + "totalMemory" + "\"" + ",";
		String machineType = "\"" + "machineType" + "\"" + ",";
		String usedCpu = "\"" + "usedCpu" + "\"" + ",";
		String usedMemory = "\"" + "usedMemory" + "\"" + ",";
		String createTime = "\"" + "createTime" + "\"";
		
		String line = outputDate + ncId + status + totalCpu + totalMemory + machineType + usedCpu + usedMemory + createTime;
		bw.write( line );
		bw.newLine();
		bw.close();
	}
	
}
