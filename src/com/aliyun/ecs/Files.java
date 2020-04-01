package com.aliyun.ecs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Files
{
	//����ļ���ʼ����ֻ����һ��
	public void First() throws IOException
	{
		Files files = new Files();
		File file1 = new File( "vm.csv" );
		File file2 = new File( "nc.csv" );
		File file3 = new File( "new_nc.csv" );
		
		//�ж��ļ��Ƿ���ڣ����ڵĻ���ɾ����������ڣ�
	    if( file1.exists() )
	    {
	    	file1.delete();
	        System.out.println( "vm.csv�Ѵ��ڣ��Ѿ������ӵ��ˣ���һ�����ã�" );
	    }
	    if( file2.exists() )
	    {
	    	file2.delete();
	        System.out.println( "nc.csv�Ѵ��ڣ��Ѿ������ӵ��ˣ���һ�����ԣ�" );
	    }
	    if( file3.exists() )
	    {
	    	file3.delete();
	        System.out.println( "new_nc.csv�Ѵ��ڣ��Ѿ������ӵ��ˣ���һ����������" );
	    }
	    
	    files.TitleVM( "vm.csv" );
	    files.TitleNC( "nc.csv" );
	    files.TitleNC( "new_nc.csv" );
	    
	}
	
	//��VM������ļ��ӱ��⣬�����룡
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
	
	//��NC������ļ��ӱ��⣬�����ۣ�
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
